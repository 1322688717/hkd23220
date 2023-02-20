package cc.ixcc.novelthree.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.treader.Config;

public class AddShelveDialog extends Dialog {
    EditText mMessage;
    AppCompatButton mCommit;
    static Activity mContext = null;
    static AddShelveDialog myDialog = null;
    DialogCallBack mDialogCallBack;
    CardView layout;
    TextView title;
    View line1;
    View line2;
    TextView submit;
    TextView cancel;
    Config config;

    public AddShelveDialog(Context context, int theme) {
        super(context, theme);
    }

    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static AddShelveDialog getMyDialog(Activity context) {
        mContext = context;
        myDialog = new AddShelveDialog(context, R.style.dialog);// 创建自定义样式dialog
        myDialog.setContentView(R.layout.dialog_add_shelve);// 得到加载view
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
                    mDialogCallBack.onActionClick(submit.getText().toString());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogCallBack != null) {
                    mDialogCallBack.onCancelClick();
                }
            }
        });
        initDayOrNight();
    }

    private void initDayOrNight() {
//        layout.setBackground(config.getDayOrNight() ?getContext().getResources().getDrawable(R.color.dialog_night) : getContext().getResources().getDrawable(R.color.white));
        layout.setCardBackgroundColor(config.getDayOrNight() ? getContext().getResources().getColor(R.color.dialog_night) : getContext().getResources().getColor(R.color.white));
        title.setTextColor(config.getDayOrNight() ? getContext().getResources().getColor(R.color.black) : getContext().getResources().getColor(R.color.board_bg_s));
        cancel.setTextColor(config.getDayOrNight() ? getContext().getResources().getColor(R.color.board_bg) : getContext().getResources().getColor(R.color.board_bg));
        submit.setTextColor(config.getDayOrNight() ? getContext().getResources().getColor(R.color.day_night) : getContext().getResources().getColor(R.color.colorButtonPressed));
        line1.setBackground(config.getDayOrNight() ? getContext().getResources().getDrawable(R.color.line_night) : getContext().getResources().getDrawable(R.color.line));
        line2.setBackground(config.getDayOrNight() ? getContext().getResources().getDrawable(R.color.line_night) : getContext().getResources().getDrawable(R.color.line));
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
    public AddShelveDialog setMessage(String strMessage) {
        // TextView tvMsg = (TextView) myDialog.findViewById(R.id.dialog_title);
       /* if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }*/
        return myDialog;
    }


    public abstract static class DialogCallBack {
        /**
         * 事件点击
         */
        public abstract void onActionClick(String code);

        public void onCancelClick() {
        }
    }

    /**
     * 接口回调
     */
    public void setDialogCallBack(DialogCallBack DialogCallBack) {
        mDialogCallBack = DialogCallBack;
    }

}
