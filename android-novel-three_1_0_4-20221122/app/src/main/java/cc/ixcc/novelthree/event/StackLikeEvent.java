package cc.ixcc.novelthree.event;

/**
 * Created by cxf on 2018/11/26.
 */

public class StackLikeEvent {
    boolean isLike;

    public StackLikeEvent(boolean isLike) {
        this.isLike = isLike;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
