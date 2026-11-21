package tech.done.user;

import tech.done.user.ILoginCallback;

interface ILoginWithDoneService {

    void register(ILoginCallback callback);
    void unregister(ILoginCallback callback);
    void login(String clientId);
}