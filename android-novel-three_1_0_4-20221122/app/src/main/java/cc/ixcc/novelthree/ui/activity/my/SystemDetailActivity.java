package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.bean.MyMessagebean;
import cc.ixcc.novelthree.common.MyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc   : 系统消息
 */
public final class SystemDetailActivity extends MyActivity {

    private static final String TAG = "SystemDetailActivity";
    MyMessagebean mBean;
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.title)
    TextView title;

    @DebugLog
    public static void start(Context context, MyMessagebean bean) {
        Intent intent = new Intent(context, SystemDetailActivity.class);
        intent.putExtra("bean", bean);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_detail;
    }

    @Override
    protected void initView() {
        mBean = (MyMessagebean) getIntent().getSerializableExtra("bean");
        setView();
    }

    private void setView() {
        if (mBean == null) return;
        title.setText(mBean.getTitle());
        time.setText(mBean.getTime_detail());
        content.setText(mBean.getDetail());
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}