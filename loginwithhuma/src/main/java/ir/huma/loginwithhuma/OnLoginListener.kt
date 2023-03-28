package ir.huma.loginwithhuma

interface OnLoginListener {
    fun onLogin(code: String?)
    fun onFail(message: String?, status: TemporaryCodeResponse.ResponseStatus?)
}