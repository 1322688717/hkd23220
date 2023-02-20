package cc.ixcc.novelthree.http;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hjq.toast.ToastUtils;

import cc.ixcc.novelthree.utils.RouteUtil;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.zhy.http.okhttp.utils.L;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

/**
 * Created by cxf on 2017/8/7.
 */

public abstract class HttpCallback extends AbsCallback<Data> {

    private Dialog mLoadingDialog;

    @Override
    public Data convertResponse(okhttp3.Response response) throws Throwable {
        return JSON.parseObject(response.body().string(), Data.class);
    }

    @Override
    public void onSuccess(Response<Data> response) {
        Data data = response.body();

        if (data == null) {
            return;
        }

        try {
            if (200 == data.getCode()) {
                onSuccess(data.getCode(), data.getMessage(), data.getData());
            } else if (301 == data.getCode()) {
                Log.e("网络请求", data.getMessage());
                onSuccess(data.getCode(), data.getMessage(), data.getData());
            } else if (401 == data.getCode()) {
                Log.e("网络请求", data.getMessage());
                RouteUtil.forwardLoginInvalid("");
            } else {
                Log.e("网络请求", data.getMessage());
                if (!TextUtils.equals(data.getMessage(), "operation failed")) {
                    //参数错误
                    ToastUtils.show(data.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("HttpCallback", e.getMessage());
        }
    }

    @Override
    public void onError(Response<Data> response) {
        Throwable t = response.getException();
        L.e("Network request error---->" + t.getClass() + " : " + t.getMessage());
        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException) {
            ToastUtils.show("Network request failed");
        }
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        onError();
    }

    public void onError() {

    }


    public abstract void onSuccess(int code, String msg, String[] info);

    @Override
    public void onStart(Request<Data, ? extends Request> request) {
        onStart();
    }

    public void onStart() {
        if (showLoadingDialog()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = createLoadingDialog();
            }
            try {
                mLoadingDialog.show();
            } catch (Exception e) {
                Log.e("HttpCallback", e.getMessage());
            }
        }
    }

    @Override
    public void onFinish() {
//        if (showLoadingDialog() && mLoadingDialog != null) {
//            mLoadingDialog.dismiss();
//        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public Dialog createLoadingDialog() {
        return null;
    }

    public boolean showLoadingDialog() {
        return false;
    }

}
