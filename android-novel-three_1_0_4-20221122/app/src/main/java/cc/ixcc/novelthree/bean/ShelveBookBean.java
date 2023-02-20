package cc.ixcc.novelthree.bean;

import java.util.List;

public class ShelveBookBean {

    /**
     * today_read_time : 0
     * count : 1
     * page : 1
     * page_size : 15
     * list : [{"id":60,"user_id":19990,"anid":75,"coverpic":"http://qnlive.csvclub.cn/admin/1/store/files/20200715/cd93a67c70ac3286c766b32ec091aea6.png","title":"一晌贪欢：绍总请自重","intro":"人生有几个三年，再见，已经是又一个三年。他步步为营，处处费心接近，她一退再退，不想重蹈覆辙，可最后，还是没能逃过自己的心。","btype":2,"last_read_time":0,"created_at":"2020-07-15 09:18:04","updated_at":"2020-07-15 09:18:04","allchapter":339,"author":"沐歌","iswz":2,"remark":"人生有几个三年，再见，已经是又一个三年。他步步为营，处处费心接近，她一退再退，不想重蹈覆辙，可最后，还是没能逃过自己的心。","desc":"人生有几个三年，再见，已经是又一个三年。他步步为营，处处费心接近，她一退再退，不想重蹈覆辙，可最后，还是没能逃过自己的心。","read_status":0,"read_chaps":0,"chapter":null}]
     */

    private int today_read_time;
    private int count;
    private String page;
    private String page_size;
    private List<ListBean> list;

    public int getToday_read_time() {
        return today_read_time;
    }

    public void setToday_read_time(int today_read_time) {
        this.today_read_time = today_read_time;
    }

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
         * id : 60
         * user_id : 19990
         * anid : 75
         * coverpic : http://qnlive.csvclub.cn/admin/1/store/files/20200715/cd93a67c70ac3286c766b32ec091aea6.png
         * title : 一晌贪欢：绍总请自重
         * intro : 人生有几个三年，再见，已经是又一个三年。他步步为营，处处费心接近，她一退再退，不想重蹈覆辙，可最后，还是没能逃过自己的心。
         * btype : 2
         * last_read_time : 0
         * created_at : 2020-07-15 09:18:04
         * updated_at : 2020-07-15 09:18:04
         * allchapter : 339
         * author : 沐歌
         * iswz : 2
         * remark : 人生有几个三年，再见，已经是又一个三年。他步步为营，处处费心接近，她一退再退，不想重蹈覆辙，可最后，还是没能逃过自己的心。
         * desc : 人生有几个三年，再见，已经是又一个三年。他步步为营，处处费心接近，她一退再退，不想重蹈覆辙，可最后，还是没能逃过自己的心。
         * read_status : 0
         * read_chaps : 0
         * chapter : null
         * isvip 是否会员书 1是
         */

        private int id;
        private int user_id;
        private int anid;
        private String coverpic;
        private String title;
        private String intro;
        private int btype;
        private int last_read_time;
        private String created_at;
        private String updated_at;
        private int allchapter;
        private String author;
        private int iswz;
        private String remark;
        private String desc;
        private int read_status;
        private int read_chaps;
        private Object chapter;
        boolean checkshow;
        boolean check;

        public String getIsvip() {
            return isvip;
        }

        public void setIsvip(String isvip) {
            this.isvip = isvip;
        }

        private String isvip;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getCoverpic() {
            return coverpic;
        }

        public void setCoverpic(String coverpic) {
            this.coverpic = coverpic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public int getLast_read_time() {
            return last_read_time;
        }

        public void setLast_read_time(int last_read_time) {
            this.last_read_time = last_read_time;
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

        public int getRead_status() {
            return read_status;
        }

        public void setRead_status(int read_status) {
            this.read_status = read_status;
        }

        public int getRead_chaps() {
            return read_chaps;
        }

        public void setRead_chaps(int read_chaps) {
            this.read_chaps = read_chaps;
        }

        public Object getChapter() {
            return chapter;
        }

        public void setChapter(Object chapter) {
            this.chapter = chapter;
        }

        public boolean isCheckshow() {
            return checkshow;
        }

        public void setCheckshow(boolean checkshow) {
            this.checkshow = checkshow;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}
