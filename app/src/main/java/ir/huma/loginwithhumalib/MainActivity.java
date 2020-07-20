package ir.huma.loginwithhumalib;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ir.huma.loginwithhuma.LoginWithHuma;
import ir.huma.loginwithhuma.LoginWithHumaButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginWithHumaButton button = findViewById(R.id.loginWithButton);
        button.setClientKey("<your token>").setOnLoginListener(new LoginWithHuma.OnLoginListener() {
            @Override
            public void onLogin(String code) {
                Toast.makeText(MainActivity.this, "login!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(MainActivity.this, "cancel or failed!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLoginClick(View view) {
        new LoginWithHuma(this)
                .setClientKey("<your token>")
                .setOnLoginListener(new LoginWithHuma.OnLoginListener() {
                    @Override
                    public void onLogin(String code) {
                        Toast.makeText(MainActivity.this, "login!!!", Toast.LENGTH_SHORT).show();

                        // send code to your server here!
                    }

                    @Override
                    public void onFail(String message) {
                        Toast.makeText(MainActivity.this, "cancel or failed!!!", Toast.LENGTH_SHORT).show();
                    }
                }).send();

    }
}
