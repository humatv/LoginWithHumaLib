package ir.huma.loginwithhumalib


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.huma.loginwithhuma.LoginWithDone
import ir.huma.loginwithhuma.LoginWithDone.OnLoginListener
import ir.huma.loginwithhuma.LoginWithDoneButton
import ir.huma.loginwithhuma.ResponseStatus
import ir.huma.loginwithhuma.TemporaryCodeResponse

class MainActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var resultText: TextView
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById<ProgressBar>(R.id.progress)
        resultText = findViewById<TextView>(R.id.result_text)

        val buttonRaw = findViewById<Button>(R.id.loginWithButton)
        val buttonDone = findViewById<LoginWithDoneButton>(R.id.loginWithButton2)

        rawButtonLogin(buttonRaw)
        doneButtonLogin(buttonDone)

    }
    private fun onLoginResult(code: String?) {
        progressBar.visibility = View.GONE
        resultText.text = "code:$code"
        Toast.makeText(this@MainActivity, "login!!!$code", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onLoginResult: $code")

    }
    private fun onErrorResult(message: String?, status: ResponseStatus?) {
        resultText.text = "error:$message // $status"
        progressBar.visibility = View.GONE
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onErrorResult: message : $message ")
        Log.d(TAG, "onErrorResult: status:  $status ")
    }

    private fun doneButtonLogin(button: LoginWithDoneButton?) {
        button?.apply {
//            setClientKey("52079f47ba2344de96862d1186be15a5")
            setOnClickListener {
                progressBar.visibility = View.VISIBLE
            }
            setOnLoginListener(object :OnLoginListener{
                override fun onLogin(code: String?) {
                    onLoginResult(code)
                    }

                override fun onFail(
                    message: String?,
                    status: ResponseStatus?,
                ) {
                    onErrorResult(message,status)

                }
            })
        }
    }

    private fun rawButtonLogin(button: Button?) {
        button?.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            LoginWithDone(
                this@MainActivity
            ).setClientKey("3e90dad8c22d68d56a15c54b35c8ea35")
                .setOnLoginListener(object : OnLoginListener {
                    override fun onLogin(code: String?) {
                        progressBar.visibility = View.GONE
                        resultText.text = "code:$code"
                        Toast.makeText(this@MainActivity, "login!!!$code", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFail(
                        message: String?,
                        status: ResponseStatus?
                    ) {
                        resultText.text = "error:$message // $status"
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }).send()
        }

    }

    fun onLoginClick(view: View?) {}
}