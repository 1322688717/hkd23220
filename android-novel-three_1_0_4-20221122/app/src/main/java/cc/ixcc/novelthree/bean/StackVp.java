package cc.ixcc.novelthree.bean;

/**
 * Date    : 2020/3/29
 * Version : 1.0
 * Desc    : 右测实体类
 */
public class StackVp {

    private int id;
    private String title;
    private String image;
    private int itemType;       //多布局标识
    private int fakePosition;   //记录当前右侧Item位置对应的左侧位置

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getFakePosition() {
        return fakePosition;
    }

    public void setFakePosition(int fakePosition) {
        this.fakePosition = fakePosition;
    }
}