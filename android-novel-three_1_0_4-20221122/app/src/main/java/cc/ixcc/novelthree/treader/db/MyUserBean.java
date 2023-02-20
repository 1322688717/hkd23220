package cc.ixcc.novelthree.treader.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class MyUserBean extends DataSupport implements Serializable {

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    /**
     * id : 19990
     * mch : 0
     * phone : 18256240676
     * nickname : 笑
     * invite_code : null
     * remark_name :
     * gender : 1
     * avatar : http://qnlive.csvclub.cn/default_boy.png
     * coin : 212
     * totalcoin : 300
     * birthday : null
     * sign : 全世界我最帅
     * province_id : 0
     * city_id : 0
     * district_id : 0
     * status : 1
     * disable_time : null
     * disable_desc :
     * disable_length :
     * charge_total : 0
     * viptime : 0
     * autobuy : 0
     * change_pwd_time : 1592538005
     * isset_pwd : 1
     * login_time : 1594288364
     * login_ip : 36.57.184.186
     * delete_time : null
     * reg_way : third_weibo
     * meid :
     * system_switch : 1
     * created_at : 2020-06-19 03:14:19
     * updated_at : 2020-07-09 09:52:44
     * unregistered : 0
     * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9kbm4uaml1c2VuY21zLmNvbVwvYXBpXC9sb2dpbl9ieV90aGlyZCIsImlhdCI6MTU5NDI4ODM5MSwiZXhwIjoxNjI1ODI0MzkxLCJuYmYiOjE1OTQyODgzOTEsImp0aSI6IllIWFluU1VmZXZOeExEM3EiLCJzdWIiOjE5OTkwLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.svPVkPVrW-bCmUizMt2S5-DzmIDumESYW1pR-ZDByFE
     * token_type : bearer
     * expires_in : 31536000
     */

    private int id;
    private int mch;
    private String phone;
    private String nickname;
    private Object invite_code;
    private String remark_name;
    private String gender;
    private String avatar;
    private int coin;
    private int totalcoin;
    private Object birthday;
    private String sign;
    private int province_id;
    private int city_id;
    private int district_id;
    private String status;
    private Object disable_time;
    private String disable_desc;
    private String disable_length;
    private int charge_total;
    private int viptime;
    private int autobuy;
    private int change_pwd_time;
    private String isset_pwd;
    private int login_time;
    private String login_ip;
    private Object delete_time;
    private String reg_way;
    private String meid;
    private String system_switch;
    private String created_at;
    private String updated_at;
    private String is_vip;
    private String unregistered;
    private String access_token;
    private String token_type;
    private int expires_in;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMch() {
        return mch;
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

    public Object getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(Object invite_code) {
        this.invite_code = invite_code;
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

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
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

    public Object getDisable_time() {
        return disable_time;
    }

    public void setDisable_time(Object disable_time) {
        this.disable_time = disable_time;
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

    public int getCharge_total() {
        return charge_total;
    }

    public void setCharge_total(int charge_total) {
        this.charge_total = charge_total;
    }

    public int getViptime() {
        return viptime;
    }

    public void setViptime(int viptime) {
        this.viptime = viptime;
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

    public Object getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Object delete_time) {
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
}
