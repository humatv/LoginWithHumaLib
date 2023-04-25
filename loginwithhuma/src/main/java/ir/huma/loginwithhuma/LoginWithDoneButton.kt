package ir.huma.loginwithhuma

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatButton
import ir.huma.loginwithhuma.LoginWithDone.OnLoginListener
open class LoginWithDoneButton : AppCompatButton,View.OnClickListener {
    var loginWithDone: LoginWithDone? = null
    private var myOnClickListener: OnClickListener? = null


    constructor(context: Context?) : super(
        ContextThemeWrapper(context, R.style.DoneButton),
        null,
        R.style.DoneButton
    ) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        ContextThemeWrapper(
            context,
            R.style.DoneButton
        ), attrs, R.style.DoneButton
    ) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        ContextThemeWrapper(context, R.style.DoneButton), attrs, R.style.DoneButton
    ) {
        init(attrs)
    }



    protected fun init(attrs: AttributeSet?) {
        typeface = Typeface.createFromAsset(context.assets, "PeydaFaNum-Regular.ttf")
        loginWithDone = LoginWithDone(context)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.LoginWithHumaButton, 0, 0
            )
            val clientKey = a.getString(R.styleable.LoginWithHumaButton_clientKey)
            if (clientKey != null) {
                loginWithDone!!.setClientKey(clientKey)
            }
            a.recycle()
        }
    }

    fun setClientKey(clientKey: String?): LoginWithDoneButton {
        loginWithDone!!.setClientKey(clientKey)
        return this
    }


    fun setOnLoginListener(onLoginListener: OnLoginListener?): LoginWithDoneButton {
        loginWithDone!!.setOnLoginListener(onLoginListener)
        super.setOnClickListener(this)
        return this
    }

    override fun setOnClickListener(l: OnClickListener?) {
        myOnClickListener = l
    }

    override fun onClick(view: View) {
        loginWithDone!!.send()
        if (myOnClickListener != null) myOnClickListener!!.onClick(view)
    }

    override fun onDetachedFromWindow() {
        try {
            loginWithDone!!.unregiter()
        } catch (e: Exception) {
        }
        super.onDetachedFromWindow()
    }
}