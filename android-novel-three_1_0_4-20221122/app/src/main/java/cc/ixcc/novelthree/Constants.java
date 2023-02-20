package cc.ixcc.novelthree;

import android.os.Environment;

import com.google.gson.Gson;
import cc.ixcc.novelthree.bean.ConfigBean;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.interfaces.CommonCallback;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.utils.SpUtil;
import com.tencent.mmkv.MMKV;

/**
 * Created by cxf on 2018/6/7.
 */

public class Constants {
    public static  boolean IV_SHOW_LISTERVIEW = false;

    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String LANGUAGE = "language";

    public static final String MOB_QQ = "qq";
    public static final String MOB_WX = "wx";

    //MMKV 储存Key
    public static final String TOKEN = "token";
    public static final String USER_INFO = "userInfo";
    public static final String CONFIG = "config";
    public static final int lianzai = 1;
    public static final String more_xinshu = "3"; //new:0

    public static final int manhua = 1;
    public static final int xiaoshuo = 2;
    public static final int boy = 1;
    public static final int girl = 2;
    public static final int PAGE_SIZE = 15;
    public static final int WX_PAY = 1;  //微信支付
    public static final int MORE_VISIBLE = 1;
    public static final int STACK_AD = 1; //广告
    public static final int SHELVE1 = 0;
    public static final int SHELVE2 = 1;
    public static final int SHELVE_SORT_READ = 0;
    public static final int SHELVE_SORT_UPDATE = 1;

    public static final String SHELVE_EXIST = "1";
    public static final String SHELVE_NO_EXIST = "0";

    public static final String COMMENT_NEW = "0";
    public static final String COMMENT_RECOMMEND = "1";

    public static final String UNLIKE = "0";
    public static final String LIKE = "1";

    public static final String TYPE_NOTICE = "notice";
    public static final String TYPE_LIKE = "comment_like";
    public static final String TYPE_COMMENT = "comment";

    public static final String CAT_TYPE_NOTICE = "system";
    public static final String CAT_TYPE_LIKE = "like";
    public static final String CAT_TYPE_COMMENT = "comment";

    public static final int TYPE_WX = 1;
    public static final int TYPE_ZFB = 2;

    public static final int WITHDRAW_VERIFY = 0;
    public static final int WITHDRAW_SUCCESS = 1;
    public static final int WITHDRAW_FAIL = 2;

    //外部sd卡
    public static final String DCMI_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    //内部存储 /data/data/<application package>/files目录
    public static final String INNER_PATH = AppContext.sInstance.getFilesDir().getAbsolutePath();
    public static final String LAST_CENSUS = "last_census";
    public static final String HOT_START = "hot_start";
    public static final String INVITE = "INVITE";
    //文件夹名字
    private static final String DIR_NAME = "novel";
    //拍照时图片保存路径
    public static final String CAMERA_IMAGE_PATH = DCMI_PATH + "/" + DIR_NAME + "/camera/";
    //log保存路径
    public static final String LOG_PATH = DCMI_PATH + "/" + DIR_NAME + "/log/";
    public static String isAgree = "ISAGREE";

    private ConfigBean mConfig;
    private static Constants sInstance;

    private boolean mLaunched;//App是否启动了
    private boolean mFrontGround;

    public static Constants getInstance() {
        if (sInstance == null) {
            synchronized (Constants.class) {
                if (sInstance == null) {
                    sInstance = new Constants();
                }
            }
        }
        return sInstance;
    }

    public void setConfig(ConfigBean config) {
        mConfig = config;
    }

    public String getCoinName() {
        if (mConfig != null) return mConfig.getCoin_name();
        return "";
    }

    public ConfigBean getConfig() {
        if (mConfig == null) {
            mConfig = MMKV.defaultMMKV().decodeParcelable(CONFIG,ConfigBean.class);
        }
        return mConfig;
    }

    public void getConfig(CommonCallback<ConfigBean> callback) {
        if (callback == null) {
            return;
        }
        ConfigBean configBean = getConfig();
        if (configBean != null) {
            callback.callback(configBean);
        } else {
            getAppInfo(callback);
        }
    }

    private void getAppInfo(final CommonCallback<ConfigBean> commonCallback) {
        HttpClient.getInstance().post(AllApi.appinit, AllApi.appinit)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
//                        ToastUtils.show(msg);
                        ConfigBean config = new Gson().fromJson(info[0], ConfigBean.class);
                        Constants.getInstance().setConfig(config);
//                        MMKV.defaultMMKV().encode(SpUtil.CONFIG, info[0]);
                        MMKV.defaultMMKV().encode(SpUtil.CONFIG, config);
                        if (commonCallback != null) {
                            commonCallback.callback(config);
                        }
                    }
                });
    }

    public boolean isLaunched() {
        return mLaunched;
    }

    public void setLaunched(boolean launched) {
        mLaunched = launched;
    }

    //app是否在前台
    public boolean isFrontGround() {
        return mFrontGround;
    }

    //app是否在前台
    public void setFrontGround(boolean frontGround) {
        mFrontGround = frontGround;
    }
}
