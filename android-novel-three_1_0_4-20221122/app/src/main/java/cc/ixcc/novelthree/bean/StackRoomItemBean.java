package cc.ixcc.novelthree.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class StackRoomItemBean implements MultiItemEntity {
    private int id;
    private String title;
    private String coverpic;
    private int allchapter;
    private String author;
    private String remark;
    private String desc;

    private boolean isvip;
    private int iswz;
    private int hots;
    private int btype;
    private int anid;
    private StackRoomBookBean.ColumnBean.Classify classify;
    private boolean isMore;
    private double average_score;
    private int leftMargin;
    private int rightMargin;

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isIsvip() {
        return isvip;
    }

    public void setIsvip(boolean isvip) {
        this.isvip = isvip;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

    public List<AdBean> getAd() {
        return ad;
    }

    public void setAd(List<AdBean> ad) {
        this.ad = ad;
    }

    private List<AdBean> ad;
    /**
     * 三种类型
     * 0 Title布局
     * 1 一行一个
     * 2 一行2个
     * 3 一行3个
     * 4 一行4个
     * -1 底部分割线
     * -2 头部LikeView
     * -3 头部LikeBanner
     * @return
     */
    private int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

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

    public String getCoverpic() {
        return coverpic;
    }

    public void setCoverpic(String coverpic) {
        this.coverpic = coverpic;
    }

    public int getAllchapter() {
        return allchapter;
    }

    public void setAllchapter(int allchapter) {
        this.allchapter = allchapter;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIswz() {
        return iswz;
    }

    public void setIswz(int iswz) {
        this.iswz = iswz;
    }

    public int getHots() {
        return hots;
    }

    public void setHots(int hots) {
        this.hots = hots;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public int getAnid() {
        return anid;
    }

    public void setAnid(int anid) {
        this.anid = anid;
    }

    public StackRoomBookBean.ColumnBean.Classify getClassify() {
        return classify;
    }

    public void setClassify(StackRoomBookBean.ColumnBean.Classify classify) {
        this.classify = classify;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public double getAverage_score() {
        return average_score;
    }

    public void setAverage_score(double average_score) {
        this.average_score = average_score;
    }


    public static class AdBean {
        /**
         * id : 55
         * coin : 28
         * btype : 2
         * title : 超级唐僧闯西游
         * author : 佚名
         * coverpic : http://dunovel.jiusencms.com/Public/upload/images/2006/03/091321031714004977.jpg
         * infopic : http://dunovel.jiusencms.com/Public/upload/images/2006/03/091321031714004977.jpg
         * remark : &lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;&amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; 作品简介&lt;/p&gt;&lt;p&gt;为了实现自己成为名厨的梦想，厨娘小荞决心逃离赵府外出历练，谁知正好撞上铭丹楼杀手言雎前来执行任务！&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;后者不仅瞬间灭了赵府满门，还抢走了她辛苦做的打卤面并当场吃光！！！&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;而杀手吃完的第一句感想竟然是——“洞房吧！”大概不是什么正经漫画。&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;
         * desc : 小说中的故事发生在1987年的佛蒙特州（Vermont）乡村，正值狂热的朋克运动（hardcore-punk movement）方兴未艾。十六岁的裘德（Jude Keffy-Horn）成长于一个嬉皮士家庭，父亲在外贩卖大麻、母亲吹制玻璃水烟枪。处于社会边缘地带的裘德一天到晚在佛蒙特州小镇上晃荡，和最好的朋友泰迪（Teddy）厮混找乐子，镇上的人对他们早已司空见惯了。
         * areas : 1
         * cateids : 1,2,9,10
         * iswz : 1
         * isfw : 1
         * isnew : 1
         * islong : 1
         * issex : 2
         * allchapter : 629
         * paychapter : 13
         * schapter : 5
         * tchapter : 4
         * hots : 289
         * thermal : 100
         * comments : 0
         * likes : 675764
         * list_order : 0
         * sharetitle : 超级唐僧闯西游
         * sharedesc : 小说中的故事发生在1987年的佛蒙特州（Vermont）乡村，正值狂热的朋克运动（hardcore-punk movement）方兴未艾。十六岁的裘德（Jude Keffy-Horn）成长于一个嬉皮士家庭，父亲在外贩卖大麻、母亲吹制玻璃水烟枪。处于社会边缘地带的裘德一天到晚在佛蒙特州小镇上晃荡，和最好的朋友泰迪（Teddy）厮混找乐子，镇上的人对他们早已司空见惯了。
         * isrecommend : 1
         * status : 1
         * ishow : 1
         * prize : 0
         * isbg : 1
         * selectpic : null
         * tag : 搞笑/冒险/后宫/仙侠/热血/恋爱/战斗/历史
         * created_at : 2020.06.19
         * updated_at : 2020.07.09
         * isvip
         */

        private int id;
        private int coin;
        private int btype;
        private String title;
        private String author;
        private String coverpic;
        private String infopic;
        private String remark;
        private String desc;
        private String areas;
        private String cateids;
        private int iswz;
        private int isfw;
        private int isnew;
        private int islong;
        private int issex;
        private int allchapter;
        private int paychapter;
        private int schapter;
        private int tchapter;
        private int hots;
        private int thermal;
        private int comments;
        private int likes;
        private int list_order;
        private String sharetitle;
        private String sharedesc;
        private int isrecommend;
        private int status;
        private int ishow;
        private int prize;
        private int isbg;
        private Object selectpic;
        private String tag;
        private String created_at;
        private String updated_at;
        private String image;
        private String url;
        private int anid;


        public boolean isIsvip() {
            return isvip;
        }

        public void setIsvip(boolean isvip) {
            this.isvip = isvip;
        }

        private boolean isvip;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAnid() {
            return anid;
        }

        public void setAnid(int anid) {
            this.anid = anid;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
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

        public String getInfopic() {
            return infopic;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setInfopic(String infopic) {
            this.infopic = infopic;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getAreas() {
            return areas;
        }

        public void setAreas(String areas) {
            this.areas = areas;
        }

        public String getCateids() {
            return cateids;
        }

        public void setCateids(String cateids) {
            this.cateids = cateids;
        }

        public int getIswz() {
            return iswz;
        }

        public void setIswz(int iswz) {
            this.iswz = iswz;
        }

        public int getIsfw() {
            return isfw;
        }

        public void setIsfw(int isfw) {
            this.isfw = isfw;
        }

        public int getIsnew() {
            return isnew;
        }

        public void setIsnew(int isnew) {
            this.isnew = isnew;
        }

        public int getIslong() {
            return islong;
        }

        public void setIslong(int islong) {
            this.islong = islong;
        }

        public int getIssex() {
            return issex;
        }

        public void setIssex(int issex) {
            this.issex = issex;
        }

        public int getAllchapter() {
            return allchapter;
        }

        public void setAllchapter(int allchapter) {
            this.allchapter = allchapter;
        }

        public int getPaychapter() {
            return paychapter;
        }

        public void setPaychapter(int paychapter) {
            this.paychapter = paychapter;
        }

        public int getSchapter() {
            return schapter;
        }

        public void setSchapter(int schapter) {
            this.schapter = schapter;
        }

        public int getTchapter() {
            return tchapter;
        }

        public void setTchapter(int tchapter) {
            this.tchapter = tchapter;
        }

        public int getHots() {
            return hots;
        }

        public void setHots(int hots) {
            this.hots = hots;
        }

        public int getThermal() {
            return thermal;
        }

        public void setThermal(int thermal) {
            this.thermal = thermal;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getList_order() {
            return list_order;
        }

        public void setList_order(int list_order) {
            this.list_order = list_order;
        }

        public String getSharetitle() {
            return sharetitle;
        }

        public void setSharetitle(String sharetitle) {
            this.sharetitle = sharetitle;
        }

        public String getSharedesc() {
            return sharedesc;
        }

        public void setSharedesc(String sharedesc) {
            this.sharedesc = sharedesc;
        }

        public int getIsrecommend() {
            return isrecommend;
        }

        public void setIsrecommend(int isrecommend) {
            this.isrecommend = isrecommend;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIshow() {
            return ishow;
        }

        public void setIshow(int ishow) {
            this.ishow = ishow;
        }

        public int getPrize() {
            return prize;
        }

        public void setPrize(int prize) {
            this.prize = prize;
        }

        public int getIsbg() {
            return isbg;
        }

        public void setIsbg(int isbg) {
            this.isbg = isbg;
        }

        public Object getSelectpic() {
            return selectpic;
        }

        public void setSelectpic(Object selectpic) {
            this.selectpic = selectpic;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }


    @Override
    public int getItemType() {
        return viewType;
    }
}
