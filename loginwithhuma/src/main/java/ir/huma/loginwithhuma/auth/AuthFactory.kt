package ir.huma.loginwithhuma.auth

import android.content.Context
import ir.huma.loginwithhuma.OnLoginListener

internal object AuthFactory {

    fun create(
        version: AuthVersion,
        clientId: String,
        loginListener: OnLoginListener,
        context: Context,
        isNavigateToRegister: Boolean = true,
        scope: String = "phone"
    ): Auth = when (version) {
        AuthVersion.V3 -> AuthV3(clientId, loginListener, context)
        AuthVersion.V2 -> AuthV2(clientId, loginListener, context)
        AuthVersion.V1 -> AuthV1(clientId, loginListener, isNavigateToRegister, scope, context)
        AuthVersion.NONE -> throw IllegalStateException("Auth version is Unknown")
    }
}