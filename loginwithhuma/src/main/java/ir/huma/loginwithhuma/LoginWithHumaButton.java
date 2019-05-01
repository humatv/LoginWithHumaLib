package ir.huma.loginwithhuma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginWithHumaButton extends android.support.v7.widget.AppCompatButton implements View.OnClickListener {


    public LoginWithHumaButton(Context context) {
        super(context);
    }

    public LoginWithHumaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginWithHumaButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        throw new RuntimeException("You cam't set onClickListener! please setOnLoginListener!");
    }

//    public LoginWithHumaButton setOnLoginListener(OnLoginListener onLoginListener) {
//        this.onLoginListener = onLoginListener;
//        super.setOnClickListener(this);
//        return this;
//    }

    @Override
    public void onClick(View view) {

    }



}
