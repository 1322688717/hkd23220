package cc.ixcc.novelthree.ui.dialog;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.core.view.GravityCompat;

import com.jiusen.base.BaseDialog;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.SingleClick;

/**

 *    time   : 2018/12/2
 *    desc   : 消息对话框
 */
public final class MessageDialog {

    public static final class Builder
            extends UIDialog.Builder<Builder> {

        private OnListener mListener;

        private final TextView mMessageView;

        private boolean isSpan = false;
        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.dialog_message);
            mMessageView = findViewById(R.id.tv_message_message);
        }

        public Builder setMessage(@StringRes int id) {
            return setMessage(getString(id));
        }
        public Builder setMessage(CharSequence text) {
            mMessageView.setText(text);
            return this;
        }

        public Builder setSpan(boolean b){
            isSpan = b;
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @Override
        public BaseDialog create() {
            if (isSpan) {
                mMessageView.setMovementMethod(LinkMovementMethod.getInstance());
                mMessageView.setGravity(GravityCompat.START);
            }
            // 如果内容为空就抛出异常
            if ("".equals(mMessageView.getText().toString())) {
                throw new IllegalArgumentException("Dialog message not null");
            }
            return super.create();
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ui_confirm:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onConfirm(getDialog());
                    }
                    break;
                case R.id.tv_ui_cancel:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }
}