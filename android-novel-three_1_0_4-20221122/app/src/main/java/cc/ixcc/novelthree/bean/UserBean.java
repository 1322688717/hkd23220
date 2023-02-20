package cc.ixcc.novelthree.bean;

import android.os.Parcel;
import android.os.Parcelable;

//用户信息
public class UserBean implements Parcelable {
    private int id;
    private String openid;
    private int mch;
    private String phone;
    private String email;
    private String nickname;
    private String remark_name;
    private String gender;
    private String avatar;
    private int coin;
    private int totalcoin;
    private String birthday;
    private String sign;
    private int province_id;
    private int city_id;
    private int district_id;
    private String status;
    private String bind_status;
    private float charge_total;
    private String viptime;
    private int autobuy;
    private int change_pwd_time;
    private String isset_pwd;
    private int login_time;
    private String login_ip;
    private String delete_time;
    private String reg_way;
    private String meid;
    private String system_switch;
    private String issex;
    private String source;
    private String created_at;
    private String updated_at;
    private String is_vip;
    private String unregistered;
    private int copyright;//0非购买用户显示部分图书   -1购买用户 显示所有图书

    public void setOpenid(String open_id){openid = open_id;}
    public String getOpenid(){return openid;}

    public void setBindStatus(String status) {bind_status = status;}
    public String getBindStatus(){return bind_status;}

    public String getEmail(){
        return email;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public int getId() { return id; }
    public void setId(int tempID){id = tempID;}

    public int getMch() {
        return mch;
    }

    public String getViptime() {
        return viptime;
    }

    public void setViptime(String viptime) {
        this.viptime = viptime;
    }

    public void setMch(int mch) {
        this.mch = mch;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark_name() {
        return remark_name;
    }

    public void setRemark_name(String remark_name) {
        this.remark_name = remark_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getTotalcoin() {
        return totalcoin;
    }

    public void setTotalcoin(int totalcoin) {
        this.totalcoin = totalcoin;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getCharge_total() {
        return charge_total;
    }

    public void setCharge_total(float charge_total) {
        this.charge_total = charge_total;
    }


    public int getAutobuy() {
        return autobuy;
    }

    public void setAutobuy(int autobuy) {
        this.autobuy = autobuy;
    }

    public int getChange_pwd_time() {
        return change_pwd_time;
    }

    public void setChange_pwd_time(int change_pwd_time) {
        this.change_pwd_time = change_pwd_time;
    }

    public String getIsset_pwd() {
        return isset_pwd;
    }

    public void setIsset_pwd(String isset_pwd) {
        this.isset_pwd = isset_pwd;
    }

    public int getLogin_time() {
        return login_time;
    }

    public void setLogin_time(int login_time) {
        this.login_time = login_time;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getReg_way() {
        return reg_way;
    }

    public void setReg_way(String reg_way) {
        this.reg_way = reg_way;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getSystem_switch() {
        return system_switch;
    }

    public void setSystem_switch(String system_switch) {
        this.system_switch = system_switch;
    }

    public String getIssex() {
        return issex;
    }

    public void setIssex(String issex) {
        this.issex = issex;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    public String getUnregistered() {
        return unregistered;
    }

    public void setUnregistered(String unregistered) {
        this.unregistered = unregistered;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    private String access_token;
    private String token_type;
    private int expires_in;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.openid);
        dest.writeInt(this.mch);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.nickname);
        dest.writeString(this.remark_name);
        dest.writeString(this.gender);
        dest.writeString(this.avatar);
        dest.writeInt(this.coin);
        dest.writeInt(this.totalcoin);
        dest.writeString(this.birthday);
        dest.writeString(this.sign);
        dest.writeInt(this.province_id);
        dest.writeInt(this.city_id);
        dest.writeInt(this.district_id);
        dest.writeString(this.status);
        dest.writeString(this.bind_status);
        dest.writeFloat(this.charge_total);
        dest.writeString(this.viptime);
        dest.writeInt(this.autobuy);
        dest.writeInt(this.change_pwd_time);
        dest.writeString(this.isset_pwd);
        dest.writeInt(this.login_time);
        dest.writeString(this.login_ip);
        dest.writeString(this.delete_time);
        dest.writeString(this.reg_way);
        dest.writeString(this.meid);
        dest.writeString(this.system_switch);
        dest.writeString(this.issex);
        dest.writeString(this.source);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.is_vip);
        dest.writeString(this.unregistered);
        dest.writeString(this.access_token);
        dest.writeString(this.token_type);
        dest.writeInt(this.expires_in);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.id = in.readInt();
        this.openid = in.readString();
        this.mch = in.readInt();
        this.phone = in.readString();
        this.email = in.readString();
        this.nickname = in.readString();
        this.remark_name = in.readString();
        this.gender = in.readString();
        this.avatar = in.readString();
        this.coin = in.readInt();
        this.totalcoin = in.readInt();
        this.birthday = in.readString();
        this.sign = in.readString();
        this.province_id = in.readInt();
        this.city_id = in.readInt();
        this.district_id = in.readInt();
        this.status = in.readString();
        this.bind_status = in.readString();
        this.charge_total = in.readInt();
        this.viptime = in.readString();
        this.autobuy = in.readInt();
        this.change_pwd_time = in.readInt();
        this.isset_pwd = in.readString();
        this.login_time = in.readInt();
        this.login_ip = in.readString();
        this.delete_time = in.readString();
        this.reg_way = in.readString();
        this.meid = in.readString();
        this.system_switch = in.readString();
        this.issex = in.readString();
        this.source = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.is_vip = in.readString();
        this.unregistered = in.readString();
        this.access_token = in.readString();
        this.token_type = in.readString();
        this.expires_in = in.readInt();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}