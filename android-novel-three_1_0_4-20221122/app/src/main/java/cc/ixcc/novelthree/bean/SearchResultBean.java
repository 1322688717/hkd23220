package cc.ixcc.novelthree.bean;

public class SearchResultBean {

    /**
     * anid : 1
     * title : 错惹心机男神
     * author : 夏雪雨
     * coverpic : http://qnlive.csvclub.cn/admin/1/store/files/20200716/498e62d4c4c72057aeb8adb8d12c68ad.png
     * iswz : 2
     * desc : 她为了摆脱师兄在毕业典礼上表白的尴尬，主动向自己心仪已久的男神表白，她只求男神逢场作戏，没想到却弄假成真，嫁入穗城第一豪门，婚后，他对她冷若冰霜，仅仅过了三天，她就被扫地出门，成为全城第一弃妇，她想重新开始平静的日子，他却对她一直纠缠不止……
     * btype : 2
     * cateids : 2,14
     */

    private int anid;
    private String title;
    private String author;
    private String coverpic;
    private int iswz;
    private String desc;
    private int btype;
    private String cateids;

    public String getIsvip() {
        return isvip;
    }

    public void setIsvip(String isvip) {
        this.isvip = isvip;
    }

    private String isvip;

    public int getAnid() {
        return anid;
    }

    public void setAnid(int anid) {
        this.anid = anid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverpic() {
        return coverpic;
    }

    public void setCoverpic(String coverpic) {
        this.coverpic = coverpic;
    }

    public int getIswz() {
        return iswz;
    }

    public void setIswz(int iswz) {
        this.iswz = iswz;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public String getCateids() {
        return cateids;
    }

    public void setCateids(String cateids) {
        this.cateids = cateids;
    }
}
