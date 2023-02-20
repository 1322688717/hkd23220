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
import androidx.appcompat.widget.AppCompatButton;

import cc.ixcc.novelthree.R;

public class PhotoDialog extends Dialog {
    AppCompatButton mCommit;
    static Activity mContext = null;
    static PhotoDialog myDialog = null;
    DialogCallBack mDialogCallBack;
    TextView takephoto;
    TextView selectphoto;
    TextView cancel;

    public PhotoDialog(Context context, int theme) {
        super(context, theme);
    }

    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static PhotoDialog getMyDialog(Activity context) {
        mContext = context;
        myDialog = new PhotoDialog(context, R.style.dialog2);// 创建自定义样式dialog
        myDialog.setContentView(R.layout.dialog_photo);// 得到加载view
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        myDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;//设置显示位置为下方
//        myDialog.getWindow().getAttributes().gravity = Gravity.CENTER;//设置显示位置为中间
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
        takephoto = myDialog.findViewById(R.id.takephoto);
        selectphoto = myDialog.findViewById(R.id.seletphoto);
        cancel = myDialog.findViewById(R.id.cancel);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogCallBack != null) {
                    mDialogCallBack.onTakePhotoClick();
                }
            }
        });
        selectphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogCallBack != null) {
                    mDialogCallBack.onSelectPhotoClick();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    /**
     * [Summary] setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public PhotoDialog setMessage(String strMessage) {
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
        public void onTakePhotoClick();
        public void onSelectPhotoClick();
    }

    /**
     * 接口回调
     */
    public void setDialogCallBack(DialogCallBack DialogCallBack) {
        mDialogCallBack = DialogCallBack;
    }

}
