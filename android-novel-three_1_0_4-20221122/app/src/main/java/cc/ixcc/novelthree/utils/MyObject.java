package cc.ixcc.novelthree.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.bean.ConfigBean;
import cc.ixcc.novelthree.interfaces.CommonCallback;
import com.jiusen.base.BaseDialog;

public class MyObject {
    Context context;
    ConfigBean mConfigBean;
    BaseDialog mInviteDialog;

    public MyObject(Context context) {
        this.context = context;
    }

    //设置类的暴露接口:
    @JavascriptInterface
    public void hiddenInvite() {
        if (mInviteDialog != null && mInviteDialog.isShowing())
            mInviteDialog.dismiss();
    }

    @JavascriptInterface
    public void invite() {
        Constants.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean bean) {
                mConfigBean = bean;
                if (mConfigBean != null) {
                    share();
                }
            }
        });
    }

    private void share() {
    }
}
