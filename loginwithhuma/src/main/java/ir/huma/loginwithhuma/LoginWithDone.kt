package ir.huma.loginwithhuma

import android.content.Context
import android.widget.Toast
import ir.huma.loginwithhuma.auth.Auth
import ir.huma.loginwithhuma.auth.AuthFactory
import ir.huma.loginwithhuma.auth.AuthVersion
import ir.huma.loginwithhuma.auth.AuthVersionDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class LoginWithDone(
    private val context: Context,
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    var clientKey: String? = null
        private set
    var scope: String? = "phone"
        private set
    var onLoginListener: ir.huma.loginwithhuma.OnLoginListener? = null
        private set
    var isNavigateToRegisterWizard: Boolean = true
        private set

    private var auth: Auth? = null

    fun setClientId(value: String?): LoginWithDone {
        clientKey = value
        return this
    }

    fun setOnLoginListener(listener: ir.huma.loginwithhuma.OnLoginListener?): LoginWithDone {
        onLoginListener = listener
        return this
    }

    fun setScope(value: String?): LoginWithDone {
        scope = value
        return this
    }

    /**
     * if value == true then if user has not login
     * we navigate to wizard to register and if it is false we do not navigate to register
     * */
    fun setNavigateToRegister(value: Boolean): LoginWithDone {
        isNavigateToRegisterWizard = value
        return this
    }

    fun login() {
        val clientId = clientKey
        val listener = onLoginListener
        val currentScope = scope

        if (clientId.isNullOrEmpty()) {
            throw RuntimeException("please set Client Key in java code!!!")
        }
        if (listener == null) {
            throw RuntimeException("please setOnLoginListener in java code!!!")
        }

        val authVersion = AuthVersionDetector.getAuthVersion(context)
        if (authVersion == AuthVersion.NONE) {
            Toast.makeText(context, "لطفا ابتدا برنامه Done UI را نصب کنید.", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (currentScope.isNullOrEmpty()) {
            throw RuntimeException("Scope is not be Null!!!")
        }

        unregister()
        auth = AuthFactory.create(
            authVersion,
            clientId,
            listener,
            context,
            isNavigateToRegisterWizard,
            currentScope,
            coroutineScope
        )
        auth?.connect()
    }

    fun unregister() {
        auth?.disconnect()
    }

    // ---------------------------------------------------------------------------------------
    // Backward compatibility with 4.1.1 and earlier. Old code keeps compiling and working.
    // ---------------------------------------------------------------------------------------

    @Deprecated("Use setClientId", ReplaceWith("setClientId(clientKey)"))
    fun setClientKey(clientKey: String?): LoginWithDone = setClientId(clientKey)

    @Deprecated("Use login()", ReplaceWith("login()"))
    fun send() = login()

    @Deprecated("Use unregister()", ReplaceWith("unregister()"))
    fun unregiter() = unregister()

    /**
     * Kept only so that code written against 4.1.1
     * (`import ir.huma.loginwithhuma.LoginWithDone.OnLoginListener`) still compiles.
     * New code should use [ir.huma.loginwithhuma.OnLoginListener].
     */
    @Deprecated(
        "Use ir.huma.loginwithhuma.OnLoginListener",
        ReplaceWith("ir.huma.loginwithhuma.OnLoginListener")
    )
    interface OnLoginListener : ir.huma.loginwithhuma.OnLoginListener
}
