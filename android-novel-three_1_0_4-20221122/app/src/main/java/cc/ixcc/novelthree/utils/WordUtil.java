package cc.ixcc.novelthree.utils;

import android.content.res.Resources;

import cc.ixcc.novelthree.treader.AppContext;

/**
 * Created by cxf on 2017/10/10.
 * 获取string.xml中的字
 */

public class WordUtil {

    private static Resources sResources;

    static {
        sResources = AppContext.sInstance.getResources();
    }

    public static String getString(int res) {
        return sResources.getString(res);
    }


    public static String getString(int res, Object...formatArgs) {
        return sResources.getString(res,formatArgs);
    }
}
