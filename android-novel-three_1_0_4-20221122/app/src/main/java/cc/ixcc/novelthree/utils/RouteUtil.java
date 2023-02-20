package cc.ixcc.novelthree.utils;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by cxf on 2019/2/25.
 */
public class RouteUtil {
    //Intent隐式启动 action
    public static final String PATH_LAUNCHER = "/app/LauncherActivity";
    public static final String PATH_MESSAGE = "/app/MyMessageActivity";
    public static final String PATH_LOGIN_INVALID = "/main/MyLoginActivity";

    /**
     * 登录过期
     */
    public static void forwardLoginInvalid(String tip) {
        ARouter.getInstance().build(PATH_LOGIN_INVALID)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .navigation();
    }

    /**
     * 启动页
     */
    public static void forwardLauncher(Context context) {
        ARouter.getInstance().build(PATH_LAUNCHER)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .navigation();
    }

    /**
     * 消息页
     */
    public static void forwardMessage(Context context) {
        ARouter.getInstance().build(PATH_MESSAGE)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .navigation();
    }
}
