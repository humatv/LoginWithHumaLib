package ir.huma.loginwithhuma

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import ir.huma.humastore.ILoginWithHumaService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


open class LoginWithDone(private val context: Context) {
    private val TAG = "LoginWithHuma"
    var clientKey: String? = null
        private set
    var scope: String? = "phone"
        private set
    var onLoginListener: OnLoginListener? = null
        private set
    var isNavigateToRegisterWizard: Boolean = true
        private set

    fun setClientKey(clientKey: String?): LoginWithDone {
        this.clientKey = clientKey
        return this
    }
    /**
     * if isNavigateToRegisterWizard == true then if user has not login
     * we navigate to wizard to register and if it is false we do not navigate to register
     * */
    fun setNavigateToRegister(isNavigateToRegisterWizard:Boolean): LoginWithDone {
        this.isNavigateToRegisterWizard = isNavigateToRegisterWizard
        return this
    }
    fun setOnLoginListener(onLoginListener: OnLoginListener?): LoginWithDone {
        this.onLoginListener = onLoginListener
        registerListeners()
        return this
    }

    fun setScope(scope: String?): LoginWithDone {
        this.scope = scope
        return this
    }


    fun send() {
        if (clientKey == null || clientKey == "") {
            throw RuntimeException("please set Client Key in java code!!!")
        }
        if (onLoginListener == null) {
            throw RuntimeException("please setOnLoginListener in java code!!!")
        }
        if (!isHumaPlatform) {
            Toast.makeText(context, "لطفا ابتدا برنامه هوما استور را نصب کنید.", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (scope == null || scope == "") {
            throw RuntimeException("Scope is not be Null!!!")
        }
        if (!isLoginV2Supported) {
            registerListeners()
            try {
                sendLoginToStore()
            } catch (e: Exception) {
                e.printStackTrace()
                if (isNavigateToRegisterWizard){
                    try {
                        sendLoginToProfile()
                    } catch (e2: Exception) {
                        e2.printStackTrace()
                    }
                }
            }
        } else {
            connectToLoginWithHuma()
        }
    }

    private var loginWithHumaService: ILoginWithHumaService? = null
    private var serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            loginWithHumaService = ILoginWithHumaService.Stub.asInterface(service)
            Log.d(TAG, "service connected!!")
            sendLoginToService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            loginWithHumaService = null
            Log.d(TAG, "service disconnected!!")
        }
    }

    private fun sendLoginToService() {
        runBlocking {
            launch {
                try {
                    val result = loginWithHumaService!!.startLogin(clientKey)
                    val response = convertJsonToObject(result)
                    Log.d(TAG, "sendLoginToService: $response")
                    Handler(Looper.getMainLooper()).post {
                        Log.d(TAG, "onServiceConnected: mainThread!!!")

                        if (response.isSuccess) {
                            onLoginListener?.onLogin(response.temporaryCode)
                        } else {
                            onLoginListener?.onFail(response.errorMessage, response.status)
                        }
                    }
                } catch (e: Exception) {
                    onLoginListener?.onFail(
                        e.message,
                        TemporaryCodeResponse.ResponseStatus.UnknownError
                    )
                }
                try {
                    context.unbindService(serviceConnection)
                    Log.d(TAG, "onServiceConnected: unbind")
                } catch (e: Exception) {

                }

            }
        }
    }

    private fun connectToLoginWithHuma() {
        val usageStatIntent = Intent("ir.huma.humastore.loginWithHuma")
        usageStatIntent.setPackage("ir.huma.humastore")
        context.bindService(usageStatIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun sendLoginToStore() {
        val `in` = Intent(Intent.ACTION_VIEW, Uri.parse("app://login.huma.ir"))
        `in`.setPackage("ir.huma.humastore")
        `in`.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        `in`.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        `in`.putExtra("key", clientKey)
        `in`.putExtra("package", context.packageName)
        `in`.putExtra("scope", scope)
        context.startActivity(`in`)
    }

    private fun sendLoginToProfile() {
        val `in` = Intent(Intent.ACTION_VIEW, Uri.parse("app://wizard.huma.ir"))
        `in`.setPackage("ir.huma.humawizard")
        `in`.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        `in`.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        `in`.putExtra("key", clientKey)
        `in`.putExtra("package", context.packageName)
        `in`.putExtra("scope", scope)
        context.startActivity(`in`)
    }

    private fun registerListeners() {
        context.registerReceiver(receiver, IntentFilter(receive))
    }

    fun unregiter() {
        try {
            context.unregisterReceiver(receiver)
        } catch (e: Exception) {
        }
    }

    private val isHumaPlatform: Boolean
        get() {
            try {
                val packageInfo = context.packageManager.getPackageInfo("ir.huma.humastore", 0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && packageInfo.longVersionCode > 44) {
                    return true
                } else if (packageInfo.versionCode > 44) {
                    return true
                }
            } catch (e: PackageManager.NameNotFoundException) {
            }
            return try {
                context.packageManager.getPackageInfo("ir.huma.humawizard", 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    private val isLoginV2Supported: Boolean
        get() {
            try {
                val packageInfo = context.packageManager.getPackageInfo("ir.huma.humastore", 0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && packageInfo.longVersionCode > 110) {
                    return true
                } else if (packageInfo.versionCode > 110) {
                    return true
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return false
        }
    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (onLoginListener != null) {
                    if (intent.hasExtra("packageName") && intent.getStringExtra("packageName") == context.packageName) {
                        if (intent.getBooleanExtra("success", false)) {
                            onLoginListener!!.onLogin(intent.getStringExtra("message"))
                        } else {
                            onLoginListener!!.onFail(
                                intent.getStringExtra("message"),
                                TemporaryCodeResponse.ResponseStatus.UnknownError
                            )
                        }
                        try {
                            context.unregisterReceiver(this)
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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


    interface OnLoginListener {
        fun onLogin(code: String?)
        fun onFail(message: String?, status: TemporaryCodeResponse.ResponseStatus?)
    }

    companion object {
        private const val send = "ir.huma.android.launcher.login"
        private const val receive = "ir.huma.android.launcher.loginResponse"
    }
}