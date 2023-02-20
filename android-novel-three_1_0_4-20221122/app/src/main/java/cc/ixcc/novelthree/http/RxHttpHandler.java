package cc.ixcc.novelthree.http;

public interface RxHttpHandler {
    void onStart();
    void onSuccess(String s);
    void onError(int code);
    void onFinish();
}