package cc.ixcc.novelthree.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import cc.ixcc.novelthree.Constants;

import cc.ixcc.novelthree.R;

public class SignSuccessDialog extends Dialog{
    //TextView mLingShuDou;
    TextView mShuDou;
    TextView mCancel;
    static Activity mContext = null;
    static SignSuccessDialog myDialog = null;
    DialogCallBack mDialogCallBack;

    public SignSuccessDialog self = null;
    public SignSuccessDialog(Context context, int theme) {
        super(context, theme);
    }

    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static SignSuccessDialog getMyDialog(Activity context) {
        mContext = context;
        // 创建自定义样式dialog
        myDialog = new SignSuccessDialog(context, R.style.dialog);
        // 得到加载view
        myDialog.setContentView(R.layout.dialog_sign_success);
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置显示位置为中间
        myDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        //是否可以用返回键取消
        myDialog.setCanceledOnTouchOutside(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //点击对话框以外不关闭
        builder.setCancelable(true);
        return myDialog;
        //关闭对话框
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化UI
        initUI();
    }

    private void initUI() {
        self = this;
        mShuDou = myDialog.findViewById(R.id.coin);
        mCancel = myDialog.findViewById(R.id.cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
    }

    @Override
    public void show() {
        if (mContext != null) {
            super.show();
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (myDialog == null) {
            return;
        }
        try {
            if (!hasFocus) {
                dismiss();
            }

        } catch (Exception e) {

        }
    }

    public SignSuccessDialog setShuDou(String s) {
        mShuDou = (TextView) myDialog.findViewById(R.id.coin);
        mShuDou.setText("Get "+ s +Constants.getInstance().getCoinName());
        return myDialog;
    }

    public interface DialogCallBack {
        /**
         * 事件点击
         */
        public void onActionClick(String code);
    }

    /**
     * 接口回调
     */
    public void setDialogCallBack(DialogCallBack DialogCallBack) {
        mDialogCallBack = DialogCallBack;
    }
}
