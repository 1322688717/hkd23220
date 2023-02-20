package cc.ixcc.novelthree.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VipHomeBean {

    /**
     * vip_privilege : [{"title":"会员免广告","image":"http://qnlive.csvclub.cn/vip_1.png"},{"title":"听书免广告","image":"http://qnlive.csvclub.cn/vip_2.png"},{"title":"尊贵标识","image":"http://qnlive.csvclub.cn/vip_3.png"},{"title":"每月送书券","image":"http://qnlive.csvclub.cn/vip_4.png"},{"title":"免费书库","image":"http://qnlive.csvclub.cn/vip_5.png"},{"title":"签到特权","image":"http://qnlive.csvclub.cn/vip_6.png"}]
     * package : [{"id":1,"title":"7天","price":"6.00","original_price":"0.00","day":7,"sday":0,"apple_id":"jiusen_VIP_7"},{"id":2,"title":"14天","price":"12.00","original_price":"0.00","day":14,"sday":0,"apple_id":"jiusen_VIP_14"},{"id":3,"title":"30天","price":"25.00","original_price":"60.00","day":30,"sday":30,"apple_id":"jiusen_VIP_30"}]
     * wxpay_switch : 1
     * alipay_switch : 1
     */

    private String wxpay_switch;
    private String alipay_switch;
    private List<VipPrivilegeBean> vip_privilege;
    @SerializedName("package")
    private List<PackageBean> packageX;

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

    public List<VipPrivilegeBean> getVip_privilege() {
        return vip_privilege;
    }

    public void setVip_privilege(List<VipPrivilegeBean> vip_privilege) {
        this.vip_privilege = vip_privilege;
    }

    public List<PackageBean> getPackageX() {
        return packageX;
    }

    public void setPackageX(List<PackageBean> packageX) {
        this.packageX = packageX;
    }

    public static class VipPrivilegeBean {
        /**
         * title : 会员免广告
         * image : http://qnlive.csvclub.cn/vip_1.png
         */

        private String title;
        private String image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class PackageBean {
        /**
         * id : 1
         * title : 7天
         * price : 6.00
         * original_price : 0.00
         * day : 7
         * sday : 0
         * apple_id : jiusen_VIP_7
         */

        private int id;
        private String title;
        private String price;
        private String original_price;
        private int day;
        private int sday;
        private String apple_id;
        private boolean isSelect;
        private String CountryCode;
        private String CurrencyCode;
        private String money_symbol;

        public String getCountryCode(){return CountryCode;}
        public String getCurrencyCode(){return CurrencyCode;}
        public String getMoney_symbol(){return money_symbol;}

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(String original_price) {
            this.original_price = original_price;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getSday() {
            return sday;
        }

        public void setSday(int sday) {
            this.sday = sday;
        }

        public String getApple_id() {
            return apple_id;
        }

        public void setApple_id(String apple_id) {
            this.apple_id = apple_id;
        }
    }
}
