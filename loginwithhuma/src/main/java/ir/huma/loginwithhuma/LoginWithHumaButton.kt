package ir.huma.loginwithhuma

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper

@Deprecated("please use LoginWithDoneButton class instead")
class LoginWithHumaButton:LoginWithDoneButton {
    constructor(context: Context?) : super(
        ContextThemeWrapper(context, R.style.DoneButton),
        null,
        R.style.DoneButton
    ) {
        super.init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        ContextThemeWrapper(
            context,
            R.style.DoneButton
        ), attrs, R.style.DoneButton
    ) {
        super.init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        ContextThemeWrapper(context, R.style.DoneButton), attrs, R.style.DoneButton
    ) {
        super.init(attrs)
    }


}