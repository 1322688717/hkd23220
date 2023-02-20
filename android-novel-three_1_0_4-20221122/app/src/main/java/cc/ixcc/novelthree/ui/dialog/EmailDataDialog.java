package cc.ixcc.novelthree.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import cc.ixcc.novelthree.R;

public class EmailDataDialog extends Dialog {
    EditText mMessage;
    TextView mCommit;
    static Activity mContext = null;
    static EmailDataDialog myDialog = null;
    DialogCallBack mDialogCallBack;

    public EmailDataDialog(Context context, int theme){
        super(context,theme);
    }

    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static EmailDataDialog getMyDialog(Activity context){
        mContext = context;
        myDialog = new EmailDataDialog(context, R.style.dialog);// 创建自定义样式dialog
        myDialog.setContentView(R.layout.dialog_email_bind);// 得到加载view
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        myDialog.getWindow().getAttributes().gravity = Gravity.CENTER;//设置显示位置为中间
        myDialog.setCanceledOnTouchOutside(true);//是否可以用返回键取消
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);//点击对话框以外不关闭
        return myDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //初始化UI
        initUI();
    }

    private void initUI(){
        mCommit = myDialog.findViewById(R.id.btn_commit);
        mMessage = myDialog.findViewById(R.id.tv_input_message);
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogCallBack !=null){
                    mDialogCallBack.onActionClick(mMessage.getText().toString());
                }
            }
        });

//        InputTextHelper.with(mContext)
//                .addView(mMessage)
//                .setMain(mCommit)
//                .setListener(helper ->
//                        (!mMessage.getText().toString().isEmpty())
//                )
//                .build();
    }

    @Override
    public void dismiss(){ super.dismiss();}

    public void onWindowFocusChanged(boolean hasFocus){
        if(myDialog == null){
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
     * @params trMessage
     * @return
     */
    public EmailDataDialog setMessage(String strMessage){

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
    public void setDialogCallBack(DialogCallBack DialogCallBack){
        mDialogCallBack = DialogCallBack;
    }
}
