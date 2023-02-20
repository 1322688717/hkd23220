package cc.ixcc.novelthree.pay.ali;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;
import com.tenjin.android.TenjinSDK;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import cc.ixcc.novelthree.bean.PayBackBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.http.MainHttpUtil;
import cc.ixcc.novelthree.statistics.AdjustUtil;
import cc.ixcc.novelthree.utils.SpUtil;

/**
 * desc   : 谷歌支付
 */
public class GooglePay implements PurchasesUpdatedListener {
    public BillingClient billingClient;

    private static Activity sParentActivity;

    private String TAG = "GooglePayLog";
    private SkuDetails skuDetail;
    private boolean isConnect;
    private static int sChargeID;
    private static String sOrderId;
    private static Activity sActivity;
    private static GooglePay instance;
    private int mAddDay;
    private SNData snBean;

    public class SNData {
        private String sn;

        public String getSN() {
            return sn;
        }

        public void setSN(String sn) {
            this.sn = sn;
        }
    }

    //当前支付类型
    private PAY_FOR_TYPE mCurThirdType = PAY_FOR_TYPE.GOLD;

    public enum PAY_FOR_TYPE {
        GOLD,
        VIP
    }

    public class GoodMsg {
        public String Sum;
        public String CONTENT_TYPE;
        public String CONTENT_ID;
        public String price_currency_code;

        public GoodMsg(String Sum, String CONTENT_TYPE, String CONTENT_ID, String price_currency_code) {

            this.Sum = Sum;
            this.CONTENT_TYPE = CONTENT_TYPE;
            this.CONTENT_ID = CONTENT_ID;
            this.price_currency_code = price_currency_code;
        }
    }

    private GoodMsg goodMsg;

    public static GooglePay GetInstance() {
        if (instance == null) {
            instance = new GooglePay();
        }
        return instance;
    }

