package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.bean.WalletBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.ui.adapter.myAdapter.WalletRecordAdapter;
import cc.ixcc.novelthree.ui.dialog.CoinInfoDialog;
import com.jiusen.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mmkv.MMKV;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cc.ixcc.novelthree.utils.SpUtil;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

/**
 * desc   : 我的钱包
 */
public final class MyWalletActivity extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener {
    private static final String TAG = "MyWalletActivity";
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.withdraw)
    TextView withdraw;
    @BindView(R.id.recharge)
    TextView recharge;
    @BindView(R.id.now_coin)
    TextView nowCoin;
    @BindView(R.id.coin)
    TextView Tcoin;
    @BindView(R.id.today_coin)
    TextView Ttoday_coin;
    @BindView(R.id.rmb)
    TextView rmb;
    @BindView(R.id.today_coin)
    TextView todayCoin;
    @BindView(R.id.sum_coin)
    TextView sumCoin;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.rl_wallet)
    LinearLayout rlWallet;
    @BindView(R.id.rv_record)
    WrapRecyclerView rvRecord;
//    @BindView(R.id.recharge)
//    TextView recharge;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.coin_yue)
    TextView coinYue;
    @BindView(R.id.today_coin2)
    TextView todayCoin2;
    @BindView(R.id.coin_sum)
    TextView coinSum;
    @BindView(R.id.coin_minxi)
    TextView coinMinxi;
    @BindView(R.id.no_data)
    LinearLayout noData;
    private WalletRecordAdapter adapter;
    int page = 1;
    List<WalletBean.FinanceBean> financeBeanList = new ArrayList<>();
    List<String> coinInfoList = new ArrayList<>();

    private MMKV mmkv = MMKV.defaultMMKV();

    @DebugLog
    public static void start(Context context) {
        Intent intent = new Intent(context, MyWalletActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void initView() {
        coinYue.setText(Constants.getInstance().getCoinName() + "Balance");
        coinSum.setText("Sum" + Constants.getInstance().getCoinName());
        todayCoin2.setText("Today" + Constants.getInstance().getCoinName());
        coinMinxi.setText(Constants.getInstance().getCoinName() + " details");
        info.setText("synopsis");
        initWalletRv();
        LoadInfo();
    }

    public void LoadInfo(){
        String token = mmkv.decodeString(SpUtil.TOKEN, "");
        if (!TextUtils.isEmpty(token)) { //登录
            HttpClient.getInstance().get(AllApi.userinfo, AllApi.userinfo).execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    UserBean bean = new Gson().fromJson(info[0], UserBean.class);
                    mmkv.encode(SpUtil.USER_INFO, bean);
                }
            });
        }
    }

    private void initWalletRv() {
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getInfo();
            }
        });
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getInfo();
            }
        });
        adapter = new WalletRecordAdapter(getContext());
        rvRecord.setAdapter(adapter);
        rvRecord.setNestedScrollingEnabled(false);
        adapter.setOnClickListener(new WalletRecordAdapter.OnClickListener() {
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }

    private void getInfo() {
        HttpClient.getInstance().get(AllApi.wallet, AllApi.wallet)
                .params("page", page)
                .params("page_size", PAGE_SIZE)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        WalletBean bean = new Gson().fromJson(info[0], WalletBean.class);
                        List<WalletBean.FinanceBean> financeBeans = bean.getFinance();
                        if (page == 1) {
                            financeBeanList.clear();
                        }
                        financeBeanList.addAll(financeBeans);
                        setView(bean);
                        if (page == 1) {
                            refresh.finishRefresh(500);
                        }
                        else {
                            refresh.finishLoadMore(500);
                        }
                    }
                });
    }

    private void setView(WalletBean bean) {
        if (bean == null) return;
        nowCoin.setText(getFormatString(bean.getNow_coin() + ""));
        todayCoin.setText(getFormatString(bean.getToday_coin() + ""));
        sumCoin.setText(getFormatString(bean.getHistory_coin() + ""));
        rmb.setText("=" + bean.getNow_coin_rmb() + "$");
        noData.setVisibility(financeBeanList.size() == 0 ? View.VISIBLE : View.GONE);
        adapter.setData(financeBeanList);
        coinInfoList = bean.getCoin_introduction();
    }

    private void showCoinInfoDialog() {
        //获取实体类
        final CoinInfoDialog dialog = CoinInfoDialog.getMyDialog(this);
        //回调实现点击
        dialog.setList(coinInfoList);
        dialog.setDialogCallBack(new CoinInfoDialog.DialogCallBack() {
            @Override
            public void onActionClick(String code) {
            }
        });
        dialog.show();
    }

    private String getFormatString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.recharge, R.id.ll_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recharge: {
                UserBean userBean1 = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                if (userBean1.getBindStatus().equals("0")) {
                    ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                }
                else {
                    startActivity(TopUpActivity.class);
                }
            }
                break;
            case R.id.ll_info:
                showCoinInfoDialog();
                break;
        }
    }
}