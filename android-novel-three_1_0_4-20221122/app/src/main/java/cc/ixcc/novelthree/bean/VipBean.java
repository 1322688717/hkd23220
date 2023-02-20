package cc.ixcc.novelthree.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VipBean {

    /**
     * vip_privilege : [{"title":"尊贵标识","content":"享有VIP专属头像标识，彰显尊贵"},{"title":"免费书库","content":"海量网文不断更，人气小说免费读"},{"title":"提现特权","content":"VIP会员大幅度提升提现资质"}]
     * package : [{"id":1,"price":"6.00","original_price":"0.00","day":7,"sday":null,"apple_id":""},{"id":2,"price":"13.00","original_price":"0.00","day":14,"sday":null,"apple_id":""},{"id":3,"price":"30.00","original_price":"0.00","day":30,"sday":30,"apple_id":""}]
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
         * title : 尊贵标识
         * content : 享有VIP专属头像标识，彰显尊贵
         */

        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class PackageBean {
        /**
         * id : 1
         * price : 6.00
         * original_price : 0.00
         * day : 7
         * sday : null
         * apple_id :
         */

        private int id;
        private String price;
        private String original_price;
        private int day;
        private String sday;
        private String apple_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getSday() {
            return sday;
        }

        public void setSday(String sday) {
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
