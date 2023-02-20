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
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.ui.adapter.myAdapter.CoinInfoAdapter;

import java.util.List;

public class CoinInfoDialog extends Dialog {
    static Activity mContext = null;
    static CoinInfoDialog myDialog = null;
    DialogCallBack mDialogCallBack;
    private RecyclerView rvinfo;
    private TextView title;
    private View view;
    private CoinInfoAdapter adapter;

    public CoinInfoDialog(Context context, int theme) {
        super(context, theme);
    }

    /***
     *
     * @param context   传入的上下文
     * @return AccountDialog   返回dialog对象
     */
    public static CoinInfoDialog getMyDialog(Activity context) {
        mContext = context;
        myDialog = new CoinInfoDialog(context, R.style.dialog);// 创建自定义样式dialog
//        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_coin_info);// 得到加载view
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        myDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;//设置显示位置为下方
        myDialog.getWindow().getAttributes().gravity = Gravity.TOP;//设置显示位置为中间
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

    public void setList(List<String> list) {
        if (adapter == null) {
            adapter = new CoinInfoAdapter(getContext());
        }
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    private void initUI() {
        rvinfo = findViewById(R.id.rv_info);
        title = findViewById(R.id.title);
        view = findViewById(R.id.view);
        view.setOnClickListener(view -> {
            if (myDialog != null && myDialog.isShowing()) myDialog.dismiss();
        });
        title.setText("What are " + Constants.getInstance().getCoinName() + "？");
        initInfoRv();
    }

    private void initInfoRv() {
        if (adapter == null) {
            adapter = new CoinInfoAdapter(getContext());
        }
//        mAdapter1.setOnItemClickListener(this);
//        rvRecord.setLayoutManager(new GridLayoutManager(getContext(), 7, GridLayoutManager.VERTICAL, false));
        rvinfo.setAdapter(adapter);
        rvinfo.setNestedScrollingEnabled(false);
        adapter.setOnClickListener(new CoinInfoAdapter.OnClickListener() {
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
    public CoinInfoDialog setMessage(String strMessage) {
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
        public void onActionClick(String code);
    }

    /**
     * 接口回调
     */
    public void setDialogCallBack(DialogCallBack DialogCallBack) {
        mDialogCallBack = DialogCallBack;
    }

}
