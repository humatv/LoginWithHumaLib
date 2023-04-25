package ir.huma.loginwithhumalib


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.huma.loginwithhuma.LoginWithDone
import ir.huma.loginwithhuma.LoginWithHuma
import ir.huma.loginwithhuma.LoginWithDone.OnLoginListener
import ir.huma.loginwithhuma.LoginWithDoneButton
import ir.huma.loginwithhuma.TemporaryCodeResponse

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
//            setClientKey("52079f47ba2344de96862d1186be15a5")
            setOnClickListener {
                progressBar.visibility = View.VISIBLE
            }
            setOnLoginListener(object :OnLoginListener{
                override fun onLogin(code: String?) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "login!!!", Toast.LENGTH_SHORT).show()
                }

                override fun onFail(
                    message: String?,
                    status: TemporaryCodeResponse.ResponseStatus?,
                ) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun rawButtonLogin(button: Button?) {
        button?.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            LoginWithDone(
                this@MainActivity
            ).setClientKey("52079f47ba2344de96862d1186be15a5")
                .setOnLoginListener(object : OnLoginListener {
                    override fun onLogin(code: String?) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "login!!!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFail(
                        message: String?,
                        status: TemporaryCodeResponse.ResponseStatus?
                    ) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }).send()
        }

    }

    fun onLoginClick(view: View?) {}
}