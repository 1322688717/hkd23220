package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.bean.WithDrawRecord;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.ui.adapter.myAdapter.WithDrawRecordAdapter;
import cc.ixcc.novelthree.ui.dialog.CoinInfoDialog;
import com.jiusen.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;
import static cc.ixcc.novelthree.Constants.WITHDRAW_FAIL;
import static cc.ixcc.novelthree.Constants.WITHDRAW_SUCCESS;
import static cc.ixcc.novelthree.Constants.WITHDRAW_VERIFY;

/**
 * desc   : 提现明细
 */
public final class WithdrawRecordActivity extends MyActivity
        implements
        KeyboardWatcher.SoftKeyboardStateListener {

    private static final String TAG = "WithdrawRecordActivity";
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.rv_record)
    WrapRecyclerView rvRecord;
    @BindView(R.id.no_data)
    LinearLayout noData;
    private WithDrawRecordAdapter adapter;
    int page = 1;
    List<WithDrawRecord> withDrawRecordList = new ArrayList<>();
    List<String> coinInfoList = new ArrayList<>();

    @DebugLog
    public static void start(Context context) {
        Intent intent = new Intent(context, WithdrawRecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw_record;
    }

    @Override
    protected void initView() {
        initWalletRv();
        getInfo();
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
        adapter = new WithDrawRecordAdapter(getContext());
//        mAdapter1.setOnItemClickListener(this);
//        rvRecord.setLayoutManager(new GridLayoutManager(getContext(), 7, GridLayoutManager.VERTICAL, false));
        rvRecord.setAdapter(adapter);
        rvRecord.setNestedScrollingEnabled(false);
        adapter.setOnClickListener(new WithDrawRecordAdapter.OnClickListener() {
            @Override
            public void onActionClick(WithDrawRecord bean) {
                switch (bean.getStatus()) { //0待审核1通过2拒绝
                    case WITHDRAW_VERIFY: //审核中
                        toast("审核中");
                        break;
                    case WITHDRAW_FAIL: //提现失败
                        toast("提现失败");
                        break;
                    case WITHDRAW_SUCCESS: //提现成功
                        WithdrawDetailActivity.start(getContext(), bean.getId());
                        break;
                }
            }
        });
    }

    //根据泛型返回解析制定的类型
    public List<WithDrawRecord> fromToJson(String json) {
        Type type = new TypeToken<List<WithDrawRecord>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    private void getInfo() {
        HttpClient.getInstance().post(AllApi.withdrawlogs, AllApi.withdrawlogs)
                .params("page", page)
                .params("page_size", PAGE_SIZE)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
//                        Gson gson = new Gson();
//                        List<WithDrawRecord> bean = gson.fromJson(gson.toJson(info), new TypeToken<List<WithDrawRecord>>() {
//                        }.getType());
                        if (page == 1) withDrawRecordList.clear();
                        for (int i = 0; i < info.length; i++) {
                            WithDrawRecord resultBean = new Gson().fromJson(info[i], WithDrawRecord.class);
                            withDrawRecordList.add(resultBean);
                        }
                        noData.setVisibility(withDrawRecordList.size() == 0 ? View.VISIBLE : View.GONE);
                        adapter.setData(withDrawRecordList);
                        if (page == 1) refresh.finishRefresh();
                        else
                            refresh.finishLoadMore();
                    }

                    public void onError() {
                        if (page == 1) refresh.finishRefresh();
                        else
                            refresh.finishLoadMore();
                    }
                });
    }

    private void showCoinInfoDialog() {
        //获取实体类
        final CoinInfoDialog dialog = CoinInfoDialog.getMyDialog(this);
        //可以设置很大不同的动画
        //inviteCodeDialog.getWindow().setWindowAnimations(R.style.Dialog_Anim_Style);
        //inviteCodeDialog.getWindow().setWindowAnimations(R.style.Dialog_Anim_Style2);
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
}