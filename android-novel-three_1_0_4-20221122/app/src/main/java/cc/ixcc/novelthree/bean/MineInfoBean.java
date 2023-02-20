package cc.ixcc.novelthree.bean;

import java.util.List;

public class MineInfoBean {

    /**
     * read_time : 0
     * coin : 4709
     * today_coin : 0
     * list : [[{"id":"1","name":"邀请好友","icon":"http://qnlive.csvclub.cn/invite_friends.png","href":"http://dnn.jiusencms.com/h5/index#"},{"id":"2","name":"阅读喜好","icon":"http://qnlive.csvclub.cn/read_hobby.png","href":""}],[{"id":"3","name":"阅读记录","icon":"http://qnlive.csvclub.cn/read_record.png","href":""},{"id":"4","name":"我的书评","icon":"http://qnlive.csvclub.cn/my_comments.png","href":""},{"id":"5","name":"联系客服","icon":"http://qnlive.csvclub.cn/contact.png","href":"http://dnn.jiusencms.com/h5/index#/service"}]]
     */

    private int read_time;
    private int coin;
    private int today_coin;
    private String lucky_wheel_switch;
    private String viptime;
    private String is_vip;

    public String getLucky_wheel_switch() {
        return lucky_wheel_switch;
    }

    public void setLucky_wheel_switch(String lucky_wheel_switch) {
        this.lucky_wheel_switch = lucky_wheel_switch;
    }

    public String getViptime() {
        return viptime;
    }

    public void setViptime(String viptime) {
        this.viptime = viptime;
    }

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    private List<List<ListBean>> list;

    public int getRead_time() {
        return read_time;
    }

    public void setRead_time(int read_time) {
        this.read_time = read_time;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getToday_coin() {
        return today_coin;
    }

    public void setToday_coin(int today_coin) {
        this.today_coin = today_coin;
    }

    public List<List<ListBean>> getList() {
        return list;
    }

    public void setList(List<List<ListBean>> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * name : 邀请好友
         * icon : http://qnlive.csvclub.cn/invite_friends.png
         * href : http://dnn.jiusencms.com/h5/index#
         */

        private String id;
        private String name;
        private String icon;
        private String href;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
