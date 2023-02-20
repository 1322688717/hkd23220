package cc.ixcc.novelthree.http;

import com.alibaba.fastjson.JSON;

public class RxJsonHttpHandler<T extends BaseResponseBean> implements RxHttpHandler {

    private Class<T> clazz;

    public RxJsonHttpHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(String s) {
        T t = JSON.parseObject(s, clazz);
        if (t != null) {
            onSuccess(t);
        } else {
            onError(ExceptionCode.CODE_DATA_FORMAT);
        }
    }

    public void onSuccess(T t) {

    }

    @Override
    public void onError(int code) {

    }

    @Override
    public void onFinish() {

    }

}