package ir.huma.loginwithhumalib;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ir.huma.loginwithhuma.LoginWithHuma;
import ir.huma.loginwithhuma.LoginWithHumaButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginWithHumaButton button = findViewById(R.id.loginWithButton);
        button.setClientKey("52079f47ba2344de96862d1186be15a5").setOnLoginListener(new LoginWithHuma.OnLoginListener() {
            @Override
            public void onLogin(String code) {
                Toast.makeText(MainActivity.this, "login!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLoginClick(View view) {
//        new LoginWithHuma(this)
//                .setClientKey("46d133bc2abeea10afa8dfcd887e74d4")
//                .setOnLoginListener(new LoginWithHuma.OnLoginListener() {
//                    @Override
//                    public void onLogin(String code) {
//                        Toast.makeText(MainActivity.this, "login!!!222", Toast.LENGTH_SHORT).show();
//
//                        // send code to your server here!
//                    }
//
//                    @Override
//                    public void onFail(String message) {
//                        Toast.makeText(MainActivity.this, "cancel or failed!!!", Toast.LENGTH_SHORT).show();
//                    }
//                }).send();
//        findViewById(R.id.loginWithButton).callOnClick();
//        new LoginWithHuma(this)
//                .setClientKey("46d133bc2abeea10afa8dfcd887e74d4")
//                .setOnLoginListener(new LoginWithHuma.OnLoginListener() {
//                    @Override
//                    public void onLogin(String code) {
//                        Toast.makeText(MainActivity.this, "login!!!222", Toast.LENGTH_SHORT).show();
//
//                        // send code to your server here!
//                    }
//
//                    @Override
//                    public void onFail(String message) {
//                        Toast.makeText(MainActivity.this, "cancel or failed!!!", Toast.LENGTH_SHORT).show();
//                    }
//                }).send();
    }
}
