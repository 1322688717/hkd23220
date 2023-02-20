package cc.ixcc.novelthree.bean;

public class BindThirdBean {
    private int user_id;
    private String type;
    private String openid;
    private String avatar;
    private String nickname;
    private String status;
    private String meid;

    public String getMEID(){return meid;}
    public void setMEID(String tempMeid){meid = tempMeid;}

    public String getOpenID(){return openid;}
    public void setOpenID(String open_id){openid = open_id;}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
