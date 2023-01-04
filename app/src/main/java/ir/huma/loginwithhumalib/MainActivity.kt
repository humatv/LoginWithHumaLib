package ir.huma.loginwithhumalib


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.huma.loginwithhuma.LoginWithHuma
import ir.huma.loginwithhuma.LoginWithHuma.OnLoginListener
import ir.huma.loginwithhuma.TemporaryCodeResponse

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.loginWithButton)

        button.setOnClickListener {
            findViewById<View>(R.id.progress).visibility = View.VISIBLE
            LoginWithHuma(
                this@MainActivity
            ).setClientKey("52079f47ba2344de96862d1186be15a5")
                .setOnLoginListener(object : OnLoginListener {
                    override fun onLogin(code: String?) {
                        findViewById<View>(R.id.progress).visibility = View.GONE
                        Toast.makeText(this@MainActivity, "login!!!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFail(
                        message: String?,
                        status: TemporaryCodeResponse.ResponseStatus?
                    ) {
                        findViewById<View>(R.id.progress).visibility = View.GONE
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }).send()
        }
    }

    fun onLoginClick(view: View?) {}
}