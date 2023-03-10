package cc.ixcc.novelthree.treader;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import cc.ixcc.novelthree.BuildConfig;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hjq.bar.TitleBar;
import com.hjq.bar.style.TitleBarLightStyle;
import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;

import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.ad.AdMobManager;
import cc.ixcc.novelthree.ad.AppOpenManager;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.helper.ActivityStackManager;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.statistics.AdjustUtil;
import cc.ixcc.novelthree.statistics.Firebase;
import cc.ixcc.novelthree.statistics.GooglePlayInstallReferrer;
import cc.ixcc.novelthree.treader.util.PageFactory;
import cc.ixcc.novelthree.ui.activity.CrashActivity;
import cc.ixcc.novelthree.ui.activity.HomeActivity;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mmkv.MMKV;

import org.litepal.LitePalApplication;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import cc.ixcc.novelthree.utils.SpUtil;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class AppContext extends MultiDexApplication {
    public static AppContext sInstance;
    public static volatile Context applicationContext;
    public static String sChannel = "noneGet";

    public static String Emailmsg;

    private static AppOpenManager appOpenManager;
    public int tenjinFlag = -1;

    public int getTenjinFlag() {
        return tenjinFlag;
    }

    public void setTenjinFlag(int tenjinFlag) {
        this.tenjinFlag = tenjinFlag;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        applicationContext = getApplicationContext();

        try {
            //?????????Adjust ??????????????????
//            AdjustUtil.GetInstance().InitAdjust(this);
            //???????????????????????????
            GooglePlayInstallReferrer.GetInstance().GooglePlayInstallReferrerInit(this);
            //?????????????????????
            Firebase.GetInstance().InitFirebase();

            //????????? ????????????SDK
            MobileAds.initialize(
                    this,
                    new OnInitializationCompleteListener() {
                        @Override
                        public void onInitializationComplete(InitializationStatus initializationStatus) {

                        }
                    });

            //?????? ??????????????????
            //appOpenManager = new AppOpenManager(this);

            AdMobManager.GetInstance().loadRewardedAd(this);
        } catch (Exception e) {
            Log.d("MineFragment", "MineFragment_error: " + e);
        }

        //?????????MMKV
        String rootDir = MMKV.initialize(this);
        System.out.println("mmkv root: " + rootDir);

        LitePalApplication.initialize(this);
        Config.createConfig(this);
        PageFactory.createPageFactory(this);
        //?????????Http
        HttpClient.getInstance().init();

        //????????? ARouter
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
        initSDK(this);

        // ??????60?????????
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    UserBean userBean1 = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                    if (userBean1 == null && userBean1.getId() == 0) {
                        return;
                    }
                    HttpClient.getInstance().post(AllApi.cdk, AllApi.cdk)
                            .params("userid", userBean1.getId())
                            .execute(new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {
                                    Log.d("TAG", "???????????????????????????????????????");
                                }
                            });

                } catch (Exception e) {
                    Log.d("MineFragment", "MineFragment_error: " + e);
                }
            }
        }, 60000);

        SendOnLine();
    }

    public void SendOnLine() {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    UserBean userBean1 = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                    if (userBean1 == null && userBean1.getId() == 0) {
                        return;
                    }
                    //??????????????????
                    HttpClient.getInstance().post(AllApi.addonlinesecs, AllApi.addonlinesecs)
                            .params("user_id", userBean1.getId())
                            .params("secs", 60)
                            .execute(new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {
                                    Log.d("TAG", "???????????????????????????");
                                }
                            });

                    SendOnLine();
                } catch (Exception e) {
                    Log.d("MineFragment", "MineFragment_error: " + e);
                }
            }
        }, 60000);
    }

    /**
     * ??????????????????????????????
     */
    public void initSDK(Application application) {
        // ???????????????
        ToastUtils.init(application);

        // ?????? Toast ?????????
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "??? Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });

        // ?????????????????????
        TitleBar.initStyle(new TitleBarLightStyle(application) {
            @Override
            public Drawable getBackground() {
                return new ColorDrawable(getColor(R.color.colorPrimary));
            }

            @Override
            public Drawable getBackIcon() {
                return getDrawable(R.drawable.ic_back_black);
            }
        });

        // Crash ????????????
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
                .enabled(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                // ????????? Activity
                .restartActivity(HomeActivity.class)
                // ????????? Activity
                .errorActivity(CrashActivity.class)
                .apply();

        // ??????????????? Header ?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setEnableLastTime(true));
        // ??????????????? Footer ?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(20));

        // Activity ??????????????????
        ActivityStackManager.getInstance().init(application);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
