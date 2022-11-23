package ir.huma.loginwithhuma

data class TemporaryCodeResponse(
    val isSuccess: Boolean,
    val errorMessage: String? = null,
    val temporaryCode: String? = null
)