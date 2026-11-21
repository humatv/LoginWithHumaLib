package ir.huma.loginwithhuma

import android.content.Context
import ir.huma.loginwithhuma.LoginWithDone.OnLoginListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Guards the public API of 4.1.1: code written against it must keep compiling
 * (with deprecation warnings only) when the dependency is bumped to 4.2.0.
 *
 * Note the explicit import of the nested `LoginWithDone.OnLoginListener`, exactly like
 * old client code had.
 */
@Suppress("DEPRECATION", "unused")
class BackwardCompatibilityTest {

    // Compile-time check only. This function is never executed.
    private fun legacyUsage(context: Context) {
        val listener = object : OnLoginListener {
            override fun onLogin(code: String?) {}
            override fun onFail(message: String?, status: TemporaryCodeResponse.ResponseStatus?) {}
        }

        val loginWithDone = LoginWithDone(context)
            .setClientKey("key")
            .setScope("phone")
            .setNavigateToRegister(true)
            .setOnLoginListener(listener)
        loginWithDone.send()
        loginWithDone.unregiter()

        LoginWithDone(context, CoroutineScope(Dispatchers.IO))
            .setClientKey(null)
            .setOnLoginListener(null)

        val key: String? = loginWithDone.clientKey
        val scope: String? = loginWithDone.scope
        val savedListener: OnLoginListener? = loginWithDone.onLoginListener as? OnLoginListener
        val navigate: Boolean = loginWithDone.isNavigateToRegisterWizard
        val coroutineScope: CoroutineScope = loginWithDone.coroutineScope

        LoginWithDoneButton(context)
            .setClientKey(null)
            .setClientKey("key")
            .setOnLoginListener(listener)
            .setOnLoginListener(null)
    }

    @Test
    fun legacyListenerIsAssignableToNewListener() {
        assertTrue(
            ir.huma.loginwithhuma.OnLoginListener::class.java
                .isAssignableFrom(OnLoginListener::class.java)
        )
    }

    @Test
    fun newListenerCanStillBeUsedDirectly() {
        val listener = object : ir.huma.loginwithhuma.OnLoginListener {
            override fun onLogin(code: String?) {}
            override fun onFail(message: String?, status: TemporaryCodeResponse.ResponseStatus?) {}
        }
        assertTrue(listener is ir.huma.loginwithhuma.OnLoginListener)
    }
}
