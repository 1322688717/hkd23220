package cc.ixcc.novelthree.ui.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.Read;
import cc.ixcc.novelthree.ui.adapter.myAdapter.DialogAdapter;

import java.util.ArrayList;
import java.util.List;

public class AccountDialog extends Dialog {

    private static Context context = null;
    private static AccountDialog myDialog = null;
    private static ArrayList<Read.HobbyBean> mlist;
    private TextView dialog_commit;
    private TextView dialog_cancle;
    RecyclerView dialogRv;
    DialogAdapter dialogAdapter;
    private SettingDialogCallBack settingDialogCallBack;
    int index = 0;

    public AccountDialog(Context context, int theme) {
        super(context, theme);
    }


    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static AccountDialog getMyDialog(Context context, List<Read.HobbyBean> list) {
        mlist = new ArrayList<>();
        AccountDialog.mlist.addAll(list);
        AccountDialog.context = context;
        myDialog = new AccountDialog(context, R.style.dialog_trans);// 创建自定义样式dialog
        myDialog.setContentView(R.layout.dialog_super);// 得到加载view
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        myDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;//设置显示位置为下方
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
        dialogRv = myDialog.findViewById(R.id.dialog_rv);
        dialog_cancle = myDialog.findViewById(R.id.dialog_cancle);
        dialog_commit = myDialog.findViewById(R.id.dialog_commit);

//        dialogRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dialogRv.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        dialogAdapter = new DialogAdapter(context, mlist);
        //dialogAdapter.setOnItemClickListener(this);
        dialogRv.setAdapter(dialogAdapter);
        dialogAdapter.setCallBackListener(new DialogAdapter.onCallBackListener() {
            @Override
            public void callBack(int position) {
                //dialogAdapter.setThisPosition(position);
                index = position;
                if (mlist.get(position).getIsselect().equals("1")) {
                    mlist.get(position).setIsselect("0");
                    dialogAdapter.notifyDataSetChanged();
                    return;
                }
                mlist.get(position).setIsselect("1");
                //price.setText("¥" + mlist.get(position).getPrice());
                //嫑忘记刷新适配器
                dialogAdapter.notifyDataSetChanged();
            }
        });

        dialog_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里可以为布局上其他的控件设置监听，添加不同的id传值就行。用来回调
//                settingDialogCallBack.onActionClick(1);//点击button为1
                myDialog.dismiss();
            }
        });
        dialog_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里可以为布局上其他的控件设置监听，添加不同的id传值就行.用来回调
                settingDialogCallBack.onActionClick(getSelectId());//点击button为2
            }
        });
    }

    private String getSelectId() {
        String id = "";
        for (Read.HobbyBean hobbyBean : mlist) {
            if (hobbyBean.getIsselect().equals("1")) {
                id += "," + hobbyBean.getId();
                continue;
            }
        }
        return id.length() > 1 ? id.substring(1, id.length()) : id;
    }


    @Override
    public void show() {
        if (context != null) {
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
            if (!hasFocus)
            {
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
    public AccountDialog setMessage(String strMessage)
    {
        return myDialog;
    }


    public interface SettingDialogCallBack {
        /**
         * 事件点击
         *
         * @param id 返回点击按键的id
         */
        public void onActionClick(String id);
    }

    /**
     * 接口回调
     *
     * @param settingDialogCallBack
     */
    public void setSettingDialogCallBack(SettingDialogCallBack settingDialogCallBack) {
        this.settingDialogCallBack = settingDialogCallBack;
    }

}
