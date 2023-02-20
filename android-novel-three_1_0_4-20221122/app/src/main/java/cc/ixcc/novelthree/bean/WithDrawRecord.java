package cc.ixcc.novelthree.bean;

public class WithDrawRecord {

    /**
     * id : 2
     * coin : 100000
     * status : 1
     * created_at : 2020.07.01
     */

    private int id;
    private int coin;
    private int status;
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
