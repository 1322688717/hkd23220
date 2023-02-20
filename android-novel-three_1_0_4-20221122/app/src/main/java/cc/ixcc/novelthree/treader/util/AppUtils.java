package cc.ixcc.novelthree.treader.util;

import static org.litepal.util.BaseUtility.capitalize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import cc.ixcc.novelthree.jsReader.utils.LogUtils;
import cc.ixcc.novelthree.utils.SpUtil;
import com.tencent.mmkv.MMKV;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AppUtils {
    public static boolean isRandomIMEID = false;
    public static boolean isLogin() {
        String token = MMKV.defaultMMKV().decodeString(SpUtil.TOKEN, "");
        if (TextUtils.isEmpty(token)) { //未登录
            return false;
        }
        return true;
    }

    public static String getDeviceModelName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    /*
     * 获取设备id
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        String deviceId = null;
        try {

            //IMEI
            deviceId = getIMEI(context);
            if (deviceId != null) {
                return deviceId;
            }

            //android ID
            deviceId = getAndroidID(context);
            if (deviceId != null) {
                return deviceId;
            }

            deviceId = getUUID();
            if (deviceId != null) {
                return deviceId;
            }

            Random r = new Random();
            deviceId = String.valueOf(r.nextInt(100000));
            isRandomIMEID = true;
            return deviceId;
        } catch (Throwable throwable) {
            LogUtils.e("TAG", "apputil error! " + throwable);

            Random r = new Random();
            deviceId = String.valueOf(r.nextInt(100000));
            isRandomIMEID = true;
            return deviceId;
        }
    }

    //IMEI
    public static String getIMEI(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return null;
            }
            @SuppressLint({"MissingPermission", "HardwareIds"}) String imei = telephonyManager.getDeviceId();
            return imei;
        } catch (Exception e) {
            return null;
        }
    }

    //MAC地址
    public String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //ANDROID ID
    public static String getAndroidID(Context context){
        String androidId = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        return androidId;
    }

    //生成唯一码
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
