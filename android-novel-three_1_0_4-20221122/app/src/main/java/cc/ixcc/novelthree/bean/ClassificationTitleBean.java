package cc.ixcc.novelthree.bean;

public class ClassificationTitleBean {

    /**
     * novel_id : 1
     * novel_name : 同人小说
     */
    private boolean isSelect;
    private int novel_id;
    public String sex;
    private String novel_name;
    private String cover;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getNovel_id() {
        return novel_id;
    }

    public void setNovel_id(int novel_id) {
        this.novel_id = novel_id;
    }

    @Override
    public String toString() {
        return "ClassificationTitleBean{" +
                "isSelect=" + isSelect +
                ", novel_id=" + novel_id +
                ", sex='" + sex + '\'' +
                ", novel_name='" + novel_name + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }

    public String getNovel_name() {
        return novel_name;
    }

    public void setNovel_name(String novel_name) {
        this.novel_name = novel_name;
    }

    public String getCover(){return cover;}
}
