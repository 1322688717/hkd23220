package cc.ixcc.novelthree.ad;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;

public class AdMobManager {

    private static AdMobManager instance;

    public OnUserEarnedRewardListener onUserEarnedRewardListener;

    public static AdMobManager GetInstance() {
        if (instance == null) {
            instance = new AdMobManager();
        }
        return instance;
    }

    private static final String TAG = "AdMobManager_Log";

    public void LoadAndShowBanner(Activity activity, AdView mAdView) {
        AdView adView = new AdView(activity);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "onAdLoaded: ");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d(TAG, "onAdFailedToLoad: " + adError.toString());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(TAG, "onAdOpened: ");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG, "onAdClicked: ");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.

                Log.d(TAG, "onAdClosed: ");
            }
        });
    }


    RewardedAd rewardedAd;

    public void loadRewardedAd(Context activity) {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
            //正式：ca-app-pub-9498778508325437/1867364217 测试：ca-app-pub-3940256099942544/5224354917
            rewardedAd = new RewardedAd(activity, "ca-app-pub-9498778508325437/1867364217");
            rewardedAd.loadAd(
                    new AdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onRewardedAdLoaded() {
                            // Ad successfully loaded.
                            Log.d(TAG, "onRewardedAdLoaded: ");
                        }

                        @Override
                        public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                            // Ad failed to load.
                            Log.d(TAG, "onRewardedAdFailedToLoad: " + loadAdError.toString());
                        }
                    });
        }
    }

    public void showRewardedVideo(Activity activity) {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback =
                    new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                            Log.d(TAG, "onRewardedAdOpened: ");
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                            Log.d(TAG, "onRewardedAdClosed: ");
                            // Preload the next video ad.
                            loadRewardedAd(activity);
                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {
                            // User earned reward.
                            Log.d(TAG, "onUserEarnedReward: " + rewardItem.toString());
                            if (onUserEarnedRewardListener!=null){
                                onUserEarnedRewardListener.onUserEarnedReward();
                            }
                        }

                        @Override
                        public void onRewardedAdFailedToShow(AdError adError) {
                            // Ad failed to display
                            Log.d(TAG, "onRewardedAdFailedToShow: " + adError.toString());
                        }
                    };
            rewardedAd.show(activity, adCallback);
        }
    }

    //                    获得金币   1为章节界面内 2为充值界面内
    public void GetCoin(String coin,int ad) {
        HttpClient.getInstance().post(AllApi.sign, AllApi.sign)
                .isMultipart(true)
                .params("coin", coin)
                .params("ad", ad)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info)
                    {
                        Log.d(TAG, "onSuccess: 观看广告获得金币:"+coin);
                    }
                });
    }

    public void SetOnUserEarnedRewardListener(OnUserEarnedRewardListener onUserEarnedRewardListener){
        this.onUserEarnedRewardListener=onUserEarnedRewardListener;
    }

    public interface OnUserEarnedRewardListener
    {
        void onUserEarnedReward();
    }

}
