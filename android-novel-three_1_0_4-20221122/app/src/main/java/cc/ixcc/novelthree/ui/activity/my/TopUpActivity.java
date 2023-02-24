package cc.ixcc.novelthree.ui.activity.my;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;

import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.bean.ActiviyNoticeInfo;
import cc.ixcc.novelthree.bean.BuyCoinInfo;
import cc.ixcc.novelthree.bean.IcornPayBackBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.custom.ShelveBannerLoader;
import cc.ixcc.novelthree.event.AddCountEvent;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.pay.ali.GooglePay;
import cc.ixcc.novelthree.statistics.AdjustUtil;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.vip.OpenVipActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.BuyCoinAdapter;

import com.jiusen.widget.layout.WrapRecyclerView;
import com.tencent.mmkv.MMKV;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.OnClick;
import cc.ixcc.novelthree.ui.fragment.CountTimerFragment;
import cc.ixcc.novelthree.ui.fragment.myFragment.MineFragment;
import cc.ixcc.novelthree.utils.DialogUitl;
import cc.ixcc.novelthree.utils.SpUtil;
import cc.ixcc.novelthree.utils.StringUtil;

/**
 * desc   : 充值
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public final class TopUpActivity extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener {
    @BindView(R.id.coin)
    TextView coin;
    @BindView(R.id.rv_coin)
    WrapRecyclerView rvCoin;
    @BindView(R.id.add_book)
    TextView money;

//    @BindView(R.id.banner)
//    Banner banner;

//    @BindView(R.id.banner2)
//    ViewPager2 pager2;

    BuyCoinAdapter adapter;
    int type = 0;
    String name;
    String account;
    BuyCoinInfo.CoinListBean mCoinBean;
    public static TopUpActivity MYActivity;
    public BuyCoinInfo beandata;
    public static String sCountryCode = "";
    public static String sCurrencyCode = "";
    MMKV mmkv = MMKV.defaultMMKV();

    private List<CountTimerFragment> fragments;

    @DebugLog
    public static void start(Context context) {
        Intent intent = new Intent(context, TopUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topup;
    }

    @Override
    protected void initView() {
        MYActivity = this;
        fragments = new ArrayList<>();
        EventBus.getDefault().register(this);
        initCoinRv();
        getInfo();
        //setcustomerserviceText();
       // initBanner();
    }

    //初始化banner运营位
//    public void initBanner() {
//        try {
//            if (!HomeActivity.mHomeActivity.IsPostNotice) {
//                HomeActivity.mHomeActivity.GetActiviyNoticeMsg(() -> {
//                    ShowBanner(HomeActivity.mHomeActivity);
//                });
//            } else {
//                ShowBanner(HomeActivity.mHomeActivity);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void updateCopyright(String envent) {
////        Log.e("EventBus", "BookShelveFragment--Copyright接受数据" + envent);
//        if (envent.equals("-1")) {
//            banner.setVisibility(View.VISIBLE);
//        } else {
//            banner.setVisibility(View.GONE);
//        }
//    }
//    public void ShowBanner(HomeActivity mHomeActivity) {
//        //如果有运营位则显示 没有则隐藏
//        if (mHomeActivity.adList_topUp.size() > 0) {
//            List<ActiviyNoticeInfo> list = mHomeActivity.adList_topUp;
//            ActiviyNoticeInfo discount = null;
//            List<ActiviyNoticeInfo> unDiscount = null;
//            try {
//                List<ActiviyNoticeInfo> count = list.stream().filter(s -> s.getType() == 17).collect(Collectors.toList());
//                unDiscount = list.stream().filter(s -> s.getType() != 17).collect(Collectors.toList());
////                for (int i = 0; i < unDiscount.size(); i++) {
////                    fragments.add(new CountTimerFragment(unDiscount.get(i).getUrl(), mHomeActivity.adList_topUp.get(i).getType()));
////                }
//                int flag = new Random().nextInt(count.size());
//                discount = count.get(flag);
//                fragments.add(new CountTimerFragment(discount, unDiscount.get(0)));
//            } catch (Exception e) {
//            }
//
//            if (unDiscount != null){
//                int zong = 0;
//                if (discount != null){
//                    zong =  unDiscount.size() + 1;
//                }else {
//                    zong = unDiscount.size();
//                }
//                int finalZong = zong;
//                pager2.setAdapter(new FragmentStateAdapter(this) {
//                    @NonNull
//                    @Override
//                    public CountTimerFragment createFragment(int position) {
//                        return fragments.get(position);
//                    }
//
//                    @Override
//                    public int getItemCount() {
//                        return finalZong;
//                    }
//                });
//
//                pager2.setCurrentItem(0);
//                pager2.setUserInputEnabled(false);
//
//            }
//            banner.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
////            banner.setVisibility(View.GONE);
//
//            banner.setOutlineProvider(new ViewOutlineProvider() { //要在加载图片之前设置这个方法
//                @Override
//                public void getOutline(View view, Outline outline) {
//                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20); //设置圆角
//                }
//            });
//
//            banner.setClipToOutline(true);
//            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//            //设置图片加载器
//            banner.setImageLoader(new ShelveBannerLoader());
//            //设置图片集合
//            banner.setImages(mHomeActivity.adList_topUp);
//            //设置轮播时间
//            banner.setDelayTime(5000);
//            banner.setOnBannerListener(new OnBannerListener() {
//                @Override
//                public void OnBannerClick(int position) {
//                    Log.e("TAG", "banner_position:" + position);
//                    if (mHomeActivity.adList_topUp != null) {
//                        if (position >= 0 && position < mHomeActivity.adList_topUp.size()) {
//                            ActiviyNoticeInfo bean = mHomeActivity.adList_topUp.get(position);
//                            if (bean != null) {
////                                AdjustUtil.GetInstance().SendBannerEvent(5);
//                                UserBean tempBean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
//                                //17支付活动 18新书推荐 19VIP活动 20URL活动
//                                switch (bean.getType()) {
//                                    case 17:
//                                        if (tempBean.getBindStatus().equals("0")) {
//                                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
//                                        } else {
//                                            for (int i = 0; i < beandata.getCoin_list().size(); i++) {
//                                                Log.e("Pay", (beandata.getCoin_list().get(i).getpayid() == null) + "");
//                                                Log.e("Pay", (bean.getpay_id() == null) + "");
//                                                Log.e("Pay", beandata.getCoin_list().get(i).getpayid() + "|" + bean.getpay_id());
//
//                                                if (beandata.getCoin_list().get(i).getpayid().equals(bean.getpay_id())) {
//                                                    if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) {
//                                                        GooglePay.GetInstance().PayForCoin(MYActivity, beandata.getCoin_list().get(i).getpayid(),
//                                                                beandata.getCoin_list().get(i).getChargeIndex(), beandata.getCoin_list().get(i).getRmb());
//                                                    } else if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.CHUANYIN) {
//                                                        //sCurrencyCode
//                                                        HttpClient.getInstance().post(AllApi.paynicorn, AllApi.paynicorn)
//                                                                .params("countryCode", sCountryCode)
//                                                                .params("currency", sCurrencyCode)
//                                                                .params("amount", beandata.getCoin_list().get(i).getRmb())
//                                                                .params("coin", beandata.getCoin_list().get(i).getCoin())
//                                                                .execute(new HttpCallback() {
//                                                                    @Override
//                                                                    public void onSuccess(int code, String msg, String[] info) {
//                                                                        Log.d("TAG", "onSuccess: " + info[0]);
//                                                                        IcornPayBackBean bean = new Gson().fromJson(info[0], IcornPayBackBean.class);
//                                                                        IcornPayBackBean.PayBackItem beanContent = new Gson().fromJson(bean.getContent(), IcornPayBackBean.PayBackItem.class);
//                                                                        if (!beanContent.getStatus().equals("0")) {
//                                                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(beanContent.getWebUrl())));
//                                                                        } else {
//                                                                            //提示重试
//                                                                        }
//
//                                                                        if (dialog.isShowing()) {
//                                                                            dialog.dismiss();
//                                                                        }
//                                                                    }
//                                                                });
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        break;
//                                    case 18:
//                                        BookDetailActivity.start(getContext(), bean.getAnid());
//                                        break;
//                                    case 19:
//                                        if (tempBean.getBindStatus().equals("0")) {
//                                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
//                                        } else {
//                                            OpenVipActivity.start(getContext());
//                                        }
//                                        break;
//                                    case 20:
//                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bean.gethttp_url())));
//                                        break;
//                                    default:
//                                        break;
//                                }
//                            }
//                        }
//                    }
//
//                }
//            });
//            //banner设置方法全部调用完毕时最后调用
//            banner.start();
//        }
//    }

    private void initCoinRv() {
        adapter = new BuyCoinAdapter(getContext());
        rvCoin.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        rvCoin.setAdapter(adapter);
        rvCoin.setNestedScrollingEnabled(false);
        adapter.setOnClickListener(new BuyCoinAdapter.OnClickListener() {
            @Override
            public void onActionClick(BuyCoinInfo.CoinListBean bean) {
                mCoinBean = bean;
                money.setText(money_symbol + " " + mCoinBean.getRmb());
            }

            @Override
            public void onClear() {
                mCoinBean = null;
            }
        });
    }

    public static String money_symbol = "$";

    //1为谷歌 2为H5
    private void getInfo() {
        if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) {
            HttpClient.getInstance().post(AllApi.chargereqconfig, AllApi.chargereqconfig)
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            beandata = new Gson().fromJson(info[0], BuyCoinInfo.class);

                            money_symbol = "$";

                            Log.d("xcs info", info[0]);


                            setView(beandata);
                        }

                        @Override
                        public void onError() {
                            super.onError();
                            Log.d("Xcs", "topup error");
                        }
                    });
        } else if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.CHUANYIN) {
            HttpClient.getInstance().post(AllApi.pay_nicorn_config, AllApi.pay_nicorn_config)
                    .params("country", SpUtil.GetCountry())
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            Log.d("TAG", "onSuccess: " + info[0]);
                            beandata = new Gson().fromJson(info[0], BuyCoinInfo.class);
                            sCountryCode = beandata.getCountryCode();
                            sCurrencyCode = beandata.getCurrencyCode();

                            money_symbol = beandata.getMoney_symbol();

                            setView(beandata);
                        }
                    });
        }
    }

    private void setView(BuyCoinInfo bean) {
        if (bean == null) {
            return;
        }

        coin.setText(bean.getCoin() + "");
        adapter.setData(bean.getCoin_list().subList(0,bean.getCoin_list().size() ));

        if (!checkVip()) {
            //添加 视频广告按钮
            List<BuyCoinInfo.CoinListBean> AdCoins = new ArrayList<>();
            BuyCoinInfo.CoinListBean AdCoin = new BuyCoinInfo.CoinListBean();
            AdCoin.setChargeIndex(-99);
            AdCoins.add(AdCoin);
            adapter.addData(AdCoins);
        }


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        params.topMargin = 30;
        LinearLayout linear = (LinearLayout) findViewById(R.id.Layout_Tips);

        for (int i = 0; i < bean.getpaytips().size(); i++) {
            TextView textView = new TextView(this);
            textView.setText((i + 1) + ". " + bean.getpaytips().get(i));
            textView.setTextColor(getColor(R.color.pay_tip));
            textView.setTextSize(12);
            textView.setLayoutParams(params);

            linear.addView(textView);
        }

        TextView customerservice = new TextView(this);
        customerservice.setTextColor(getColor(R.color.pay_tip));
        customerservice.setTextSize(12);
        customerservice.setLayoutParams(params);
        // 不需要点击的文字
        customerservice.setText((bean.getpaytips().size() + 1) + ".  If your top-up failed or the iCoins were not credited to your account for more than 24 hours, You can contact ");
        // 设置需要点击的文字
        SpannableString clickString1 = new SpannableString("customer service");
        // 设置需要点击文字的样式
        clickString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 点击之后需要做的操作
                WebViewActivity.forward(getContext(), MineFragment.ContachUrl, "contact us", false);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 设置可点击文字的颜色
                ds.setColor(getColor(R.color.text_color_theme)); //设置颜色
            }
        }, 0, clickString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将需要点击的文字添加到我们的TextView中
        customerservice.append(clickString1);
        // 继续添加不需要点击的文字组装TextView
        customerservice.append(new SpannableString("."));
        // 设置点击文字点击效果为透明
        customerservice.setHighlightColor(Color.TRANSPARENT);
        // 开始响应点击事件
        customerservice.setMovementMethod(LinkMovementMethod.getInstance());
        linear.addView(customerservice);

        TextView customerHelp = new TextView(this);
        customerHelp.setTextColor(getColor(R.color.pay_tip));
        customerHelp.setTextSize(12);
        customerHelp.setLayoutParams(params);
        // 不需要点击的文字
        customerHelp.setText((bean.getpaytips().size() + 2) + ".  After a purchase or recharge, if the balance does not update after a long period please contact us via ");
        // 设置需要点击的文字
        SpannableString clickString2 = new SpannableString("Help & Feedback");
        // 设置需要点击文字的样式
        clickString2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                WebViewActivity.forward(getContext(), MineFragment.HelpUrl, "Help & Feedback", false);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 设置可点击文字的颜色
                ds.setColor(getColor(R.color.text_color_theme)); //设置颜色
            }
        }, 0, clickString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将需要点击的文字添加到我们的TextView中
        customerHelp.append(clickString2);
        // 继续添加不需要点击的文字组装TextView
        customerHelp.append(new SpannableString("."));
        // 设置点击文字点击效果为透明
        customerHelp.setHighlightColor(Color.TRANSPARENT);
        // 开始响应点击事件
        customerHelp.setMovementMethod(LinkMovementMethod.getInstance());
        linear.addView(customerHelp);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {

    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //GooglePay.GetInstance().queryPurchasesAsync();
    }

    @Override
    public void onRightClick(View v) {
        startActivity(WithdrawRecordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commit:
                //todo 提现修改为充值
                TopUp();
                break;
            default:
                break;
        }
    }

    public Dialog dialog = null;

    private void TopUp() {
        if (mCoinBean == null) {
            toast("Please select the recharge amount");
            return;
        }

        //遮挡界面
        dialog = DialogUitl.loadingDialog(this, "loading");
        dialog.show();

        // 延迟5秒执行
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, 5000);


        Log.d("TAG", (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) + "");


        if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) {
            GooglePay.GetInstance().PayForCoin(this, mCoinBean.getpayid(), mCoinBean.getChargeIndex(), mCoinBean.getRmb());
        } else if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.CHUANYIN) {
            //sCurrencyCode

            HttpClient.getInstance().post(AllApi.paynicorn, AllApi.paynicorn)
                    .params("countryCode", sCountryCode)
                    .params("currency", sCurrencyCode)
                    .params("amount", mCoinBean.getRmb())
                    .params("coin", mCoinBean.getCoin())
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            Log.d("TAG", "onSuccess: " + info[0]);
                            IcornPayBackBean bean = new Gson().fromJson(info[0], IcornPayBackBean.class);
                            IcornPayBackBean.PayBackItem beanContent = new Gson().fromJson(bean.getContent(), IcornPayBackBean.PayBackItem.class);
                            if (!beanContent.getStatus().equals("0")) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(beanContent.getWebUrl())));
                            } else {
                                //提示重试
                            }

                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onError() {
                            super.onError();
                            Log.d("TAG", "TopUpActivity: " + "onError");
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddCountEvent e) {
        type = e.getType();
        name = e.getName();
        account = e.getAccount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        HomeActivity.onShowComplete(HomeActivity.FirstShowType.SignInfo);
    }

    //是否是vip
    private boolean checkVip() {
        UserBean bean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        return StringUtil.isVip(bean);
    }
}