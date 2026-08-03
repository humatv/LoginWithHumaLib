package ir.huma.loginwithhuma

import android.content.Context
import android.widget.Toast
import ir.huma.loginwithhuma.auth.Auth
import ir.huma.loginwithhuma.auth.AuthFactory
import ir.huma.loginwithhuma.auth.AuthVersionDetector
import ir.huma.loginwithhuma.auth.AuthVersion

open class LoginWithDone(
    private val context: Context
) {

    private var clientId: String? = null
    private var scope: String = "phone"
    private var loginListener: OnLoginListener? = null
    private var isNavigateToRegisterUser: Boolean = true

    private var auth: Auth? = null

    fun setClientId(value: String) = apply { clientId = value }

    fun setOnLoginListener(listener: OnLoginListener) = apply { loginListener = listener }

    fun setScope(value: String) = apply { scope = value }

    fun setNavigateToRegister(value: Boolean) = apply { isNavigateToRegisterUser = value }

    fun login() {
        require(!clientId.isNullOrEmpty()) { "Client id must be set" }
        requireNotNull(loginListener) { "OnLoginListener must be set" }
        require(scope.isNotEmpty()) { "Scope must be set" }

        val authVersion = AuthVersionDetector.getAuthVersion(context)
        if (authVersion == AuthVersion.NONE) {
            Toast.makeText(context, "لطفا ابتدا برنامه Done UI را نصب کنید.", Toast.LENGTH_SHORT)
                .show()
            return
        }
        unregister()
        auth = AuthFactory.create(
            authVersion,
            clientId!!,
            loginListener!!,
            context,
            isNavigateToRegisterUser,
            scope
        )
        auth?.connect()
    }

    fun unregister() {
        auth?.disconnect()
    }
}