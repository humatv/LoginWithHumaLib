package ir.huma.loginwithhuma;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.view.View;

public class LoginWithHumaButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener {
    LoginWithHuma loginWithHuma;

    public LoginWithHumaButton(Context context) {
        super(new ContextThemeWrapper(context, R.style.HumaButton), null, R.style.HumaButton);
        init(null);
    }

    public LoginWithHumaButton(Context context, AttributeSet attrs) {
        super(new ContextThemeWrapper(context, R.style.HumaButton), attrs, R.style.HumaButton);
        init(attrs);
    }

    public LoginWithHumaButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(new ContextThemeWrapper(context, R.style.HumaButton), attrs, R.style.HumaButton);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "IRANYekanMobileRegular.ttf"));
        loginWithHuma = new LoginWithHuma(getContext());
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.LoginWithHumaButton, 0, 0);
            String clientKey = a.getString(R.styleable.LoginWithHumaButton_clientKey);
            if (clientKey != null) {
                loginWithHuma.setClientKey(clientKey);
            }
            a.recycle();
        }
    }

    public LoginWithHumaButton setClientKey(String clientKey) {
        loginWithHuma.setClientKey(clientKey);
        return this;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        throw new RuntimeException("You can't set onClickListener! please call setOnLoginListener!");
    }

    public LoginWithHumaButton setOnLoginListener(LoginWithHuma.OnLoginListener onLoginListener) {
        loginWithHuma.setOnLoginListener(onLoginListener);
        super.setOnClickListener(this);
        return this;
    }

    @Override
    public void onClick(View view) {
        loginWithHuma.send();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            loginWithHuma.unregiter();
        } catch (Exception e) {

        }
    }
}
