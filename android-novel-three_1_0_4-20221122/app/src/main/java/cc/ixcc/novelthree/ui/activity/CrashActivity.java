package cc.ixcc.novelthree.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.aop.SingleClick;
import cc.ixcc.novelthree.common.MyActivity;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;

/**
 *    desc   : 崩溃捕捉界面
 */
public final class CrashActivity extends MyActivity {
    
    private CaocConfig mConfig;

    private AlertDialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crash;
    }

    @Override
    protected void initView() {
        setOnClickListener(R.id.btn_crash_restart, R.id.btn_crash_log);
    }

    @Override
    protected void initData() {
        try {
            //初始化Http
            HttpClient.getInstance().init();

            HttpClient.getInstance().post(AllApi.crashinfo, AllApi.crashinfo)
                    .params("crash_info", CustomActivityOnCrash.getAllErrorDetailsFromIntent(CrashActivity.this, getIntent()).toString())
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            Log.d("TAG", "发送崩溃信息！");
                        }
                    });

        } catch (Exception e) {
            Log.d("MineFragment", "MineFragment_error: " + e);
        }


        mConfig = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        if (mConfig == null) {
            // 这种情况永远不会发生，只要完成该活动就可以避免递归崩溃
            finish();
        }
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_crash_restart:
                CustomActivityOnCrash.restartApplication(CrashActivity.this, mConfig);
                break;
            case R.id.btn_crash_log:
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(CrashActivity.this)
                            .setTitle(R.string.crash_error_details)
                            .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(CrashActivity.this, getIntent()))
                            .setPositiveButton(R.string.crash_close, null)
                            .setNeutralButton(R.string.crash_copy_log, (dialog, which) -> copyErrorToClipboard())
                            .create();
                }
                mDialog.show();
                TextView textView = mDialog.findViewById(android.R.id.message);
                if (textView != null) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 复制报错信息到剪贴板
     */
    private void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(CrashActivity.this, getIntent());
        ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(getString(R.string.crash_error_info), errorInformation));
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}