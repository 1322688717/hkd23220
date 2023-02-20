package cc.ixcc.novelthree.bean;

import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/19.
 */

public class MobBean {

    private String mType;
    private int mIcon1;
    private int mIcon2;
    private String mName;
    private boolean mChecked;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getIcon1() {
        return mIcon1;
    }

    public void setIcon1(int icon1) {
        mIcon1 = icon1;
    }

    public int getIcon2() {
        return mIcon2;
    }

    public void setIcon2(int icon2) {
        mIcon2 = icon2;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }


    /**
     * 登录类型
     */
    public static List<MobBean> getLoginTypeList(String[] types) {
        List<MobBean> list = new ArrayList<>();
        if (types != null) {
            for (String type : types) {
                MobBean bean = new MobBean();
                bean.setType(type);
                switch (type) {
                    case Constants.MOB_QQ:
                        bean.setIcon1(R.mipmap.ic_qq);
                        break;
                    case Constants.MOB_WX:
                        bean.setIcon1(R.mipmap.ic_wx);
                        break;
//                    case Constants.MOB_FACEBOOK:
//                        bean.setIcon1(R.mipmap.icon_login_fb);
//                        break;
//                    case Constants.MOB_TWITTER:
//                        bean.setIcon1(R.mipmap.icon_login_tt);
//                        break;
                }
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 分享类型
     */
    public static List<MobBean> getShareTypeList(String[] types) {
        List<MobBean> list = new ArrayList<>();
//        if (types != null) {
//            for (String type : types) {
//                MobBean bean = new MobBean();
//                bean.setType(type);
//                switch (type) {
//                    case Constants.MOB_QQ:
//                        bean.setIcon1(R.mipmap.icon_share_qq_3);
//                        bean.setName(R.string.mob_qq);
//                        break;
//                    case Constants.MOB_QZONE:
//                        bean.setIcon1(R.mipmap.icon_share_qzone_3);
//                        bean.setName(R.string.mob_qzone);
//                        break;
//                    case Constants.MOB_WX:
//                        bean.setIcon1(R.mipmap.icon_share_wx_3);
//                        bean.setName(R.string.mob_wx);
//                        break;
//                    case Constants.MOB_WX_PYQ:
//                        bean.setIcon1(R.mipmap.icon_share_pyq_3);
//                        bean.setName(R.string.mob_wx_pyq);
//                        break;
//                    case Constants.MOB_FACEBOOK:
//                        bean.setIcon1(R.mipmap.icon_share_fb_3);
//                        bean.setName(R.string.mob_fb);
//                        break;
//                    case Constants.MOB_TWITTER:
//                        bean.setIcon1(R.mipmap.icon_share_tt_3);
//                        bean.setName(R.string.mob_tt);
//                        break;
//                }
//                list.add(bean);
//            }
//        }
        return list;
    }

}
