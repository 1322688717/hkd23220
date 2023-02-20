package cc.ixcc.novelthree.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.model.Response;
import com.zhy.http.okhttp.utils.L;

import cc.ixcc.novelthree.bean.Read;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.Data;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.ui.dialog.AccountDialog;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 阅读喜好
 */

public class ReadPreferencesActivity extends MyActivity {
    @BindView(R.id.def_boy)
    ImageView defBoy;
    @BindView(R.id.def_girl)
    ImageView defGirl;
    List<Read.HobbyBean> mlist;
    @BindView(R.id.Next_but)
    Button Nextbut;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read_preferences;
    }

    @Override
    protected void initView() {
        mlist = new ArrayList<>();
        getInfo();
    }

    @Override
    protected void initData() {
    }

    private void getInfo() {
        HttpClient.getInstance().post(AllApi.hobby, AllApi.hobby)
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    ToastUtils.show(msg);
                    Read bean = new Gson().fromJson(info[0], Read.class);
                    mlist = bean.getHobby();
                    defBoy.setImageResource(bean.getIssex().equals(Constants.boy + "") ? R.mipmap.boy_selected : R.mipmap.boy_def);
                    defGirl.setImageResource(bean.getIssex().equals(Constants.boy + "") ? R.mipmap.girl_def : R.mipmap.girl_selected);
                }

                @Override
                public void onError(Response<Data> response) {
                    Throwable t = response.getException();
                    L.e("网络请求错误---->" + t.getClass() + " : " + t.getMessage());
                    if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException) {
                        ToastUtils.show("Network request failed");
                    }

                    super.onError(response);
                    hideDialog();
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.def_boy, R.id.def_girl,R.id.Next_but})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.def_boy:
                defBoy.setImageResource(R.mipmap.boy_selected);
                defGirl.setImageResource(R.mipmap.girl_def);
                Nextbut.setVisibility(View.GONE);
                showMyDialog("1");
                break;
            case R.id.def_girl:
                defBoy.setImageResource(R.mipmap.boy_def);
                defGirl.setImageResource(R.mipmap.girl_selected);
                Nextbut.setVisibility(View.GONE);
                showMyDialog("2");
                break;
            case R.id.Next_but:
                showMyDialog("2");
                break;
        }
    }

    private void showMyDialog(String issex) {
        //获取实体类
        final AccountDialog mAccountDialog = AccountDialog.getMyDialog(ReadPreferencesActivity.this, mlist);
        //回调实现点击
        mAccountDialog.setSettingDialogCallBack(new AccountDialog.SettingDialogCallBack() {
            @Override
            public void onActionClick(String id) {
                HttpClient.getInstance().post(AllApi.savehobby, AllApi.savehobby)
                    .params("issex", issex)
                    .params("cids", id)
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) { toast(msg);
                        mAccountDialog.dismiss();
                        finish();
                        //HomeActivity.onShowComplete(HomeActivity.FirstShowType.ReadPreferencesActivity);
                        }
                    });
            }
        });
        mAccountDialog.show();
    }
}
