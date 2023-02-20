package cc.ixcc.novelthree.statistics;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.LogLevel;

public class AdjustUtil {

    private static AdjustUtil instance;

    public static AdjustUtil GetInstance() {
        if (instance == null) {
            instance = new AdjustUtil();
        }
        return instance;
    }

    public Application application;

    public void InitAdjust(Application application) {
        this.application = application;

        String appToken = "fruck889adxc";
        //注意注意！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        //设置当前开发，环境测试使用：AdjustConfig.ENVIRONMENT_SANDBOX; 正式环境使用：AdjustConfig.ENVIRONMENT_PRODUCTION;
        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;
        AdjustConfig config = new AdjustConfig(application, appToken, environment);
        //设置日志级别，调试时打开日志使用“LogLevel.VERBOSE”，上线时关闭日志，使用“LogLevel.SUPRESS”
        config.setLogLevel(LogLevel.SUPRESS);

        //出传音渠道包打开
        //出谷歌包时需要注释 注意！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        //config.setDefaultTracker("bvjh5af");

        Adjust.onCreate(config);

        application.registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

    }

    //推送标签 用户获取用户卸载重装事件
    public void setPushToken(String pushNotificationsToken)
    {
        // Send the token with context (recommended)
        Adjust.setPushToken(pushNotificationsToken, this.application);

    }

    // Sum：金额  CURRENCY：货币类型（默认美元 USD）
    public void SendPayEvent(float Sum, String CURRENCY, String OrderId) {
        AdjustEvent adjustEvent = new AdjustEvent("8487db");
        adjustEvent.setRevenue(Sum, CURRENCY);
        adjustEvent.setOrderId(OrderId);
        Adjust.trackEvent(adjustEvent);
    }

    //app崩溃事件
    public void Sendcrash() {
        AdjustEvent adjustEvent = new AdjustEvent("e0huew");
        Adjust.trackEvent(adjustEvent);
    }

    // banner广告触发事件  Pos: 1弹框运营位 2书架运营位 3福利中心运营位 4书籍详情页运营位 5支付页运营位 6主页运营位
    public void SendBannerEvent(int Pos) {
        String eventToken = "";
        switch (Pos) {
            case 1:
                eventToken = "qgnyci";
                break;
            case 2:
                eventToken = "vs9yw7";
                break;
            case 3:
                eventToken = "hw1fxh";
                break;
            case 4:
                eventToken = "wnolq7";
                break;
            case 5:
                eventToken = "hdf8c1";
                break;
            case 6:
                eventToken = "226ep4";
                break;
            default:
                break;
        }

        AdjustEvent adjustEvent = new AdjustEvent(eventToken);
        Adjust.trackEvent(adjustEvent);
    }

    private static final class AdjustLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    }

}


