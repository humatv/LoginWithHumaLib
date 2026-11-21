package ir.huma.loginwithhumalib


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ir.huma.loginwithhuma.LoginWithDone
import ir.huma.loginwithhuma.LoginWithDoneButton
import ir.huma.loginwithhuma.OnLoginListener
import ir.huma.loginwithhuma.TemporaryCodeResponse
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById<ProgressBar>(R.id.progress)

        val buttonRaw = findViewById<Button>(R.id.loginWithButton)
        val buttonDone = findViewById<LoginWithDoneButton>(R.id.loginWithButton2)

        rawButtonLogin(buttonRaw)
        doneButtonLogin(buttonDone)

    }

    private fun doneButtonLogin(button: LoginWithDoneButton?) {
        button?.apply {
            setOnClickListener {
                progressBar.visibility = View.VISIBLE
            }
            setOnLoginListener(object : OnLoginListener {
                override fun onLogin(code: String?) {
                    lifecycleScope.launch {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "login!!!", Toast.LENGTH_SHORT).show()
                        loginWithDone?.unregister()
                    }
                }

                override fun onFail(
                    message: String?,
                    status: TemporaryCodeResponse.ResponseStatus?,
                ) {
                    lifecycleScope.launch {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                        loginWithDone?.unregister()
                    }
                }
            })
        }
    }

    private fun rawButtonLogin(button: Button?) {
        button?.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val loginWithDone = LoginWithDone(
                this@MainActivity
            )
            loginWithDone.setClientId("52079f47ba2344de96862d1186be15a5")
                .setOnLoginListener(object : OnLoginListener {
                    override fun onLogin(code: String?) {
                        lifecycleScope.launch {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, "login!!!", Toast.LENGTH_SHORT).show()
                            loginWithDone.unregister()
                        }
                    }

                    override fun onFail(
                        message: String?,
                        status: TemporaryCodeResponse.ResponseStatus?
                    ) {
                        lifecycleScope.launch {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                            loginWithDone.unregister()
                        }
                    }
                }).login()
        }

    }
}