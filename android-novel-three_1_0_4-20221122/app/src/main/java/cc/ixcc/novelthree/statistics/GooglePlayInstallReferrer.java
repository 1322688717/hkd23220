package cc.ixcc.novelthree.statistics;

import android.content.Context;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;


/**
 * desc   : 谷歌安装数据
 */

public class GooglePlayInstallReferrer {
    private String TAG="GooglePlayInstallReferrerlog";
    private Context activity;

    private static GooglePlayInstallReferrer instance;

    public static GooglePlayInstallReferrer GetInstance() {
        if (instance == null) {
            instance = new GooglePlayInstallReferrer();
        }
        return instance;
    }

    public void GooglePlayInstallReferrerInit(Context activity) {
        this.activity = activity;

        InstallReferrerClient referrerClient;

        referrerClient = InstallReferrerClient.newBuilder(this.activity).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.
                        Log.i(TAG, "服务连接成功");

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.

                        Log.e(TAG, "app不支持该API");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.

                        Log.e(TAG, "连接失败");
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

    }

}
