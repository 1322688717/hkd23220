package cc.ixcc.novelthree.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.treader.Config;

public class TipDialog extends Dialog {
    static Activity mContext = null;
    static TipDialog myDialog = null;
    DialogCallBack mDialogCallBack;
    DialogCancelCallBack mCancelCallBack;
    CardView layout;
    TextView title;
    View line1;
    View line2;
    TextView submit;
    TextView cancel;
    Config config;

    public TipDialog(Context context, int theme) {
        super(context, theme);
    }

    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static TipDialog getMyDialog(Activity context) {
        mContext = context;
        myDialog = new TipDialog(context, R.style.dialog);// 创建自定义样式dialog
        myDialog.setContentView(R.layout.dialog_tip);// 得到加载view
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        myDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;//设置显示位置为下方
        myDialog.getWindow().getAttributes().gravity = Gravity.CENTER;//设置显示位置为中间
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

    public void setTitle(String s) {
        if (title == null) title = myDialog.findViewById(R.id.title);
        if (TextUtils.isEmpty(s)) title.setVisibility(View.GONE);
        else
            title.setText(s);
    }

    public void setSubmit(String s) {
        if (submit == null) submit = myDialog.findViewById(R.id.submit);
        if (TextUtils.isEmpty(s)) submit.setVisibility(View.GONE);
        else
            submit.setText(s);
    }

    public void setCancel(String s) {
        if (cancel == null) cancel = myDialog.findViewById(R.id.cancel);
        if (TextUtils.isEmpty(s)) cancel.setVisibility(View.GONE);
        else
            cancel.setText(s);
    }

    private void initUI() {
        config = Config.getInstance();
        layout = myDialog.findViewById(R.id.layout);
        title = myDialog.findViewById(R.id.title);
        line1 = myDialog.findViewById(R.id.line1);
        line2 = myDialog.findViewById(R.id.line2);
        submit = myDialog.findViewById(R.id.submit);
        cancel = myDialog.findViewById(R.id.cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogCallBack != null) {
                    mDialogCallBack.onActionClick();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                if (mCancelCallBack!=null) {
                    mCancelCallBack.onCancel();
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
        try {
            if (!hasFocus) {
                dismiss();
            }

        } catch (Exception e) {

        }
    }

    /**
     * [Summary] setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public TipDialog setMessage(String strMessage) {
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
        public void onActionClick();
    }

    public interface DialogCancelCallBack{
        void onCancel();
    }

    /**
     * 接口回调
     */
    public void setDialogCallBack(DialogCallBack DialogCallBack) {
        mDialogCallBack = DialogCallBack;
    }


    /**
     * 接口回调
     */
    public void setCancelCallBack(DialogCancelCallBack cancel) {
        mCancelCallBack = cancel;
    }
}
