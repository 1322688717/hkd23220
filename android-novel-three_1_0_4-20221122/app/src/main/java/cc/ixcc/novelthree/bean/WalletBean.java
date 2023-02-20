package cc.ixcc.novelthree.bean;

import java.util.List;

public class WalletBean {

    /**
     * now_coin : 248
     * withdraw_rate : 10000
     * now_coin_rmb : 0
     * today_coin : 36
     * history_coin : 336
     * coin_introduction : ["10,000书豆=1元，可兑换现金","每日阅读、签到或完成福利中心其他任务，均可获得书豆","每天阅读时间越久，获得的书豆越多","书豆无需转换为现金，可直接累计提现","书豆为某某小说的虚拟积分，与设备制作商Apple Inc.无关"]
     * finance : [{"id":10612,"user_id":19990,"anid":0,"chaps":0,"type":"签到奖励积分","value":36,"action":2,"created_at":"2020-07-10 07:21:49","updated_at":"2020-07-10 07:21:49"}]
     */

    private int now_coin;
    private String withdraw_rate;
    private double now_coin_rmb;
    private int today_coin;
    private int history_coin;
    private List<String> coin_introduction;
    private List<FinanceBean> finance;

    public int getNow_coin() {
        return now_coin;
    }

    public void setNow_coin(int now_coin) {
        this.now_coin = now_coin;
    }

    public String getWithdraw_rate() {
        return withdraw_rate;
    }

    public void setWithdraw_rate(String withdraw_rate) {
        this.withdraw_rate = withdraw_rate;
    }

    public double getNow_coin_rmb() {
        return now_coin_rmb;
    }

    public void setNow_coin_rmb(double now_coin_rmb) {
        this.now_coin_rmb = now_coin_rmb;
    }

    public int getToday_coin() {
        return today_coin;
    }

    public void setToday_coin(int today_coin) {
        this.today_coin = today_coin;
    }

    public int getHistory_coin() {
        return history_coin;
    }

    public void setHistory_coin(int history_coin) {
        this.history_coin = history_coin;
    }

    public List<String> getCoin_introduction() {
        return coin_introduction;
    }

    public void setCoin_introduction(List<String> coin_introduction) {
        this.coin_introduction = coin_introduction;
    }

    public List<FinanceBean> getFinance() {
        return finance;
    }

    public void setFinance(List<FinanceBean> finance) {
        this.finance = finance;
    }

    public static class FinanceBean {
        /**
         * id : 10612
         * user_id : 19990
         * anid : 0
         * chaps : 0
         * type : 签到奖励积分
         * value : 36
         * action : 2
         * created_at : 2020-07-10 07:21:49
         * updated_at : 2020-07-10 07:21:49
         */

        private int id;
        private int user_id;
        private int anid;
        private int chaps;
        private String type;
        private int value;
        private int action;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getAnid() {
            return anid;
        }

        public void setAnid(int anid) {
            this.anid = anid;
        }

        public int getChaps() {
            return chaps;
        }

        public void setChaps(int chaps) {
            this.chaps = chaps;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
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
    }
}
