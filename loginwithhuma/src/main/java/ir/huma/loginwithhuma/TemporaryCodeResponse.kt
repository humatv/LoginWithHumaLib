package ir.huma.loginwithhuma

data class TemporaryCodeResponse(
    val isSuccess: Boolean,
    val errorMessage: String? = null,
    val temporaryCode: String? = null,
    val status : ResponseStatus
){
    enum class ResponseStatus{
        Success,
        InternetError,
        ServerError,
        UserCancel,
        NeedWizard,
        UnknownPackage,
        UnknownError
    }
}