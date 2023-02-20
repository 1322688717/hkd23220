package cc.ixcc.novelthree.event;

/**
 * Created by cxf on 2018/11/26.
 */

public class BookLikeEvent {
    String is_admire;

    public BookLikeEvent(String is_admire) {
        this.is_admire = is_admire;
    }

    public String getIs_admire() {
        return is_admire;
    }

    public void setIs_admire(String is_admire) {
        this.is_admire = is_admire;
    }
}