    public void Init(Activity activity) {
        sActivity = activity;

        billingClient = BillingClient.newBuilder(sActivity).setListener(this).enablePendingPurchases().build();
        Log.i(TAG, "GooglePayInit:" + billingClient.isReady());
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The billing client is ready. You can query purchases here.
                    isConnect = true;
                    Log.e(TAG, "谷歌支付链接成功");
                } else {
                    Log.e(TAG, billingResult.getResponseCode() + "");
                    isConnect = false;
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                isConnect = false;
                Log.e(TAG, "onBillingServiceDisconnected");
            }
        });
    }

    public void PayForCoin(Activity activity, String goodsId, int chareID, String Sum) {
        sParentActivity = activity;
        mCurThirdType = PAY_FOR_TYPE.GOLD;
        sChargeID = chareID;

        Log.e(TAG, "Pay:" + goodsId);
        List<String> skuList = new ArrayList<>();
        skuList.clear();
        skuList.add(goodsId);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                Log.e(TAG, "Pay:" + billingResult.getResponseCode());

                //测试 appsflyer收入统计
//                    goodMsg = new GoodMsg(
//                            Sum,
//                            "Title",
//                            "Sku",
//                            "USD"
//                    );
//                    AppsFlyer.GetInstance().SendPayMsg(Float.parseFloat(goodMsg.Sum), goodMsg.CONTENT_TYPE, goodMsg.CONTENT_ID, goodMsg.price_currency_code);

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                    if (skuDetailsList.size() > 0) {
                        skuDetail = skuDetailsList.get(0);

                        goodMsg = new GoodMsg(
                                Sum,
                                skuDetail.getTitle(),
                                skuDetail.getSku(),
                                "USD"
                        );

                        Log.e(TAG, skuDetail.toString());
                        googlePay();
                    }
                }
            }
        });
    }

    public void PayVip(Activity activity, String goodsId, int chargeID, int Day, String Sum) {
        sParentActivity = activity;
        mCurThirdType = PAY_FOR_TYPE.VIP;
        mAddDay = Day;
        sChargeID = chargeID;

        Log.e(TAG, "Pay:" + goodsId);
        List<String> skuList = new ArrayList<>();
        skuList.clear();
        skuList.add(goodsId);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        Log.e(TAG, "Pay:" + billingResult.getResponseCode());
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                            if (skuDetailsList.size() > 0) {
                                skuDetail = skuDetailsList.get(0);

                                goodMsg = new GoodMsg(
                                        Sum,
                                        skuDetail.getTitle(),
                                        skuDetail.getSku(),
                                        "USD"
                                );

                                Log.e(TAG, skuDetail.toString());
                                googlePay();
                            }
                        }
                    }
                });
    }

    private void googlePay() {
        if (mCurThirdType == PAY_FOR_TYPE.GOLD) {
            //创建金币订单 1 谷歌支付
            MainHttpUtil.createorder(sChargeID, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    snBean = new Gson().fromJson(info[0], SNData.class);
                    if (isConnect) {
                        BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetail).build();
                        BillingResult result = billingClient.launchBillingFlow(sActivity, flowParams);
                        int responseCode = result.getResponseCode();
                        if (responseCode != 0) {
                            Log.e(TAG, responseCode + ":Current region does not support Google payments");
                        }
                    } else {
                        Log.e(TAG, "Current region does not support Google payments");
                    }
                }
            });
        } else {
            //创建金币订单 1 VIP支付
            MainHttpUtil.createViporder(sChargeID, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    snBean = new Gson().fromJson(info[0], SNData.class);
                    if (isConnect) {
                        BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetail).build();
                        BillingResult result = billingClient.launchBillingFlow(sActivity, flowParams);
                        int responseCode = result.getResponseCode();
                        if (responseCode != 0) {
                            Log.e(TAG, responseCode + ":Current region does not support Google payments");
                        }
                    } else {
                        Log.e(TAG, "Current region does not support Google payments");
                    }
                }
            });
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                Log.e(TAG, "购买成功！！！！" + purchase.getSkus().get(0));
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
            Log.e(TAG, "Handle any other error codes");
        }
    }

    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                ConsumeParams acknowledgePurchaseParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                //注意这里通知方式分3种类型（消耗型、订阅型、奖励型），本文是消耗性产品的通知方式，其它方式请看官方文档
                billingClient.consumeAsync(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
            String DeveloperPayload = purchase.getDeveloperPayload();
            String OrderId = purchase.getOrderId();
            String OriginalJson = purchase.getOriginalJson();
            String PackageName = purchase.getPackageName();
            String PurchaseState = purchase.getPurchaseState() + "";
            String PurchaseTime = purchase.getPurchaseTime() + "";
            String PurchaseToken = purchase.getPurchaseToken();
            String Signature = purchase.getSignature();
            ArrayList<String> Sku = purchase.getSkus();
            //facebook购买事件手动添加
            AppEventsLogger logger = AppEventsLogger.newLogger(sActivity);
            Bundle params = new Bundle();
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE,"product");
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID,OrderId);
            Currency currency = Currency.getInstance("USD");
            logger.logPurchase(new BigDecimal(goodMsg.Sum),currency,params);
            //Tenjin 购买成功埋点事件
            sendPurchaseEvent(purchase,parseDouble(goodMsg.Sum),goodMsg.price_currency_code);
            //发送给第三方统计 支付信息
            //  AdjustUtil.GetInstance().SendPayEvent(Float.parseFloat(goodMsg.Sum),goodMsg.price_currency_code,OrderId);

            if (mCurThirdType == PAY_FOR_TYPE.GOLD) {
                //金币购买
                HttpClient.getInstance().post(AllApi.googlePayCoin, AllApi.googlePayCoin)
                        .params("sn", snBean.getSN())
                        .params("channelorderid", OrderId)
                        .execute(new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {

                                PayBackBean bean = new Gson().fromJson(info[0], PayBackBean.class);

                                UserBean tempBean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                                tempBean.setCoin(tempBean.getCoin() + bean.getCoins());

                                MMKV.defaultMMKV().encode(SpUtil.USER_INFO, tempBean);

                                if (sParentActivity != null) {
                                    sParentActivity.finish();
                                }

                                //JsReadActivity.OnReshPayChapterUI();
                            }
                        });
            } else {
                //VIP支付
                String tatalSec = String.valueOf(mAddDay * 24 * 3600);
                HttpClient.getInstance().post(AllApi.googlePayVip, AllApi.googlePayVip)
                        .params("sn", snBean.getSN())
                        .params("channelorderid", OrderId)
                        .execute(new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                if (sParentActivity != null) {
                                    sParentActivity.finish();
                                }
                            }
                        });
            }
        }
    }
    public void sendPurchaseEvent(Purchase purchase, Double price, String currencyCode) {
        Log.e(TAG, "Tenjin发送！！！！");
        String sku = purchase.getSkus().get(0);
        String purchaseData = purchase.getOriginalJson();
        String dataSignature = purchase.getSignature();
        TenjinSDK instance = TenjinSDK.getInstance(sParentActivity, "HKZXUSKGYMSF3PRHSVJYZYJNGE5WK3ZF");
        instance.transaction(sku, currencyCode, 1, price, purchaseData, dataSignature);
    }

   public  double parseDouble(String s){
       double a;
       try {
           a = Double.parseDouble(s);
       } catch (Exception e) {
           return -1;
       }
       return a;
   }

    private ConsumeResponseListener acknowledgePurchaseResponseListener = new ConsumeResponseListener() {
        @Override
        public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {

        }
    };
}
