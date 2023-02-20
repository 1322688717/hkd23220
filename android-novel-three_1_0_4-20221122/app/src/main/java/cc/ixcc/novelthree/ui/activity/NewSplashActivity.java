package cc.ixcc.novelthree.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;

import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.bean.UserInfoBean;
import cc.ixcc.novelthree.http.Data;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.treader.util.AppUtils;
import cc.ixcc.novelthree.ui.activity.my.WebViewActivity;
import com.jiusen.base.BaseDialog;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.bean.ConfigBean;
import cc.ixcc.novelthree.bean.StackRoomChannelBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.ui.dialog.MessageDialog;
import cc.ixcc.novelthree.ui.view.CountDownView;
import cc.ixcc.novelthree.utils.RouteUtil;
import cc.ixcc.novelthree.utils.SpUtil;

import com.lzy.okgo.model.Response;
import com.tencent.mmkv.MMKV;
import cc.ixcc.novelthree.R;

import com.tenjin.android.AttributionInfoCallback;
import com.tenjin.android.TenjinSDK;
import com.tenjin.android.config.TenjinConsts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

@Route(path = RouteUtil.PATH_LAUNCHER)
public class NewSplashActivity extends MyActivity {
    LinearLayout logo;
    FrameLayout adContainer;
    Timer timer;
    TextView countdown;
    int nowCount = 5;
    boolean isReceiveCallback = false;
    boolean isOvertime = false;
    private MMKV mmkv = MMKV.defaultMMKV();
    public static String globleMEIDStr = "";
    public static NewSplashActivity self = null;
    private TenjinSDK instance;
//    private CountDownView cdv;

    //AdvanceAD ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TenjinSDK instance = TenjinSDK.getInstance(this, "HKZXUSKGYMSF3PRHSVJYZYJNGE5WK3ZF");
//        instance.connect();
        self = this;
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_splash_new);
        adContainer = findViewById(R.id.splash_container);
        logo = findViewById(R.id.logo_container);
        countdown = findViewById(R.id.skip_view);
        ButterKnife.bind(this);
