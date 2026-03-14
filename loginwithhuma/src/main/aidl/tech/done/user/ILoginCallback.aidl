package tech.done.user;

oneway interface ILoginCallback {

    void onSuccess(String code);
    void onFailure(String message);
}