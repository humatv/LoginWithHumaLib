package ir.huma.loginwithhuma

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import ir.huma.loginwithhuma.LoginWithHuma.OnLoginListener

class LoginWithHumaButton : AppCompatButton,View.OnClickListener {
    var loginWithHuma: LoginWithHuma? = null
    private var myOnClickListener: OnClickListener? = null


    constructor(context: Context?) : super(
        ContextThemeWrapper(context, R.style.HumaButton),
        null,
        R.style.HumaButton
    ) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        ContextThemeWrapper(
            context,
            R.style.HumaButton
        ), attrs, R.style.HumaButton
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        ContextThemeWrapper(context, R.style.HumaButton), attrs, R.style.HumaButton
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        typeface = Typeface.createFromAsset(context.assets, "IRANYekanMobileRegular.ttf")
        loginWithHuma = LoginWithHuma(context)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.LoginWithHumaButton, 0, 0
            )
            val clientKey = a.getString(R.styleable.LoginWithHumaButton_clientKey)
            if (clientKey != null) {
                loginWithHuma!!.setClientKey(clientKey)
            }
            a.recycle()
        }
    }

    fun setClientKey(clientKey: String?): LoginWithHumaButton {
        loginWithHuma!!.setClientKey(clientKey)
        return this
    }


    fun setOnLoginListener(onLoginListener: OnLoginListener?): LoginWithHumaButton {
        loginWithHuma!!.setOnLoginListener(onLoginListener)
        super.setOnClickListener(this)
        return this
    }

    override fun setOnClickListener(l: OnClickListener?) {
        myOnClickListener = l
    }

    override fun onClick(view: View) {
        loginWithHuma!!.send()
        if (myOnClickListener != null) myOnClickListener!!.onClick(view)
    }

    override fun onDetachedFromWindow() {
        try {
            loginWithHuma!!.unregiter()
        } catch (e: Exception) {
        }
        super.onDetachedFromWindow()
    }
}