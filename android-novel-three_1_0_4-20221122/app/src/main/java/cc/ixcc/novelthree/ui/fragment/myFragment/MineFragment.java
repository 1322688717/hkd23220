package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import cc.ixcc.novelthree.BuildConfig;

import cc.ixcc.novelthree.Constants;

import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.bean.ConfigBean;
import cc.ixcc.novelthree.bean.MineInfoBean;
import cc.ixcc.novelthree.bean.UnReadBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.http.MainHttpUtil;
import cc.ixcc.novelthree.treader.db.MyUserBean;
import cc.ixcc.novelthree.treader.util.AppUtils;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.MyMessageActivity;
import cc.ixcc.novelthree.ui.activity.my.MyWalletActivity;
import cc.ixcc.novelthree.ui.activity.my.ReadPreferencesActivity;
import cc.ixcc.novelthree.ui.activity.my.SearchRecordActivity;
import cc.ixcc.novelthree.ui.activity.my.SettingActivity;
import cc.ixcc.novelthree.ui.activity.my.ThirdLoginActivity;
import cc.ixcc.novelthree.ui.activity.my.TopUpActivity;
import cc.ixcc.novelthree.ui.activity.my.WebView27Activity;
import cc.ixcc.novelthree.ui.activity.my.WebViewActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.MineInfoAdapter;
import cc.ixcc.novelthree.utils.SpUtil;

import com.jiusen.base.views.MyReboundScrollView;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc   : 我的Fragment
 */
