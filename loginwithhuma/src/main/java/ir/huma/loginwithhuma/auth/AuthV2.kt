package ir.huma.loginwithhuma.auth

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import ir.huma.humastore.ILoginWithHumaService
import ir.huma.loginwithhuma.OnLoginListener
import ir.huma.loginwithhuma.TemporaryCodeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

private val TAG = AuthV2::class.java.simpleName

internal class AuthV2(
    private val clientId: String,
    private val loginListener: OnLoginListener,
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
): Auth {

    private var loginWithHumaService: ILoginWithHumaService? = null

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "Done Auth V2 has connected")
            loginWithHumaService = ILoginWithHumaService.Stub.asInterface(service)
            scope.launch(Dispatchers.IO) {
                sendLoginToService()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "Done Auth V2 has unexpectedly disconnected")
            loginWithHumaService = null
        }
    }

    private suspend fun sendLoginToService() {
        try {
            val result = loginWithHumaService!!.startLogin(clientId)
            val response = convertJsonToObject(result)
            Log.d(TAG, "sendLoginToService: $response")
            withContext(Dispatchers.Main) {
                Log.d(TAG, "onServiceConnected: mainThread!!!")

                if (response.isSuccess) loginListener.onLogin(response.temporaryCode)
                else loginListener.onFail(response.errorMessage, response.status)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                loginListener.onFail(e.message, TemporaryCodeResponse.ResponseStatus.UnknownError)
            }
        }

        runCatching {
            context.unbindService(connection)
            Log.d(TAG, "onServiceConnected: unbind")
        }
    }

    private fun convertJsonToObject(str: String): TemporaryCodeResponse {
        val mapper = ObjectMapper()
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        mapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, true)
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false)
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        mapper.registerModule(KotlinModule())
        return mapper.readValue(str, TemporaryCodeResponse::class.java)
    }

    override fun connect() {
        val intent = Intent("ir.huma.humastore.loginWithHuma").apply {
            setPackage("ir.huma.humastore")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.bindService(
                intent,
                Context.BIND_AUTO_CREATE,
                Executors.newSingleThreadExecutor(),
                connection
            )
        } else {
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun disconnect() {
        runCatching { context.unbindService(connection) }
    }
}