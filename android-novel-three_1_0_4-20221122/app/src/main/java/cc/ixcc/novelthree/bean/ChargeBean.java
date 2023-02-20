package cc.ixcc.novelthree.bean;

import java.util.List;

public class ChargeBean {

    /**
     * user_data : {"coin":800000,"user_info":{"title":"普通用户","viptime":"未开通VIP","vip_type":0}}
     * normal : [{"id":1,"money":"0.01","smoney":0,"lv":"100.00","desc":"","ishot":"1","ismonth":"0","isyear":"0","ishalf":"0","isover":"0","isopen":"1","ischoose":"0","isonce":"1","delete_time":null,"created_at":null,"updated_at":null},{"id":2,"money":"50.00","smoney":60,"lv":"100.00","desc":"","ishot":"1","ismonth":"0","isyear":"0","ishalf":"0","isover":"0","isopen":"1","ischoose":"0","isonce":"1","delete_time":null,"created_at":null,"updated_at":null},{"id":3,"money":"100.00","smoney":90,"lv":"100.00","desc":null,"ishot":"0","ismonth":"0","isyear":"0","ishalf":"0","isover":"0","isopen":"1","ischoose":"0","isonce":"0","delete_time":null,"created_at":null,"updated_at":null},{"id":4,"money":"200.00","smoney":120,"lv":"100.00","desc":null,"ishot":"0","ismonth":"0","isyear":"0","ishalf":"0","isover":"0","isopen":"1","ischoose":"1","isonce":"0","delete_time":null,"created_at":null,"updated_at":null}]
     * vip : [{"id":5,"money":"80.00","smoney":0,"lv":"0.00","desc":"包月VIP免费看","ishot":"0","ismonth":"1","isyear":"0","ishalf":"0","isover":"0","isopen":"1","ischoose":"0","isonce":"0","delete_time":null,"created_at":null,"updated_at":null},{"id":6,"money":"365.00","smoney":0,"lv":"0.00","desc":"包年VIP免费看","ishot":"0","ismonth":"0","isyear":"1","ishalf":"0","isover":"0","isopen":"1","ischoose":"0","isonce":"0","delete_time":null,"created_at":null,"updated_at":null}]
     * wxpay_switch : 1
     * alipay_switch : 1
     */

    private UserDataBean user_data;
    private String wxpay_switch;
    private String alipay_switch;
    private List<NormalBean> normal;
    private List<NormalBean> vip;

    public UserDataBean getUser_data() {
        return user_data;
    }

    public void setUser_data(UserDataBean user_data) {
        this.user_data = user_data;
    }

    public String getWxpay_switch() {
        return wxpay_switch;
    }

    public void setWxpay_switch(String wxpay_switch) {
        this.wxpay_switch = wxpay_switch;
    }

    public String getAlipay_switch() {
        return alipay_switch;
    }

    public void setAlipay_switch(String alipay_switch) {
        this.alipay_switch = alipay_switch;
    }

    public List<NormalBean> getNormal() {
        return normal;
    }

    public void setNormal(List<NormalBean> normal) {
        this.normal = normal;
    }

    public List<NormalBean> getVip() {
        return vip;
    }

    public void setVip(List<NormalBean> vip) {
        this.vip = vip;
    }

    public static class UserDataBean {
        /**
         * coin : 800000
         * user_info : {"title":"普通用户","viptime":"未开通VIP","vip_type":0}
         */

        private int coin;
        private UserInfoBean user_info;

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public UserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean user_info) {
            this.user_info = user_info;
        }

        public static class UserInfoBean {
            /**
             * title : 普通用户
             * viptime : 未开通VIP
             * vip_type : 0
             */

            private String title;
            private String viptime;
            private int vip_type;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getViptime() {
                return viptime;
            }

            public void setViptime(String viptime) {
                this.viptime = viptime;
            }

            public int getVip_type() {
                return vip_type;
            }

