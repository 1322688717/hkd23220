package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.utils.StarBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评价本书
 */
public class EvaluateBookActivity extends MyActivity {
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.starBar)
    StarBar starBar;
    @BindView(R.id.book_status)
    TextView bookStatus;
    int starBarNum;
    int anime_id;
    @BindView(R.id.content)
    EditText mContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_evaluate_book;
    }

    @DebugLog
    public static void start(Context context, int anime_id) {
        Intent intent = new Intent(context, EvaluateBookActivity.class);
        intent.putExtra("anime_id", anime_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        anime_id = getIntent().getIntExtra("anime_id", 0);
        //设置星星为整数评分
        starBar.setIntegerMark(true);
        //设置显示的评分等级  默认为1
        starBar.setStarMark(5);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        addComment();
    }

    private void addComment() {
        String content = mContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            toast("请输入内容");
            return;
        }
        starBarNum = (int) starBar.getStarMark();
        HttpClient.getInstance().post(AllApi.commentadd, AllApi.commentadd)
                .params("anime_id", anime_id)
                .params("cid", "")
                .params("content", content)
                .params("star", starBarNum)
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtils.show(msg);
                        }
                        finish();
                    }
                });
    }

    @OnClick({R.id.starBar})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.starBar:
                starBarNum = (int) starBar.getStarMark();
                if (starBarNum == 1) {
                    bookStatus.setText("2.0");
                } else if (starBarNum == 2) {
                    bookStatus.setText("4.0");
                } else if (starBarNum == 3) {
                    bookStatus.setText("6.0");
                } else if (starBarNum == 4) {
                    bookStatus.setText("8.0");
                } else if (starBarNum == 5) {
                    bookStatus.setText("10.0");
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
