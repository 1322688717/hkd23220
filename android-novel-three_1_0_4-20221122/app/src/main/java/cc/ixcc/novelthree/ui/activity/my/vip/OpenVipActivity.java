package cc.ixcc.novelthree.ui.activity.my.vip;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.bean.IcornPayBackBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.bean.VipHomeBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.jsReader.utils.ToastUtils;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.pay.ali.GooglePay;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.adapter.VipBenefitAdapter;
import cc.ixcc.novelthree.ui.adapter.VipPackageAdapter;
import cc.ixcc.novelthree.utils.DialogUitl;
import cc.ixcc.novelthree.utils.SpUtil;
import cc.ixcc.novelthree.utils.StringUtil;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc   : 开通会员
 */
public final class OpenVipActivity extends MyActivity implements KeyboardWatcher.SoftKeyboardStateListener {
    private static final String TAG = "OpenVipActivity";
    VipBenefitAdapter benefitAdapter;
    VipPackageAdapter chargeAdapter;
    @BindView(R.id.title)
    TitleBar title;

    @BindView(R.id.quanyi)
    RecyclerView quanyi;
    @BindView(R.id.taocan)
    RecyclerView taocan;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.month_money)
    TextView monthMoney;
    @BindView(R.id.btn_commit)
    TextView btnCommit;
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.unVipGroup)
    Group unVipGroup;
    @BindView(R.id.vipGroup)
    Group vipGroup;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvVipTime)
    TextView tvVipTime;

    private int select = -1;
    List<VipHomeBean.VipPrivilegeBean> benefitList;
    List<VipHomeBean.PackageBean> packageList;
    private MMKV mmkv = MMKV.defaultMMKV();

    @DebugLog
    public static void start(Context context) {
        Intent intent = new Intent(context, OpenVipActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String money_symbol = "$";
    @Override
    protected void initData() {
        if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) {
            HttpClient.getInstance().post(AllApi.vipinfo, AllApi.vipinfo).execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (info != null && info.length > 0) {
                        Log.e("TAG", "info: " + info[0]);
                        VipHomeBean vipHomeBean = new Gson().fromJson(info[0], VipHomeBean.class);
                        List<VipHomeBean.PackageBean> packageX = vipHomeBean.getPackageX();
                        for (VipHomeBean.PackageBean x : packageX) {
                            x.setSelect(false);
                        }
                        money_symbol = "$";

                        setData(vipHomeBean);
                    }
                    else {

                    }
                }
            });
        }
        else if(SpUtil.CUR_PAYTYPE == SpUtil.PayType.CHUANYIN)
        {
            HttpClient.getInstance().post(AllApi.chuanyinvipinfo, AllApi.chuanyinvipinfo)
                    .params("country", SpUtil.GetCountry())
                    .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (info != null && info.length > 0) {
                        Log.e("TAG", "info: " + info[0]);
                        VipHomeBean vipHomeBean = new Gson().fromJson(info[0], VipHomeBean.class);
                        List<VipHomeBean.PackageBean> packageX = vipHomeBean.getPackageX();
                        for (VipHomeBean.PackageBean x : packageX) {
                            x.setSelect(false);
                        }
                        money_symbol = vipHomeBean.getPackageX().get(0).getMoney_symbol();

                        setData(vipHomeBean);

                    } else {

                    }
                }
            });
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_vip;
    }

    @Override
    protected void initView() {
        title.setTitle("Membership Card");
        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        tvName.setText(userBean.getNickname());
        if (StringUtil.isVip(userBean)) {
            unVipGroup.setVisibility(View.GONE);
            vipGroup.setVisibility(View.VISIBLE);
            tvName.setText(userBean.getNickname());
            tvVipTime.setText("Expires on "+userBean.getViptime());
        }
        else {
            unVipGroup.setVisibility(View.VISIBLE);
            vipGroup.setVisibility(View.GONE);
        }
        initInfoRv1();
        initInfoRv2();
    }

    private void setData(VipHomeBean bean) {
        Log.d("TAG", "benefit: " + bean.getVip_privilege().size());
        benefitAdapter.addData(bean.getVip_privilege());
        benefitAdapter.notifyDataSetChanged();
        packageList.clear();
        packageList.addAll(bean.getPackageX());
        chargeAdapter.notifyDataSetChanged();
    }

    private void initInfoRv1() {
        if (benefitList == null) {
            benefitList = new ArrayList<>();
        }
        benefitAdapter = new VipBenefitAdapter(benefitList);
        quanyi.setAdapter(benefitAdapter);
        quanyi.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        quanyi.setNestedScrollingEnabled(false);
    }

    private void initInfoRv2() {
        if (packageList == null) {
            packageList = new ArrayList<>();
        }
        chargeAdapter = new VipPackageAdapter(packageList);
        taocan.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        taocan.setAdapter(chargeAdapter);
        taocan.setNestedScrollingEnabled(false);
        chargeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (select == position) {
                    return;
                }
                List<VipHomeBean.PackageBean> data = (List<VipHomeBean.PackageBean>) adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (position == i) {
                        data.get(i).setSelect(true);
                    }
                    else {
                        data.get(i).setSelect(false);
                    }
                }
                select = position;
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void paySuccess() {
        HttpClient.getInstance().get(AllApi.userinfo, AllApi.userinfo).execute(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                UserBean bean = new Gson().fromJson(info[0], UserBean.class);
                mmkv.encode(SpUtil.USER_INFO, bean);
                ToastUtils.show("Payment successful");
                finish();
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        //GooglePay.GetInstance().queryPurchasesAsync();
    }

    public Dialog dialog = null;
    @OnClick({R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                if (select < 0) {
                    ToastUtils.show("Please select a package");
                    return;
                }
                else {
                    //遮挡界面
                    dialog = DialogUitl.loadingDialog(this, "loading");
                    dialog.show();

                    // 延迟5秒执行
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }
                    }, 5000);

                    VipHomeBean.PackageBean bean = packageList.get(select);
                    if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) {
                        String appID = bean.getApple_id();
                        int chargeID = bean.getId();
                        int day = bean.getDay();
                        GooglePay.GetInstance().PayVip(this, appID, chargeID, day, bean.getPrice());
                    }
                    else if(SpUtil.CUR_PAYTYPE == SpUtil.PayType.CHUANYIN) {
                        HttpClient.getInstance().post(AllApi.chuanyincreateorder, AllApi.chuanyincreateorder)
                            .params("countryCode", bean.getCountryCode())
                            .params("currency", bean.getCurrencyCode())
                            .params("amount", bean.getPrice())
                            .params("vip_id", bean.getId())
                            .execute(new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {
                                    Log.d("TAG", "onSuccess: " + info[0]);
                                    IcornPayBackBean bean = new Gson().fromJson(info[0], IcornPayBackBean.class);
                                    IcornPayBackBean.PayBackItem beanContent = new Gson().fromJson(bean.getContent(), IcornPayBackBean.PayBackItem.class);
                                    if(!beanContent.getStatus().equals("0"))
                                    {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(beanContent.getWebUrl())));
                                    }
                                    else
                                    {
                                        //提示重试
                                    }

                                    if (dialog.isShowing()){
                                        dialog.dismiss();
                                    }
                                }
                            });
                    }
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        HomeActivity.onShowComplete(HomeActivity.FirstShowType.SignInfo);
    }
}