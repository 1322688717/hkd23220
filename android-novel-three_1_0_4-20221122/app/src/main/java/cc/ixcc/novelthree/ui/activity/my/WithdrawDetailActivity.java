package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.bean.WithdrawDetailBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.other.KeyboardWatcher;

import butterknife.BindView;
import butterknife.OnClick;

import static cc.ixcc.novelthree.Constants.WX_PAY;

/**
 * desc   : 提现详情
 */
public final class WithdrawDetailActivity extends MyActivity
        implements
        KeyboardWatcher.SoftKeyboardStateListener {

    private static final String TAG = "WithdrawRecordActivity";
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.timestr)
    TextView timestr;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.typestr)
    TextView typestr;
    @BindView(R.id.type)
    TextView type;
    int id;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.button)
    TextView button;

    @DebugLog
    public static void start(Context context, int id) {
        Intent intent = new Intent(context, WithdrawDetailActivity.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw_detail;
    }

    @Override
    protected void initView() {
        id = getIntent().getIntExtra("id", 0);
        getInfo(id);
    }

    private void getInfo(int id) {
        HttpClient.getInstance().post(AllApi.withdrawdetail, AllApi.withdrawdetail)
                .params("id", id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        WithdrawDetailBean bean = new Gson().fromJson(info[0], WithdrawDetailBean.class);
                        setView(bean);
                    }
                });
    }

    private void setView(WithdrawDetailBean bean) {
        time.setText(bean.getCreated_at());
        type.setText(bean.getType() == WX_PAY ? "微信" : "支付宝");
        money.setText(bean.getMoney());
        getStatus(status, bean.getStatus());
    }

    private void getStatus(TextView statustv, int status) {
        statustv.setText("提现成功");
        statustv.setTextColor(Color.parseColor("#4D77FD"));
//        switch (status) {
//            case WITHDRAW_VERIFY: //审核中
//                statustv.setText("审核中");
//                statustv.setTextColor(Color.parseColor("#FCAD0F"));
//                break;
//            case WITHDRAW_FAIL: //提现失败
//                statustv.setText("提现失败");
//                statustv.setTextColor(Color.parseColor("#D53D3C"));
//                break;
//            case WITHDRAW_SUCCESS: //提现成功
//                statustv.setText("提现成功");
//                statustv.setTextColor(Color.parseColor("#4D77FD"));
//                break;
//        }
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

    @OnClick({R.id.type, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.type:
                break;
            case R.id.button:
                finish();
                break;
        }
    }
}