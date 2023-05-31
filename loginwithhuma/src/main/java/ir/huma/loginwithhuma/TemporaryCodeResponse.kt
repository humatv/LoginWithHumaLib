package ir.huma.loginwithhuma

import androidx.annotation.Keep


data class TemporaryCodeResponse(
    val isSuccess: Boolean,
    val errorMessage: String? = null,
    val temporaryCode: String? = null,
    val status : ResponseStatus
)