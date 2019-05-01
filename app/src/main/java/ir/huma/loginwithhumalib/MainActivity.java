package ir.huma.loginwithhumalib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ir.huma.loginwithhuma.LoginWithHuma;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginClick(View view) {
        new LoginWithHuma(this)
                .setClientKey("<client token>")
                .setOnLoginListener(new LoginWithHuma.OnLoginListener() {
                    @Override
                    public void onLogin(String code) {
                        // send code to your server here!
                    }

                    @Override
                    public void onFail(String message) {
                    }
                }).send();

    }
}