//         cdv = (CountDownView) findViewById(R.id.tv_red_skip);
//        cdv.setCountdownTime(10);
//        cdv.setAddCountDownListener(new CountDownView.OnCountDownFinishListener() {
//            @Override
//            public void countDownFinished() {
//                //时间完了 干的事情
//                Log.e("Tenjinfacebook", "倒计时");
//                AppContext.sInstance.setTenjinFlag(0);
//                getAppInfo();
//            }
//
//        });
//        cdv.startCountDown();
    }

    public void toAutoLogin() {
        self.mmkv.encode(SpUtil.Auto_Login, true);
        self.autoLogin("gcode");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash_new;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        //goToMainActivity();
        //getAppInfo();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                --nowCount;
                post(new Runnable() {
                    @Override
                    public void run() {
                        countdown.setText(nowCount + "s");
                    }
                });
                if (nowCount == 0 && !isReceiveCallback) {
                    timer.cancel();
                    isOvertime = true;
                    getAppInfo();
                }
            }
        }, 1000, 1000);
    }

    private void getAppInfo() {
        HttpClient.getInstance().post(AllApi.appinit, AllApi.appinit)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        ConfigBean config = new Gson().fromJson(info[0], ConfigBean.class);
                        Constants.getInstance().setConfig(config);

                        //隐私政策
                        boolean bIsAgree = mmkv.decodeBool(Constants.isAgree, false);
                        mmkv.encode(SpUtil.CONFIG, config);

                        if (bIsAgree) {
                            preloading();
                            //请求权限
                            //requestPermission(self);
                            toAutoLogin();
                        } else {
                            showFirstDialog(config);
                        }
                    }
                });
    }

    /**
     * 自动登录
     */
    public static void autoLogin(String gcode) {
        self.globleMEIDStr = MMKV.defaultMMKV().decodeString(SpUtil.MEID, "");
        if (self.globleMEIDStr.equals("")) {
            self.globleMEIDStr = AppUtils.getDeviceId(self);
        }
        HttpClient.getInstance().post(AllApi.autologin, AllApi.autologin)
                .params("meid", self.globleMEIDStr)
                .params("machine", AppUtils.getDeviceModelName())
                .params("channel", AppContext.sChannel)
                .params("source", "android")
                .params("gcode", gcode)
                .params("mch_type", (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) ? 0 : 1)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        self.getUserInfo(info[0]);
                        self.mmkv.encode(SpUtil.MEID, self.globleMEIDStr);
                    }
                });
    }

    private void getUserInfo(String json) {
        UserInfoBean bean = new Gson().fromJson(json, UserInfoBean.class);
        self.mmkv.encode(SpUtil.TOKEN, bean.getToken_type() + bean.getAccess_token());
        self.mmkv.encode(SpUtil.UID, String.valueOf(bean.getId()));
        instance.getAttributionInfo(new AttributionInfoCallback() {
            @Override
            public void onSuccess(Map<String, String> map) {
                //facebook    google    tiktok
//                cdv.endCountDown();
                Log.e("Tenjinfacebook", map.toString());
                if (map.containsKey(TenjinConsts.ATTR_PARAM_AD_NETWORK)) {
                    if (map.get(TenjinConsts.ATTR_PARAM_AD_NETWORK).equals("organic")) {
                        //appContext.setTenjinFlag(0);   //非购买用户
                        setHttpCopyright(bean.getId(), 0);//非购买用户
                    } else {
                        //appContext.setTenjinFlag(-1);  //购买用户
                        setHttpCopyright(bean.getId(), -1);//
                    }
                }
            }
        });
        HttpClient.getInstance().get(AllApi.userinfo, AllApi.userinfo)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        UserBean bean = new Gson().fromJson(info[0], UserBean.class);
                        self.mmkv.encode(SpUtil.USER_INFO, bean);
                        AppContext.Emailmsg = bean.getEmail();
                    }
                });
    }

    private void setHttpCopyright(int userId, int copyright) {
        Log.e("Tenjinfacebook", String.valueOf(userId));
        HttpClient.getInstance().post(AllApi.setcopyright, AllApi.setcopyright)
                .params("user_id", userId)
                .params("copyright", -1)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
//                        Log.e("Tenjinfacebook", "copyright设置后台成功");
                    }

                    @Override
                    public void onError(Response<Data> response) {
                        super.onError(response);
                    }
                });
    }

    private void showFirstDialog(ConfigBean config) {
        SpannableString spanString = new SpannableString("   Your privacy is important to us. Before using DoveNovel app,you need to agree to our\n　《Terms of Service》and《Privacy policy》 Please read the details in this privacy statement to understand how and where Dove Novel handles personal data.\n   Our app will uploading users' Images information.");
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //用户协议
                WebViewActivity.forward(getContext(), config.getUser_agreement(), "Terms of Service", false);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getColor(R.color.colorButtonDefault)); //设置颜色
            }
        }, 90, 106, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //隐私政策
                WebViewActivity.forward(getContext(), config.getUser_terms(), "Privacy policy", false);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getColor(R.color.colorButtonDefault)); //设置颜色
            }
        }, 111, 125, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        BaseDialog baseDialog = new MessageDialog.Builder(NewSplashActivity.this)
                .setMessage(spanString)
                .setSpan(true)
                .setCanceledOnTouchOutside(false)
                .setListener(new MessageDialog.OnListener() {

                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        dialog.dismiss();
                        mmkv.encode(Constants.isAgree, true);
                        preloading();

                        //请求权限
                        //requestPermission(self);
                        toAutoLogin();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void preloading() {
        HttpClient.getInstance().get(AllApi.bookchannel, AllApi.bookchannel)
                .execute(new HttpCallback() {

                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        try {
                            HashSet<String> infoSet = new HashSet<>();
                            infoSet.addAll(Arrays.asList(info));
                            mmkv.encode(SpUtil.CHANNEL, infoSet);
                            StackRoomChannelBean recommendBean = new Gson().fromJson(info[0], StackRoomChannelBean.class);
                            getRecommend(recommendBean);
                        } catch (Exception e) {
                            ShowSplashAd();
                        }
                    }
                });
    }

    private void getRecommend(StackRoomChannelBean recommendBean) {
        HttpClient.getInstance().get(AllApi.stackroombook, AllApi.stackroombook)
                .params("channel_id", recommendBean.getId())
                .params("page", 1)
                .params("page_size", 15)
                .params("copyright", AppContext.sInstance.getTenjinFlag())
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        mmkv.encode(SpUtil.RECOMMEND, info[0]);
                        //ShowSplashAd();
                        goToMainActivity();
                    }
                });
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = TenjinSDK.getInstance(this, "HKZXUSKGYMSF3PRHSVJYZYJNGE5WK3ZF");
        instance.connect();
        Log.e("Tenjinfacebook", "tenjin连接");
    }

    //加载插屏广告
    private void ShowSplashAd() {
        /**
         * 加载并展示开屏广告
         */
//        ad = new AdvanceAD(this);
//        //建议skipView传入null，代表使用SDK内部默认跳过按钮。如果需要自定义跳过按钮，skipView传入自定义跳过布局即可，注意：部分渠道不支持自定义，即使传了也不会生效。
//        ad.loadSplash(adContainer, logo, null, new AdvanceAD.SplashCallBack() {
//            @Override
//            public void jumpMain() {
//                if(isHotStart)
//                {
//                    self.finish();
//                }
//                else
//                {
//                    NewSplashActivity.isHotStart = true;
//                    goToMainActivity();
//                }
//            }
//        });
    }
    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isReceiveCallback = true;
        if (!isOvertime) {
            timer.cancel();
        }
    }
}