package cc.ixcc.novelthree.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ConfigBean implements Parcelable {

    /**
     * login_type : [""]
     * share_type : ["qq","wx"]
     * shelve_style : 0
     * is_rel : 1
     * user_agreement : http://dnn.jiusencms.com/h5/index#/agreement
     * user_terms : http://dnn.jiusencms.com/h5/index#/userterms
     * invite_friends : http://dnn.jiusencms.com/h5/index#
     * share : http://dnn.jiusencms.com/share/index?share_uid=
     * sharepic : http://qnlive.csvclub.cn/admin/1/store/files/20200723/2e8640570cdc80e07ab9a9e945f41f34.png
     * sharetitle : 分享送书豆啦
     * sharedescribe : 邀请新用户注册还可享受更多好礼，等你来拿
     */

    private String shelve_style;
    private String is_rel;
    private String user_agreement;
    private String user_terms;
    private String invite_friends;
    private String share;
    private String sharepic;
    private String sharetitle;
    private String sharedescribe;

    private String is_adv;
    private String coin_name;

    private String android_download_page;
    private String android_version_code;
    private String android_version;
    private String android_version_must;
    private String android_version_desc;
    public String getIs_adv() {
        return is_adv;
    }
    public void setIs_adv(String is_adv) {
        this.is_adv = is_adv;
    }
    public static Creator<ConfigBean> getCREATOR() {
        return CREATOR;
    }

    private List<String> login_type;
    private List<String> share_type;

    public String getAndroid_download_page() {
        return android_download_page;
    }

    public void setAndroid_download_page(String android_download_page) {
        this.android_download_page = android_download_page;
    }

    public String getAndroid_version_code() {
        return android_version_code;
    }

    public void setAndroid_version_code(String android_version_code) {
        this.android_version_code = android_version_code;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getAndroid_version_must() {
        return android_version_must;
    }

    public void setAndroid_version_must(String android_version_must) {
        this.android_version_must = android_version_must;
    }

    public String getAndroid_version_desc() {
        return android_version_desc;
    }

    public void setAndroid_version_desc(String android_version_desc) {
        this.android_version_desc = android_version_desc;
    }

    public String getShelve_style() {
        return shelve_style;
    }

    public void setShelve_style(String shelve_style) {
        this.shelve_style = shelve_style;
    }

    public String getIs_rel() {
        return is_rel;
    }

    public void setIs_rel(String is_rel) {
        this.is_rel = is_rel;
    }

    public String getUser_agreement() {
        return user_agreement;
    }

    public void setUser_agreement(String user_agreement) {
        this.user_agreement = user_agreement;
    }

    public String getUser_terms() {
        return user_terms;
    }

    public void setUser_terms(String user_terms) {
        this.user_terms = user_terms;
    }

    public String getInvite_friends() {
        return invite_friends;
    }

    public void setInvite_friends(String invite_friends) {
        this.invite_friends = invite_friends;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getSharepic() {
        return sharepic;
    }

    public void setSharepic(String sharepic) {
        this.sharepic = sharepic;
    }

    public String getSharetitle() {
        return sharetitle;
    }

    public void setSharetitle(String sharetitle) {
        this.sharetitle = sharetitle;
    }

    public String getSharedescribe() {
        return sharedescribe;
    }

    public void setSharedescribe(String sharedescribe) {
        this.sharedescribe = sharedescribe;
    }

    public String getCoin_name() {
        return coin_name;
    }

    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
    }

    public List<String> getLogin_type() {
        return login_type;
    }

    public void setLogin_type(List<String> login_type) {
        this.login_type = login_type;
    }

    public List<String> getShare_type() {
        return share_type;
    }

    public void setShare_type(List<String> share_type) {
        this.share_type = share_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shelve_style);
        dest.writeString(this.is_rel);
        dest.writeString(this.user_agreement);
        dest.writeString(this.user_terms);
        dest.writeString(this.invite_friends);
        dest.writeString(this.share);
        dest.writeString(this.sharepic);
        dest.writeString(this.sharetitle);
        dest.writeString(this.sharedescribe);
        dest.writeString(this.coin_name);
        dest.writeStringList(this.login_type);
        dest.writeStringList(this.share_type);
    }

    public ConfigBean() {
    }

    protected ConfigBean(Parcel in) {
        this.shelve_style = in.readString();
        this.is_rel = in.readString();
        this.user_agreement = in.readString();
        this.user_terms = in.readString();
        this.invite_friends = in.readString();
        this.share = in.readString();
        this.sharepic = in.readString();
        this.sharetitle = in.readString();
        this.sharedescribe = in.readString();
        this.coin_name = in.readString();
        this.login_type = in.createStringArrayList();
        this.share_type = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ConfigBean> CREATOR = new Parcelable.Creator<ConfigBean>() {
        @Override
        public ConfigBean createFromParcel(Parcel source) {
            return new ConfigBean(source);
        }

        @Override
        public ConfigBean[] newArray(int size) {
            return new ConfigBean[size];
        }
    };
}
