package cc.ixcc.novelthree.bean;

public class UserInfoBean {

    /**
     * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kbm4uaml1c2VuY21zLmNvbVwvYXBpXC9wd2RfbG9naW4iLCJpYXQiOjE1OTQyNzU4MjEsImV4cCI6MTYyNTgxMTgyMSwibmJmIjoxNTk0Mjc1ODIxLCJqdGkiOiJGNENiaUtEUU9TUTdjTXNQIiwic3ViIjoxMDM4MywicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.ts6Y7vDMBDF4baCNgTdxsWAXCwCYXF5ARbyOo7cLi1A
     * autobuy : 0
     * avatar : http://qnlive.csvclub.cn/android_10383_20200709_105325_7896946.jpg
     * charge_total : 0
     * city_id : 0
     * coin : 150
     * created_at : 2020-07-08 06:39:56
     * disable_desc :
     * disable_length :
     * district_id : 0
     * expires_in : 31536000
     * gender : 1
     * id : 10383
     * invite_code : 123
     * isset_pwd : 1
     * login_ip : 36.57.184.186
     * login_time : 1594275734
     * mch : 0
     * meid : a000006921ad64
     * nickname : 测试123
     * phone : 18356137671
     * province_id : 0
     * reg_way : pwd
     * remark_name :
     * sign : 呵呵哈哈呵呵
     * status : 1
     * system_switch : 1
     * token_type : bearer
     * totalcoin : 150
     * updated_at : 2020-07-09 06:22:14
     * viptime : 0
     */

    private String access_token;
    private int autobuy;
    private String avatar;
    private float charge_total;
    private int city_id;
    private int coin;
    private String created_at;
    private String disable_desc;
    private String disable_length;
    private int district_id;
    private int expires_in;
    private String gender;
    private int id;
    private String invite_code;
    private String isset_pwd;
    private String login_ip;
    private int login_time;
    private int mch;
    private String meid;
    private String nickname;
    private String phone;
    private int province_id;
    private String reg_way;
    private String remark_name;
    private String sign;
    private String status;
    private String system_switch;
    private String token_type;
    private int totalcoin;
    private String updated_at;
    private String viptime;
    private String hasuser = "-1";

    public String getHasUser()
    {
        return hasuser;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getAutobuy() {
        return autobuy;
    }

    public void setAutobuy(int autobuy) {
        this.autobuy = autobuy;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public float getCharge_total() {
        return charge_total;
    }

    public void setCharge_total(float charge_total) {
        this.charge_total = charge_total;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDisable_desc() {
        return disable_desc;
    }

    public void setDisable_desc(String disable_desc) {
        this.disable_desc = disable_desc;
    }

    public String getDisable_length() {
        return disable_length;
    }

    public void setDisable_length(String disable_length) {
        this.disable_length = disable_length;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getIsset_pwd() {
        return isset_pwd;
    }

    public void setIsset_pwd(String isset_pwd) {
        this.isset_pwd = isset_pwd;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public int getLogin_time() {
        return login_time;
    }

    public void setLogin_time(int login_time) {
        this.login_time = login_time;
    }

    public int getMch() {
        return mch;
    }

    public void setMch(int mch) {
        this.mch = mch;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getReg_way() {
        return reg_way;
    }

    public void setReg_way(String reg_way) {
        this.reg_way = reg_way;
    }

    public String getRemark_name() {
        return remark_name;
    }

    public void setRemark_name(String remark_name) {
        this.remark_name = remark_name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystem_switch() {
        return system_switch;
    }

    public void setSystem_switch(String system_switch) {
        this.system_switch = system_switch;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getTotalcoin() {
        return totalcoin;
    }

    public void setTotalcoin(int totalcoin) {
        this.totalcoin = totalcoin;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getViptime() {
        return viptime;
    }

    public void setViptime(String viptime) {
        this.viptime = viptime;
    }
}
