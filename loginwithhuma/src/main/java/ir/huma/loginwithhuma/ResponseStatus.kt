package ir.huma.loginwithhuma

enum class ResponseStatus {
    Success,
    InternetError,
    ServerError,
    UserCancel,
    NeedWizard,
    UnknownPackage,
    UnknownError
}