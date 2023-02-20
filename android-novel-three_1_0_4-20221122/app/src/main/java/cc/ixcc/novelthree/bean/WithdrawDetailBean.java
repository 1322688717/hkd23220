package cc.ixcc.novelthree.bean;

public class WithdrawDetailBean {

    /**
     * money : 10.00
     * status : 0
     * type : 1
     * account : 5854612
     * created_at : 2020.07.10
     */

    private String money;
    private int status;
    private int type;
    private String account;
    private String created_at;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
