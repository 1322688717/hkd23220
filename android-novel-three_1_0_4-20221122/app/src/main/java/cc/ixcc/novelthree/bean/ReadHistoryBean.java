package cc.ixcc.novelthree.bean;

import java.util.List;

public class ReadHistoryBean {

    /**
     * count : 1
     * page : 1
     * page_size : 15
     * list : [{"id":40106,"mch":0,"user_id":10048,"anid":48,"chaps":2,"btype":2,"title":"一夜危情，总裁撩婚好手段","created_at":"2020-07-17 04:59:15","updated_at":"2020-07-17 04:59:15","allchapter":170,"author":"小梅子","coverpic":"http://qnlive.csvclub.cn/admin/1/store/files/20200716/a4eaf0be1be94afc2489fc695dd005c2.png","iswz":2,"remark":"为了替蒙受不白之冤的父亲洗刷冤屈，她给那个冷血总裁下了套，却没想连自己都套进去了，还意外怀着那个恶魔的孩子，一边是杀父之仇，一边是孩子父亲，这一次她该何去何从。","desc":"为了替蒙受不白之冤的父亲洗刷冤屈，她给那个冷血总裁下了套，却没想连自己都套进去了，还意外怀着那个恶魔的孩子，一边是杀父之仇，一边是孩子父亲，这一次她该何去何从。","chapter":{"title":"第二章：不知所措","chaps":2},"is_shelve":"0"}]
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
         * id : 40106
         * mch : 0
         * user_id : 10048
         * anid : 48
         * chaps : 2
         * btype : 2
         * title : 一夜危情，总裁撩婚好手段
         * created_at : 2020-07-17 04:59:15
         * updated_at : 2020-07-17 04:59:15
         * allchapter : 170
         * author : 小梅子
         * coverpic : http://qnlive.csvclub.cn/admin/1/store/files/20200716/a4eaf0be1be94afc2489fc695dd005c2.png
         * iswz : 2
         * remark : 为了替蒙受不白之冤的父亲洗刷冤屈，她给那个冷血总裁下了套，却没想连自己都套进去了，还意外怀着那个恶魔的孩子，一边是杀父之仇，一边是孩子父亲，这一次她该何去何从。
         * desc : 为了替蒙受不白之冤的父亲洗刷冤屈，她给那个冷血总裁下了套，却没想连自己都套进去了，还意外怀着那个恶魔的孩子，一边是杀父之仇，一边是孩子父亲，这一次她该何去何从。
         * chapter : {"title":"第二章：不知所措","chaps":2}
         * is_shelve : 0
         */

        private int id;
        private int mch;
        private int user_id;
        private int anid;
        private int chaps;
        private int btype;
        private String title;
        private String created_at;
        private String updated_at;
        private int allchapter;
        private String author;
        private String coverpic;
        private int iswz;
        private String remark;
        private String desc;
        private ChapterBean chapter;
        private String is_shelve;
        boolean isAdd;
        private String isvip;

        public String getIsvip() {
            return isvip;
        }

        public void setIsvip(String isvip) {
            this.isvip = isvip;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMch() {
            return mch;
        }

        public void setMch(int mch) {
            this.mch = mch;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getAnid() {
            return anid;
        }

        public void setAnid(int anid) {
            this.anid = anid;
        }

        public int getChaps() {
            return chaps;
        }

        public void setChaps(int chaps) {
            this.chaps = chaps;
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

        public ChapterBean getChapter() {
            return chapter;
        }

        public void setChapter(ChapterBean chapter) {
            this.chapter = chapter;
        }

        public String getIs_shelve() {
            return is_shelve;
        }

        public void setIs_shelve(String is_shelve) {
            this.is_shelve = is_shelve;
        }

        public boolean isAdd() {
            return isAdd;
        }

        public void setAdd(boolean add) {
            isAdd = add;
        }

        public static class ChapterBean {
            /**
             * title : 第二章：不知所措
             * chaps : 2
             */

            private String title;
            private int chaps;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getChaps() {
                return chaps;
            }

            public void setChaps(int chaps) {
                this.chaps = chaps;
            }
        }
    }
}
