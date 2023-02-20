package cc.ixcc.novelthree.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.jiusen.base.BaseActivity;
import com.jiusen.base.BaseDialog;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.action.SwipeAction;
import cc.ixcc.novelthree.action.TitleBarAction;
import cc.ixcc.novelthree.action.ToastAction;
import cc.ixcc.novelthree.helper.BindEventBus;
import cc.ixcc.novelthree.http.model.HttpData;
import cc.ixcc.novelthree.ui.dialog.WaitDialog;

import com.hjq.http.listener.OnHttpListener;
import com.tenjin.android.TenjinSDK;
//import com.jiusen.umeng.UmengClient;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * desc   : 项目中的 Activity 基类
 */
public abstract class MyActivity extends BaseActivity
        implements ToastAction, TitleBarAction,
        SwipeAction, OnHttpListener {

    /**
     * 标题栏对象
     */
    private TitleBar mTitleBar;
    /**
     * 状态栏沉浸
     */
    private ImmersionBar mImmersionBar;

    /**
     * 加载对话框
     */
    private BaseDialog mDialog;
    /**
     * 对话框数量
     */
    private int mDialogTotal;
    private Context mContext;

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        if (mDialog == null) {
            mDialog = new WaitDialog.Builder(this).setCancelable(false).create();
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mDialogTotal++;
    }

    /**
     * 显示加载对话框
     */
    public void showDialog(String title) {
        if (mDialog == null) {
            mDialog = new WaitDialog.Builder(this).setCancelable(false).setMessage(title).create();
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mDialogTotal++;
    }

    public void setMessage(String title) {
        if (mDialog == null) return;
        if (!mDialog.isShowing()) {
            return;
        }
        mDialog.setTitle(title);
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        if (mDialogTotal == 1) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
        if (mDialogTotal > 0) {
            mDialogTotal--;
        }
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        //初始化埋点代码
        startTenjinSDK();
//        TenjinSDK instance = TenjinSDK.getInstance(this, "HKZXUSKGYMSF3PRHSVJYZYJNGE5WK3ZF");
//        instance.connect();

        if (getTitleBar() != null) {
            getTitleBar().setOnTitleBarListener(this);
        }

        ButterKnife.bind(this);
        initImmersion();
        mContext = this;
        //判断是否需要注册EventBus
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().register(this);
        }
    }

    private void startTenjinSDK() {
        // Tenjin SDK Integration
        // Add Tenjin API Key from your Tenjin dashboard - https://www.tenjin.io/dashboard/docs.
        TenjinSDK instance = TenjinSDK.getInstance(this, "HKZXUSKGYMSF3PRHSVJYZYJNGE5WK3ZF");
        // Set the appstore
        // If you distribute your app on Google Play store or Amazon store. Then set it to googleplay
        instance.setAppStore(TenjinSDK.AppStoreType.googleplay);
//        //设置app的版本号
//        instance.appendAppSubversion(8888);
        // connect to start the TenjinSDK
        instance.connect();
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersion() {
        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            createStatusBarConfig().init();

            // 设置标题栏沉浸
            if (mTitleBar != null) {
                ImmersionBar.setTitleBar(this, mTitleBar);
            }
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return true;
    }

    /**
     * 初始化沉浸式状态栏
     */
    protected ImmersionBar createStatusBarConfig() {
        // 在BaseActivity里初始化 // 默认状态栏字体颜色为黑色
        mImmersionBar = ImmersionBar.with(this).statusBarDarkFont(isStatusBarDarkFont());
        return mImmersionBar;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    @Nullable
    public ImmersionBar getStatusBarConfig() {
        return mImmersionBar;
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(@StringRes int id) {
        setTitle(getString(id));
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mTitleBar != null) {
            mTitleBar.setTitle(title);
        }
    }

    @Override
    @Nullable
    public TitleBar getTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = findTitleBar(getContentView());
        }
        return mTitleBar;
    }

    @Override
    public void onLeftClick(View v) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTenjinSDK();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    /**
     * {@link OnHttpListener}
     */
    @Override
    public void onStart(Call call) {
        showDialog();
    }

    @Override
    public void onSucceed(Object result) {
        if (result instanceof HttpData) {
            toast(((HttpData) result).getMessage());
        }
    }

    @Override
    public void onFail(Exception e) {
        toast(e.getMessage());
    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }

    @Override
    protected void onDestroy() {
        if (isShowDialog()) {
            mDialog.dismiss();
        }
        mDialog = null;
        if (this.getClass().isAnnotationPresent(BindEventBus.class)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    protected void checkPermission(Activity thisActivity, String permission, int requestCode, String errorText) {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(thisActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, permission)) {
                //Toast.makeText(this,errorText,Toast.LENGTH_SHORT).show();
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity, new String[]{permission}, requestCode);
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity, new String[]{permission}, requestCode);
            }
        } else {
        }
    }
}