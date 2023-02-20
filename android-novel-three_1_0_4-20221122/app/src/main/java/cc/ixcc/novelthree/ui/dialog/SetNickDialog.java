package cc.ixcc.novelthree.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.utils.SpUtil;
import com.tencent.mmkv.MMKV;

import java.util.Timer;
import java.util.TimerTask;

public class SetNickDialog extends Dialog {
    static Activity mContext = null;
    static SetNickDialog myDialog = null;
    DialogCallBack mDialogCallBack;
    TextView commit;
    EditText nickname;
    TextView cancel;

    public SetNickDialog(Context context, int theme) {
        super(context, theme);
    }

    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static SetNickDialog getMyDialog(Activity context) {
        mContext = context;
        myDialog = new SetNickDialog(context, R.style.dialog2);// 创建自定义样式dialog
        myDialog.setContentView(R.layout.dialog_set_nick);// 得到加载view
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
        commit = myDialog.findViewById(R.id.commit);
        nickname = myDialog.findViewById(R.id.nickname);
        cancel = myDialog.findViewById(R.id.cancel);

        showSoftInput(nickname);
        getDbInfo();
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogCallBack != null) {
                    mDialogCallBack.onActionClick(nickname.getText().toString());
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

    /**
     * 自动获取焦点并弹出软键盘
     *
     * @param etIpAddress
     */
    private void showSoftInput(EditText etIpAddress) {
        //自动获取焦点
        etIpAddress.setFocusable(true);
        etIpAddress.setFocusableInTouchMode(true);
        etIpAddress.requestFocus();
        //200毫秒后弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) etIpAddress.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etIpAddress, 0);
            }
        }, 200);
    }

    private void getDbInfo() {
//        String dbid = MMKV.defaultMMKV().decodeString(SpUtil.DBUID, "0");
//        List<MyUserBean> userLists = DataSupport.findAll(MyUserBean.class);
//        for (int i = 0; i < userLists.size(); i++) {
//            MyUserBean myUserBean = userLists.get(i);
//            if (String.valueOf(myUserBean.getId()).equals(dbid)) {
//                nickname.setText(myUserBean.getNickname());
//                nickname.setSelection(nickname.length());
//            }
//        }
        UserBean bean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        nickname.setText(bean.getNickname());
        nickname.setSelection(nickname.length());
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
    public SetNickDialog setMessage(String strMessage) {
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
        public void onActionClick(String nickname);
    }

    /**
     * 接口回调
     */
    public void setDialogCallBack(DialogCallBack DialogCallBack) {
        mDialogCallBack = DialogCallBack;
    }

}
