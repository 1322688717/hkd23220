package cc.ixcc.novelthree.bean;
import java.util.ArrayList;
import java.util.List;

public class BuyCoinInfo {
    private int coin;
    private String CountryCode;
    private String CurrencyCode;
    private String money_symbol;

    private List<CoinListBean> coin_list;
    private List<String> paytips;
    public int getCoin() {
        return coin;
    }
    public void setCoin(int coin) {
        this.coin = coin;
    }
    public List<CoinListBean> getCoin_list() {
        return coin_list;
    }
    public List<String> getpaytips() {
        if (paytips!=null){
            return paytips;
        }else {
           return new ArrayList<String>();
        }
    }

    public String getCountryCode(){return CountryCode;}
    public void setCountryCode(String countryCode){CountryCode = countryCode;}
    public String getCurrencyCode(){return CurrencyCode;}
    public void setCurrencyCode(String currencyCode){CurrencyCode = currencyCode;}
    public String getMoney_symbol(){return money_symbol;}
    public void setMoney_symbol(String temp_money_symbol){money_symbol = temp_money_symbol;}

    public void setCoin_list(List<CoinListBean> coin_list) {
        this.coin_list = coin_list;
    }
    public void setpaytips(List<String> paytips) {
        this.paytips = paytips;
    }

    public static class CoinListBean {
        private int charge_index;
        private String payid;
        private String charge_rmb;
        private float coin;
        private float handsel;

        public void setChargeIndex(int index)
        {
            this.charge_index = index;
        }

        public int getChargeIndex()
        {
            return this.charge_index;
        }

        public float getCoin() {
            return coin;
        }
        public void setCoin(int coin) {
            this.coin = coin;
        }

        public String getRmb() {
            return charge_rmb;
        }
        public void setRmb(String rmb)
        {
            this.charge_rmb = rmb;
        }

        public String getpayid()
        {
            return payid;
        }
        public void setpayid(String payid)
        {
            this.payid = payid;
        }

        public float gethandsel()
        {
            return handsel;
        }

        public void sethandsel(float handsel) {
            this.handsel = handsel;
        }
    }
}
