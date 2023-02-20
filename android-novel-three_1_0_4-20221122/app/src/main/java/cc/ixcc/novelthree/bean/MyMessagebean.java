package cc.ixcc.novelthree.bean;

import java.io.Serializable;

public class MyMessagebean implements Serializable {

    /**
     * id : 93
     * cat_type : comment
     * type : comment
     * send_uid : 10048
     * send_avatar : http://qnlive.csvclub.cn/default_boy.png
     * send_nickname : 手机用户18356137670
     * read_status : 0
     * time_before : 18小时前
     * time_detail : 昨天06:56
     * rel_id : 2050
     * rel_title : 巨灵天子
     * cover_url : http://qnlive.csvclub.cn/admin/1/store/files/20200716/f257a6bf94f6e5a20d6ff6b961813769.png
     * summary : 123
     * summary_type : text
     * content : {"anid":"2050","title":"巨灵天子","coverpic":"http://qnlive.csvclub.cn/admin/1/store/files/20200716/f257a6bf94f6e5a20d6ff6b961813769.png","time":"2020-07-21T06:56:37.000000Z","type":"text"}
     * comment_id : 58
     * alt : 你发表了书评
     * at_alt : 你发表了书评
     */

    private int id;
    private String cat_type;
    private String type;
    private String send_uid;
    private String send_avatar;
    private String send_nickname;
    private String read_status;
    private String time_before;
    private String time_detail;
    private String rel_id;
    private String rel_title;
    private String cover_url;
    private String summary;
    private String url;
    private String summary_type;
    private String msg_title;
    private String title;
    private String detail;
    private ContentBean content;
    private int comment_id;
    private String alt;
    private String at_alt;
    private String is_vip;

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSend_uid() {
        return send_uid;
    }

    public void setSend_uid(String send_uid) {
        this.send_uid = send_uid;
    }

    public String getSend_avatar() {
        return send_avatar;
    }

    public void setSend_avatar(String send_avatar) {
        this.send_avatar = send_avatar;
    }

    public String getSend_nickname() {
        return send_nickname;
    }

    public void setSend_nickname(String send_nickname) {
        this.send_nickname = send_nickname;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getTime_before() {
        return time_before;
    }

    public void setTime_before(String time_before) {
        this.time_before = time_before;
    }

    public String getTime_detail() {
        return time_detail;
    }

    public void setTime_detail(String time_detail) {
        this.time_detail = time_detail;
    }

    public String getRel_id() {
        return rel_id;
    }

    public void setRel_id(String rel_id) {
        this.rel_id = rel_id;
    }

    public String getRel_title() {
        return rel_title;
    }

    public void setRel_title(String rel_title) {
        this.rel_title = rel_title;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary_type() {
        return summary_type;
    }

    public void setSummary_type(String summary_type) {
        this.summary_type = summary_type;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getComment_id() {
        return comment_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getAt_alt() {
        return at_alt;
    }

    public void setAt_alt(String at_alt) {
        this.at_alt = at_alt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class ContentBean implements Serializable {
        /**
         * anid : 2050
         * title : 巨灵天子
         * coverpic : http://qnlive.csvclub.cn/admin/1/store/files/20200716/f257a6bf94f6e5a20d6ff6b961813769.png
         * time : 2020-07-21T06:56:37.000000Z
         * type : text
         */

        private String anid;
        private String title;
        private String coverpic;
        private String time;
        private String type;
        private int likes;
        private String like_comment_content;
        private String like_comment_type;

        public String getAnid() {
            return anid;
        }

        public void setAnid(String anid) {
            this.anid = anid;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public String getLike_comment_content() {
            return like_comment_content;
        }

        public void setLike_comment_content(String like_comment_content) {
            this.like_comment_content = like_comment_content;
        }

        public String getLike_comment_type() {
            return like_comment_type;
        }

        public void setLike_comment_type(String like_comment_type) {
            this.like_comment_type = like_comment_type;
        }
    }
}
