package cc.ixcc.novelthree.event;

/**
 * Created by cxf on 2018/11/26.
 */

public class BlurImgEvent {
    String url;
    String tag;
    boolean isLike;

    public BlurImgEvent(String url, String tag, boolean isLike) {
        this.url = url;
        this.tag = tag;
        this.isLike = isLike;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
