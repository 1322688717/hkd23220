package cc.ixcc.novelthree.event;

/**
 * Created by cxf on 2018/11/26.
 */

public class StackTabSelectEvent {
    private String tag;
    private boolean light;

    public StackTabSelectEvent(String tag, boolean light) {
        this.tag = tag;
        this.light = light;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }
}
