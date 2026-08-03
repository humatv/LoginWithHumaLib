package ir.huma.loginwithhuma.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import ir.huma.loginwithhuma.OnLoginListener
import ir.huma.loginwithhuma.TemporaryCodeResponse

internal class AuthV1(
    private val clientId: String,
    private val loginListener: OnLoginListener,
    private val isNavigateToRegister: Boolean,
    private val scope: String,
    private val context: Context
) : Auth {

    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (intent.hasExtra("packageName") && intent.getStringExtra("packageName") == context.packageName) {
                    if (intent.getBooleanExtra("success", false)) {
                        loginListener.onLogin(intent.getStringExtra("message"))
                    } else {
                        loginListener.onFail(
                            intent.getStringExtra("message"),
                            TemporaryCodeResponse.ResponseStatus.UnknownError
                        )
                    }
                    runCatching {
                        context.unregisterReceiver(this)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun connect() {
        registerListener()
        try {
            sendLoginToStore()
        } catch (e: Exception) {
            e.printStackTrace()
            if (isNavigateToRegister) {
                try {
                    sendLoginToProfile()
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
        }
    }

    override fun disconnect() {
        runCatching { context.unregisterReceiver(receiver) }
    }

    private fun registerListener() {
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(receive),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    private fun sendLoginToStore() {
        val intent = Intent(Intent.ACTION_VIEW, "app://login.huma.ir".toUri()).apply {
            setPackage("ir.huma.humastore")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            putExtra("key", clientId)
            putExtra("package", context.packageName)
            putExtra("scope", scope)
        }
        context.startActivity(intent)
    }

    private fun sendLoginToProfile() {
        val intent = Intent(Intent.ACTION_VIEW, "app://wizard.huma.ir".toUri()).apply {
            setPackage("ir.huma.humawizard")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            putExtra("key", clientId)
            putExtra("package", context.packageName)
            putExtra("scope", scope)
        }
        context.startActivity(intent)
    }


    companion object {
        private const val receive = "ir.huma.android.launcher.loginResponse"
    }
}