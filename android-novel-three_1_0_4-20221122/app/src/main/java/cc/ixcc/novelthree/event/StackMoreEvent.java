package cc.ixcc.novelthree.event;

/**
 * Created by cxf on 2018/11/26.
 */

public class StackMoreEvent {
    int position;

    public StackMoreEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
