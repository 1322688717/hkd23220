package cc.ixcc.novelthree.bean;

import java.util.List;

public class BookMoreBean {

    /**
     * count : 2
     * page : 1
     * page_size : 15
     * list : [{"id":65,"title":"独家蜜婚","coverpic":"http://dunovel.jiusencms.com//Public/upload/images/2005/29/104457489102008032.jpg","allchapter":2084,"author":"佚名","remark":"&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;&amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; 作品简介&lt;/p&gt;&lt;p&gt;为了实现自己成为名厨的梦想，厨娘小荞决心逃离赵府外出历练，谁知正好撞上铭丹楼杀手言雎前来执行任务！&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;后者不仅瞬间灭了赵府满门，还抢走了她辛苦做的打卤面并当场吃光！！！&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;而杀手吃完的第一句感想竟然是\u2014\u2014\u201c洞房吧！\u201d大概不是什么正经漫画。&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;","desc":"小说中的故事发生在1987年的佛蒙特州（Vermont）乡村，正值狂热的朋克运动（hardcore-punk movement）方兴未艾。十六岁的裘德（Jude Keffy-Horn）成长于一个嬉皮士家庭，父亲在外贩卖大麻、母亲吹制玻璃水烟枪。处于社会边缘地带的裘德一天到晚在佛蒙特州小镇上晃荡，和最好的朋友泰迪（Teddy）厮混找乐子，镇上的人对他们早已司空见惯了。","iswz":1,"hots":494,"btype":2,"anid":65,"classify":"都市","average_score":93.3},{"id":54,"title":"不良鲜妻有点甜","coverpic":"http://dunovel.jiusencms.com/Public/upload/images/2006/03/091321031714004977.jpg","allchapter":1080,"author":"佚名","remark":"&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;&amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; 作品简介&lt;/p&gt;&lt;p&gt;为了实现自己成为名厨的梦想，厨娘小荞决心逃离赵府外出历练，谁知正好撞上铭丹楼杀手言雎前来执行任务！&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;后者不仅瞬间灭了赵府满门，还抢走了她辛苦做的打卤面并当场吃光！！！&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;而杀手吃完的第一句感想竟然是\u2014\u2014\u201c洞房吧！\u201d大概不是什么正经漫画。&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;","desc":"小说中的故事发生在1987年的佛蒙特州（Vermont）乡村，正值狂热的朋克运动（hardcore-punk movement）方兴未艾。十六岁的裘德（Jude Keffy-Horn）成长于一个嬉皮士家庭，父亲在外贩卖大麻、母亲吹制玻璃水烟枪。处于社会边缘地带的裘德一天到晚在佛蒙特州小镇上晃荡，和最好的朋友泰迪（Teddy）厮混找乐子，镇上的人对他们早已司空见惯了。","iswz":1,"hots":546,"btype":2,"anid":54,"classify":"都市","average_score":50}]
     */

    private int count;
    private String page;
    private String page_size;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 65
         * title : 独家蜜婚
         * coverpic : http://dunovel.jiusencms.com//Public/upload/images/2005/29/104457489102008032.jpg
         * allchapter : 2084
         * author : 佚名
         * remark : &lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;&amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; &amp;nbsp; 作品简介&lt;/p&gt;&lt;p&gt;为了实现自己成为名厨的梦想，厨娘小荞决心逃离赵府外出历练，谁知正好撞上铭丹楼杀手言雎前来执行任务！&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;后者不仅瞬间灭了赵府满门，还抢走了她辛苦做的打卤面并当场吃光！！！&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;&lt;p&gt;而杀手吃完的第一句感想竟然是——“洞房吧！”大概不是什么正经漫画。&lt;/p&gt;&lt;p&gt;&lt;br/&gt;&lt;/p&gt;
         * desc : 小说中的故事发生在1987年的佛蒙特州（Vermont）乡村，正值狂热的朋克运动（hardcore-punk movement）方兴未艾。十六岁的裘德（Jude Keffy-Horn）成长于一个嬉皮士家庭，父亲在外贩卖大麻、母亲吹制玻璃水烟枪。处于社会边缘地带的裘德一天到晚在佛蒙特州小镇上晃荡，和最好的朋友泰迪（Teddy）厮混找乐子，镇上的人对他们早已司空见惯了。
         * iswz : 1
         * hots : 494
         * btype : 2
         * anid : 65
         * classify : 都市
         * average_score : 93.3
         */

        private int id;
        private String title;
        private String coverpic;
        private int allchapter;
        private String author;
        private String remark;
        private String desc;
        private int iswz;
        private int hots;
        private int btype;
        private int anid;
        private StackRoomBookBean.ColumnBean.Classify classify;
        private double average_score;

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

        public double getAverage_score() {
            return average_score;
        }

        public void setAverage_score(double average_score) {
            this.average_score = average_score;
        }
    }
}
