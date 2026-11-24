package ir.huma.loginwithhuma

import android.content.Context
import android.widget.Toast
import ir.huma.loginwithhuma.auth.Auth
import ir.huma.loginwithhuma.auth.AuthFactory
import ir.huma.loginwithhuma.auth.AuthVersion
import ir.huma.loginwithhuma.auth.AuthVersionDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class LoginWithDone(private val context: Context) {

    @Deprecated(
        "The coroutine scope is managed internally. Will be removed in the next major version.",
        ReplaceWith("LoginWithDone(context)")
    )
    constructor(context: Context, coroutineScope: CoroutineScope) : this(context) {
        authScope = coroutineScope
    }

    private var authScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private var clientId: String? = null
    private var loginScope: String? = "phone"
    private var loginListener: ir.huma.loginwithhuma.OnLoginListener? = null
    private var navigateToRegister: Boolean = true

    private var auth: Auth? = null

    fun setClientId(value: String?): LoginWithDone = apply { clientId = value }

    fun setOnLoginListener(listener: ir.huma.loginwithhuma.OnLoginListener?): LoginWithDone =
        apply { loginListener = listener }

    fun setScope(value: String?): LoginWithDone = apply { loginScope = value }

    /**
     * if value == true then if user has not login
     * we navigate to wizard to register and if it is false we do not navigate to register
     * */
    fun setNavigateToRegister(value: Boolean): LoginWithDone =
        apply { navigateToRegister = value }

    fun login() {
        val id = clientId
        require(!id.isNullOrEmpty()) { "Client id must be set" }
        val listener = requireNotNull(loginListener) { "OnLoginListener must be set" }

        val authVersion = AuthVersionDetector.getAuthVersion(context)
        if (authVersion == AuthVersion.NONE) {
            Toast.makeText(context, "لطفا ابتدا برنامه Done UI را نصب کنید.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val currentScope = loginScope
        require(!currentScope.isNullOrEmpty()) { "Scope must be set" }

        unregister()
        auth = AuthFactory.create(
            authVersion,
            id,
            listener,
            context,
            navigateToRegister,
            currentScope,
            authScope
        )
        auth?.connect()
    }

    fun unregister() {
        auth?.disconnect()
    }

    // ---------------------------------------------------------------------------------------
    // Backward compatibility with 4.1.1 and earlier. Old code keeps compiling and working.
    // To be removed in the next major version.
    // ---------------------------------------------------------------------------------------

    @Deprecated("Will be removed in the next major version")
    val clientKey: String? get() = clientId

    @Deprecated("Will be removed in the next major version")
    val scope: String? get() = loginScope

    @Deprecated("Will be removed in the next major version")
    val onLoginListener: ir.huma.loginwithhuma.OnLoginListener? get() = loginListener

    @Deprecated("Will be removed in the next major version")
    val isNavigateToRegisterWizard: Boolean get() = navigateToRegister

    @Deprecated("Will be removed in the next major version")
    val coroutineScope: CoroutineScope get() = authScope

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
