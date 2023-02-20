package cc.ixcc.novelthree.event;

/**
 * Created by cxf on 2018/11/26.
 */

public class AddCountEvent {
    int type;
    String name;
    String account;

    public AddCountEvent(int type, String name, String account) {
        this.type = type;
        this.name = name;
        this.account = account;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
