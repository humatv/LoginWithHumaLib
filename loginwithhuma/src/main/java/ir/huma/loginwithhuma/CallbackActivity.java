package ir.huma.loginwithhuma;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class CallbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        try {
            Toast.makeText(this, getIntent().getData().toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
        finish();
    }
}
