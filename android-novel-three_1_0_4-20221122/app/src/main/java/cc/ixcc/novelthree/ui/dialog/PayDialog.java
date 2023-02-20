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

import cc.ixcc.novelthree.R;

public class PayDialog extends Dialog {
    TextView mWx;
    TextView mZfb;
    static Activity mContext = null;
    static PayDialog myDialog = null;
    DialogCallBack mDialogCallBack;
    static boolean mWxVisible;
    static boolean mZfbVisible;

    public PayDialog(Context context, int theme) {
        super(context, theme);
    }

    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static PayDialog getMyDialog(Activity context,boolean iswx,boolean iszfb) {
        mContext = context;
        mWxVisible = iswx;
        mZfbVisible = iszfb;
        myDialog = new PayDialog(context, R.style.dialog3);// 创建自定义样式dialog
        myDialog.setContentView(R.layout.dialog_pay);// 得到加载view
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        myDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;//设置显示位置为下方
        myDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;//设置显示位置为中间
        myDialog.setCanceledOnTouchOutside(true);//是否可以用返回键取消
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);//点击对话框以外不关闭
        return myDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化UI
        initUI();
    }

    private void initUI() {
        mWx = myDialog.findViewById(R.id.wx);
        mZfb = myDialog.findViewById(R.id.zfb);
        mWx.setVisibility(mWxVisible ? View.VISIBLE : View.GONE);
        mZfb.setVisibility(mZfbVisible ? View.VISIBLE : View.GONE);
        mWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogCallBack != null) {
                    mDialogCallBack.onAction1Click();
                }
            }
        });
        mZfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogCallBack != null) {
                    mDialogCallBack.onAction2Click();
                }
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
        if (!hasFocus) {
//            dismiss();
        }


    }

    /**
     * [Summary] setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public PayDialog setMessage(String strMessage) {
        // TextView tvMsg = (TextView) myDialog.findViewById(R.id.dialog_title);
       /* if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }*/
        return myDialog;
    }


    public interface DialogCallBack {
        /**
         * 事件点击
         */
        public void onAction1Click();

        public void onAction2Click();
    }

    /**
     * 接口回调
     */
    public void setDialogCallBack(DialogCallBack DialogCallBack) {
        mDialogCallBack = DialogCallBack;
    }

}
