package cc.ixcc.novelthree.ad;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private static final String LOG_TAG = "AppOpenManager";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private final Application myApplication;

    private Activity currentActivity;

    private static boolean isShowingAd = false;
    private long loadTime = 0;

    /**
     * Constructor
     * 构造函数
     */
    public AppOpenManager(Application myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }


    /**
     * Request an ad
     */
    public void fetchAd() {
        // We will implement this below.

        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAppOpenAdLoaded(AppOpenAd appOpenAd) {

                        AppOpenManager.this.appOpenAd = appOpenAd;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
                        Log.d(LOG_TAG, "onAdLoaded: ");
                    }

                    @Override
                    public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {

                        // Handle the error.
                        Log.d(LOG_TAG, "onAppOpenAdLoaded: 加载广告失败，，" + loadAdError.toString());

                    }


                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);

    }

    /**
     * 为确保您不会展示过期的广告，请在 AppOpenManager 中添加一个方法，用于检查广告引用加载后经过了多长时间。然后，使用该方法检查广告是否仍然有效
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Creates and returns ad request.
     * 创建并返回广告请求
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     * 检查ad是否存在并显示的实用方法
     */
    public boolean isAdAvailable() {

        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * LifecycleObserver methods
     * 监听前台事件 调用广告。
     */
    @OnLifecycleEvent(ON_START)
    public void onStart()
    {
        showAdIfAvailable();
    }

    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        //只显示广告如果没有一个应用程序打开广告当前显示
        //有一个广告。
        Log.d(LOG_TAG, "isShowingAd==" + isShowingAd);
        Log.d(LOG_TAG, "isAdAvailable==" + isAdAvailable());
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();

                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent: ");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {

                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: ");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;

                            Log.d(LOG_TAG, "onAdShowedFullScreenContent: ");
                        }
                    };
            Log.d(LOG_TAG, "activity_name" + currentActivity.getClass().getName());

            appOpenAd.show(currentActivity,fullScreenContentCallback);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity)
    {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity)
    {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }


}
