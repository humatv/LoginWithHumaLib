package ir.huma.loginwithhuma.auth

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteException
import android.util.Log
import androidx.core.content.ContextCompat
import ir.huma.loginwithhuma.OnLoginListener
import tech.done.user.ILoginCallback
import tech.done.user.ILoginWithDoneService

private val TAG = AuthV3::class.java.simpleName

internal class AuthV3(
    private val clientId: String,
    private val loginListener: OnLoginListener,
    private val context: Context
) : Auth {

    private var loginWithDoneService: ILoginWithDoneService? = null
    private var isConnected = false
    private val mainHandler = Handler(Looper.getMainLooper())

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "Done Auth V3 has connected")
            loginWithDoneService = ILoginWithDoneService.Stub.asInterface(service)
            isConnected = true

            try {
                loginWithDoneService?.register(loginCallback)
                loginWithDoneService?.login(clientId)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "Done Auth V3 has unexpectedly disconnected")
            loginWithDoneService = null
            isConnected = false
        }
    }

    private val loginCallback = object : ILoginCallback.Stub() {

        // AIDL callbacks run on a binder thread. OnLoginListener has always been invoked on the
        // main thread (AuthV1/AuthV2 and 4.1.1), and clients update UI in it, so keep that contract.
        override fun onSuccess(code: String?) {
            mainHandler.post { loginListener.onLogin(code) }
        }

        override fun onFailure(message: String?) {
            mainHandler.post { loginListener.onFail(message, null) }
        }
    }

    override fun connect() {
        val intent = Intent("tech.done.user.loginWithDone").apply {
            setPackage("ir.huma.android.launcher")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val executor = ContextCompat.getMainExecutor(context)
            context.bindService(intent, Context.BIND_AUTO_CREATE, executor, connection)
        } else {
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun disconnect() {
        if (isConnected) {
            runCatching { loginWithDoneService?.unregister(loginCallback) }
            loginWithDoneService = null
            isConnected = false
            runCatching { context.unbindService(connection) }
        }
    }
}