public class MineFragment extends MyFragment<HomeActivity> implements MineInfoAdapter.OnClickListener {
    @BindView(R.id.mine_set)
    ImageView mineSet;
    @BindView(R.id.mine_news)
    ImageView mineNews;
    @BindView(R.id.default_img)
    ImageView defaultImg;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.read_time)
    TextView readTime;
    @BindView(R.id.today_coin)
    TextView today_coin;
    @BindView(R.id.coin)
    TextView coin;
    @BindView(R.id.reboundscroll)
    MyReboundScrollView reboundscroll;
    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.sex_img)
    ImageView sexImg;
    @BindView(R.id.ll_top)
    RelativeLayout llTop;
    @BindView(R.id.rl_wallet)
    ConstraintLayout rlWallet;
    @BindView(R.id.red_point)
    ImageView redPoint;
    @BindView(R.id.withdraw)
    LinearLayout withdraw;
    ConfigBean mConfigBean;
    @BindView(R.id.ll_set)
    LinearLayout llSet;
    @BindView(R.id.ll_news)
    RelativeLayout llNews;
    @BindView(R.id.coin_yue)
    TextView coinYue;
    @BindView(R.id.coin_today)
    TextView coinToday;
    MineInfoAdapter mAdapter1;
    MineInfoAdapter mAdapter2;
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;
    List<MineInfoBean.ListBean> mList1 = new ArrayList<>();
    List<MineInfoBean.ListBean> mList2 = new ArrayList<>();
    @BindView(R.id.ivVip)
    ConstraintLayout ivVip;
    @BindView(R.id.tvVip)
    TextView tvVip;
    @BindView(R.id.tvOpenVip)
    LinearLayout tvOpenVip;
    @BindView(R.id.tvOpenVipText)
    TextView tvOpenVipText;

    @BindView(R.id.userID)
    TextView userID;

    private MMKV mmkv = MMKV.defaultMMKV();
    boolean isVisibleToUser;
    public static String ContachUrl;
    public static String HelpUrl;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void initView() {
        coinYue.setText(Constants.getInstance().getCoinName() + " Balance");
        coinToday.setText("Today’s " + Constants.getInstance().getCoinName());
        readTime.getPaint().setFakeBoldText(true);
        initInfoRv1();
        initInfoRv2();
    }

    @Override
    protected void initData() {

    }

    private void initInfoRv1() {
        mAdapter1 = new MineInfoAdapter(getContext());
        recyclerView1.setAdapter(mAdapter1);
        recyclerView1.setNestedScrollingEnabled(false);
        mAdapter1.setOnClickListener(this);
    }

    private void initInfoRv2() {
        mAdapter2 = new MineInfoAdapter(getContext());
        recyclerView2.setAdapter(mAdapter2);
        recyclerView2.setNestedScrollingEnabled(false);
        mAdapter2.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getInfo();
                    setAndroidNativeLightStatusBar(false);
                }
            }, 10);
            //相当于Fragment的onResume，为true时，Fragment已经可见
        } else {
            //相当于Fragment的onPause，为false时，Fragment不可见
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isVisibleToUser) {
            return;
        }

        getInfo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAndroidNativeLightStatusBar(false);
            }
        }, 10);
    }

    private void getDbInfo(UserBean bean) {
        Glide.with(getContext())
                .load(bean.getAvatar())
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(defaultImg);
        userName.setText(bean.getNickname());

        if (bean.getBindStatus().equals("0")) {
            sign.setVisibility(View.VISIBLE);

            userID.setVisibility(View.GONE);
            sign.setText(bean.getSign());
        } else {
            sign.setVisibility(View.GONE);

            userID.setVisibility(View.VISIBLE);
            userID.setText("ID：" + bean.getId());
        }

        sexImg.setVisibility(View.VISIBLE);
        if (TextUtils.equals(bean.getIs_vip(), "1")) {
            sexImg.setImageResource(R.mipmap.mine_vip_sign);
            tvOpenVipText.setText(R.string.text_vip_center);
            tvVip.setText(bean.getViptime() + "Expired");
        } else {
            tvOpenVipText.setText(R.string.text_vip_go);
            tvVip.setText(R.string.vipTitStr);

            if (bean.getGender().equals("0")) { //1男2女0保密
                sexImg.setVisibility(View.GONE);
            } else {
                sexImg.setImageResource(bean.getGender().equals("1") ? R.mipmap.mine_boy : R.mipmap.mine_girl);
            }
        }
    }

    private void getInfo() {
        String token = mmkv.decodeString(SpUtil.TOKEN, "");
        if (TextUtils.isEmpty(token)) { //未登录
            defaultImg.setImageResource(R.mipmap.default_tx);
            userName.setText("Not logged in");
            sign.setText("Fill in your profile");
            tvVip.setText(getString(R.string.vipTitStr));
            tvOpenVipText.setText(getString(R.string.text_vip_center));
            readTime.setText("0");
            coin.setText("0");
            today_coin.setText("0");
            sexImg.setVisibility(View.GONE);
            redPoint.setVisibility(View.GONE);
        } else {
            HttpClient.getInstance().get(AllApi.userinfo, AllApi.userinfo).execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    try {
                        UserBean userbean = new Gson().fromJson(info[0], UserBean.class);
                        mmkv.encode(SpUtil.USER_INFO, userbean);
                        getDbInfo(userbean);
                    } catch (Exception e) {
                        Log.d("MineFragment", "MineFragment_error: " + e);
                    }
                }
            });
        }
        HttpClient.getInstance().post(AllApi.myinfo, AllApi.myinfo).execute(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                try {
                    MineInfoBean bean = new Gson().fromJson(info[0], MineInfoBean.class);
                    setView(bean);
                    getUnReadMessage();
                } catch (Exception e) {
                    Log.d("MineFragment", "MineFragment_error: " + e);
                }
            }
        });
    }

    private void saveUser(MyUserBean bean) {
        if (bean.save()) {
            mmkv.encode(SpUtil.DBUID, String.valueOf(bean.getId()));
        }
    }

    private void setView(MineInfoBean bean) {
        readTime.setText(bean.getRead_time() + "");
        //TODO
        coin.setText(bean.getCoin() + "");
        today_coin.setText(bean.getToday_coin() + "");

        if (mAdapter1 != null && mAdapter2 != null) {
            mList1.clear();
            mList2.clear();
            if (bean.getList().size() > 0) {
                List<MineInfoBean.ListBean> beanList = bean.getList().get(0);
                for (int i = 0; i < beanList.size(); i++) {
                    MineInfoBean.ListBean listBean = beanList.get(i);

                    if (TextUtils.equals(listBean.getName(), "My member")) {
                        //ivVip.setVisibility(View.GONE);
                    } else {
                        mList1.add(listBean);
                    }
                    if (listBean.getName().contains("invite")) {
                        mmkv.encode(Constants.INVITE, listBean.getHref());
                    }
                    if (listBean.getName().contains("My comment")) {
                        mList1.remove(listBean);
                    }
                    if (TextUtils.equals(listBean.getName(), "Contact us")) {
                        ContachUrl = listBean.getHref();
                    } else if (TextUtils.equals(listBean.getName(), "Help & Feedback")) {

                        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        //url链接
                        String url = listBean.getHref();
                        //获取参数
                        String user_id = "?user_id=" + userBean.getId();
                        HelpUrl = url + user_id;

                    }
                }
                mAdapter1.setData(mList1);
            }
            if (bean.getList().size() > 1) {
                mList2.addAll(bean.getList().get(1));
                mAdapter2.setData(mList2);

                for (int i = 0; i < mList2.size(); i++) {
                    MineInfoBean.ListBean listBean = mList2.get(i);

                    if (TextUtils.equals(listBean.getName(), "Contact us")) {
                        ContachUrl = listBean.getHref();
                    } else if (TextUtils.equals(listBean.getName(), "Help & Feedback")) {

                        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        //url链接
                        String url = listBean.getHref();
                        //获取参数
                        String user_id = "?user_id=" + userBean.getId();
                        HelpUrl = url + user_id;

                    }
                }
            }

        }
    }

    private void getUnReadMessage() {
        if (!AppUtils.isLogin()) { //未登录
            return;
        }
        MainHttpUtil.getReadCount(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                UnReadBean bean = new Gson().fromJson(info[0], UnReadBean.class);
                redPoint.setVisibility(bean.getUnread_total() <= 0 ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void setAndroidNativeLightStatusBar(boolean light) {
        ImmersionBar.with(this).statusBarDarkFont(!light).keyboardEnable(true).init();
    }

    @OnClick({R.id.recharge, R.id.ll_top, R.id.rl_wallet, R.id.ll_set, R.id.ll_news, R.id.default_img, R.id.sex_img, R.id.user_name, R.id.sign, R.id.read_time, R.id.coin, R.id.today_coin, R.id.reboundscroll, R.id.tvOpenVip, R.id.tvOpenVip2, R.id.ivVip})
    public void onViewClicked(View view) {
        if (!isLogin()) {
            toast("User not logged in");
            return;
        }
        switch (view.getId()) {
            case R.id.recharge:

////                startActivity(MyWalletActivity.class);
//                UserBean userBean1 = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
//
//                if (userBean1.getBindStatus().equals("0")) {
//                    ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
//                } else {
//                    startActivity(TopUpActivity.class);
//                }

                break;
            case R.id.ll_top:
                startActivity(SettingActivity.class);
                break;
            case R.id.rl_wallet:
                startActivity(MyWalletActivity.class);
                break;
            case R.id.ll_set:
                startActivity(SettingActivity.class);
                break;
            case R.id.ll_news:
                MyMessageActivity.start(getContext(), 0);
                break;
            case R.id.default_img:
                startActivity(SettingActivity.class);
                break;
            case R.id.sex_img:
                startActivity(SettingActivity.class);
                break;
            case R.id.user_name:
                startActivity(SettingActivity.class);
                break;
            case R.id.sign:
                startActivity(SettingActivity.class);
                break;
            case R.id.read_time:
                break;
            case R.id.coin:
                startActivity(MyWalletActivity.class);
                break;
            case R.id.today_coin:
                startActivity(MyWalletActivity.class);
                break;
            case R.id.reboundscroll:
                break;
            case R.id.tvOpenVip:
            case R.id.tvOpenVip2:
            case R.id.ivVip:
//                //开通VIP
//                UserBean userBean2 = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
//                if (userBean2!=null){
//                    if (userBean2.getBindStatus().equals("0")) {
//                        ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.VIP);
//                    } else {
//                        OpenVipActivity.start(getContext());
//                    }
//                }

                UserBean userBean1 = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);

                if (userBean1.getBindStatus().equals("0")) {
                    ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                } else {
                    startActivity(TopUpActivity.class);
                }
                break;
            default:
                break;
        }
    }

    private boolean isLogin() {
        String token = mmkv.decodeString(SpUtil.TOKEN, "");
        if (TextUtils.isEmpty(token)) { //未登录
            startActivity(ThirdLoginActivity.class);
            return false;
        }
        return true;
    }

    @Override
    public void itemClick(MineInfoBean.ListBean bean, int position) {
        if (!isLogin()) {
            return;
        }
        switch (Integer.parseInt(bean.getId())) {
            case 1:
                WebViewActivity.forward(getContext(), bean.getHref(), "invite friends", true);
                break;
            case 2:
                startActivity(ReadPreferencesActivity.class);
                break;
            case 3:
                startActivity(SearchRecordActivity.class);
                break;
            case 4:
                MyMessageActivity.start(getContext(), 2);
                break;
            case 5:
                WebViewActivity.forward(getContext(), bean.getHref(), "contact us", false);
                break;
            case 6:
                HomeActivity.mHomeActivity.Gotofrag_weal();
                break;
            case 7:
                MainHttpUtil.writerApply(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 200) {
                            String href = bean.getHref();
                            String url = "";
                            if (TextUtils.isEmpty(href)) {
                                url = BuildConfig.host + "writer#";
                            } else {
                                url = href;
                            }

                            if (info != null && info.length > 0 && info[0] != null && info[0].length() > 3) {
                                WebView27Activity.forward(getContext(), url + "/Home", "Write for My Book", true);
                            } else {
                                WebView27Activity.forward(getContext(), url, "Write for My Book", true);
                            }
                        }
                    }
                });
                break;
            case 8:
                startActivity(SettingActivity.class);
                break;
            case 9:
                UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);

                //url链接
                String url = bean.getHref();
                //获取参数
                String user_id = "?user_id=" + userBean.getId();

                //WebView27Activity.forward(getContext(), url + "/Home", "Write for My Book", true);
                WebViewActivity.forward(getContext(), url + user_id, "Help & Feedback", true);

                break;
            default:
                break;
        }
    }
}