            public void setVip_type(int vip_type) {
                this.vip_type = vip_type;
            }
        }
    }

    public static class NormalBean {
        /**
         * id : 1
         * money : 0.01
         * smoney : 0
         * lv : 100.00
         * desc :
         * ishot : 1
         * ismonth : 0
         * isyear : 0
         * ishalf : 0
         * isover : 0
         * isopen : 1
         * ischoose : 0
         * isonce : 1
         * delete_time : null
         * created_at : null
         * updated_at : null
         */

        private int id;
        private String money;
        private String smoney;
        private String lv;
        private String desc;
        private String ishot;
        private String ismonth;
        private String isyear;
        private String ishalf;
        private String isover;
        private String isopen;
        private String ischoose;
        private String isonce;
        private Object delete_time;
        private Object created_at;
        private Object updated_at;
        private int coin;
        private int scoin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSmoney() {
            return smoney;
        }

        public void setSmoney(String smoney) {
            this.smoney = smoney;
        }

        public String getLv() {
            return lv;
        }

        public void setLv(String lv) {
            this.lv = lv;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIshot() {
            return ishot;
        }

        public void setIshot(String ishot) {
            this.ishot = ishot;
        }

        public String getIsmonth() {
            return ismonth;
        }

        public void setIsmonth(String ismonth) {
            this.ismonth = ismonth;
        }

        public String getIsyear() {
            return isyear;
        }

        public void setIsyear(String isyear) {
            this.isyear = isyear;
        }

        public String getIshalf() {
            return ishalf;
        }

        public void setIshalf(String ishalf) {
            this.ishalf = ishalf;
        }

        public String getIsover() {
            return isover;
        }

        public void setIsover(String isover) {
            this.isover = isover;
        }

        public String getIsopen() {
            return isopen;
        }

        public void setIsopen(String isopen) {
            this.isopen = isopen;
        }

        public String getIschoose() {
            return ischoose;
        }

        public void setIschoose(String ischoose) {
            this.ischoose = ischoose;
        }

        public String getIsonce() {
            return isonce;
        }

        public void setIsonce(String isonce) {
            this.isonce = isonce;
        }

        public Object getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(Object delete_time) {
            this.delete_time = delete_time;
        }

        public Object getCreated_at() {
            return created_at;
        }

        public void setCreated_at(Object created_at) {
            this.created_at = created_at;
        }

        public Object getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(Object updated_at) {
            this.updated_at = updated_at;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getScoin() {
            return scoin;
        }

        public void setScoin(int scoin) {
            this.scoin = scoin;
        }
    }

    public static class VipBean {
        /**
         * id : 5
         * money : 80.00
         * smoney : 0
         * lv : 0.00
         * desc : 包月VIP免费看
         * ishot : 0
         * ismonth : 1
         * isyear : 0
         * ishalf : 0
         * isover : 0
         * isopen : 1
         * ischoose : 0
         * isonce : 0
         * delete_time : null
         * created_at : null
         * updated_at : null
         */

        private int id;
        private String money;
        private int smoney;
        private String lv;
        private String desc;
        private String ishot;
        private String ismonth;
        private String isyear;
        private String ishalf;
        private String isover;
        private String isopen;
        private String ischoose;
        private String isonce;
        private Object delete_time;
        private Object created_at;
        private Object updated_at;
        private String day;
        private String sday;
        private String oldmoney;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getSmoney() {
            return smoney;
        }

        public void setSmoney(int smoney) {
            this.smoney = smoney;
        }

        public String getLv() {
            return lv;
        }

        public void setLv(String lv) {
            this.lv = lv;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIshot() {
            return ishot;
        }

        public void setIshot(String ishot) {
            this.ishot = ishot;
        }

        public String getIsmonth() {
            return ismonth;
        }

        public void setIsmonth(String ismonth) {
            this.ismonth = ismonth;
        }

        public String getIsyear() {
            return isyear;
        }

        public void setIsyear(String isyear) {
            this.isyear = isyear;
        }

        public String getIshalf() {
            return ishalf;
        }

        public void setIshalf(String ishalf) {
            this.ishalf = ishalf;
        }

        public String getIsover() {
            return isover;
        }

        public void setIsover(String isover) {
            this.isover = isover;
        }

        public String getIsopen() {
            return isopen;
        }

        public void setIsopen(String isopen) {
            this.isopen = isopen;
        }

        public String getIschoose() {
            return ischoose;
        }

        public void setIschoose(String ischoose) {
            this.ischoose = ischoose;
        }

        public String getIsonce() {
            return isonce;
        }

        public void setIsonce(String isonce) {
            this.isonce = isonce;
        }

        public Object getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(Object delete_time) {
            this.delete_time = delete_time;
        }

        public Object getCreated_at() {
            return created_at;
        }

        public void setCreated_at(Object created_at) {
            this.created_at = created_at;
        }

        public Object getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(Object updated_at) {
            this.updated_at = updated_at;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getSday() {
            return sday;
        }

        public void setSday(String sday) {
            this.sday = sday;
        }

        public String getOldmoney() {
            return oldmoney;
        }

        public void setOldmoney(String oldmoney) {
            this.oldmoney = oldmoney;
        }
    }
}
