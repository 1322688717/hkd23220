package cc.ixcc.novelthree.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import cc.ixcc.novelthree.R;

/**
 * Created by cxf on 2017/8/8.
 */

public class DialogUitl {
    //第三方登录的时候用显示的dialog
//    public static Dialog loginAuthDialog(Context context) {
//        Dialog dialog = new Dialog(context, R.style.dialog);
//        dialog.setContentView(R.layout.dialog_login_loading);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        return dialog;
//    }

    /**
     * 用于网络请求等耗时操作的LoadingDialog
     */
    public static Dialog loadingDialog(Context context, String text) {
        Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(text)) {
            TextView titleView = (TextView) dialog.findViewById(R.id.text);
            if (titleView != null) {
                titleView.setText(text);
            }
        }
        return dialog;
    }
}
