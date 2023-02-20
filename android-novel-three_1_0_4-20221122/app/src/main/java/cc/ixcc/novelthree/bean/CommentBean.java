package cc.ixcc.novelthree.bean;

import java.util.List;

public class CommentBean {
    /**
     * id : 74
     * title : 侯府商女
     * coverpic : http://dunovel.jiusencms.com//Public/upload/images/2005/29/104457489102008032.jpg
     * total_score_person : 1
     * average_score : 4
     * average_star :
     * list : [{"id":25,"anid":74,"cid":0,"btype":2,"title":"侯府商女","coverpic":"http://dunovel.jiusencms.com//Public/upload/images/2005/29/104457489102008032.jpg","user_id":19990,"avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo1CwMrhdXCZLgl99RyGkGKpB9fYbcnUbMHHlrbumFcB0E0Mq5lMaFDctroibN1KDEhHBOiaQH3ekJg/132","nickname":"笑","content":"内容新颖，题材有趣","status":1,"star":4,"likes":1,"created_at":"2020-06-23T06:37:58.000000Z","updated_at":"2020-06-23T10:22:15.000000Z"}]
     */

    private int id;
    private String title;
    private String coverpic;
    private int total_score_person;
    private float average_score;
    private int average_star;
    private List<ListBean> list;

    public float getAverage_score() {
        return average_score;
    }

    public void setAverage_score(float average_score) {
        this.average_score = average_score;
    }

    public int getAverage_star() {
        return average_star;
    }

    public void setAverage_star(int average_star) {
        this.average_star = average_star;
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

    public int getTotal_score_person() {
        return total_score_person;
    }

    public void setTotal_score_person(int total_score_person) {
        this.total_score_person = total_score_person;
    }

//    public int getAverage_score() {
//        return average_score;
//    }
//
//    public void setAverage_score(int average_score) {
//        this.average_score = average_score;
//    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 25
         * anid : 74
         * cid : 0
         * btype : 2
         * title : 侯府商女
         * coverpic : http://dunovel.jiusencms.com//Public/upload/images/2005/29/104457489102008032.jpg
         * user_id : 19990
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo1CwMrhdXCZLgl99RyGkGKpB9fYbcnUbMHHlrbumFcB0E0Mq5lMaFDctroibN1KDEhHBOiaQH3ekJg/132
         * nickname : 笑
         * content : 内容新颖，题材有趣
         * status : 1
         * star : 4
         * likes : 1
         * created_at : 2020-06-23T06:37:58.000000Z
         * updated_at : 2020-06-23T10:22:15.000000Z
         */

        private int id;
        private int anid;
        private int cid;
        private int btype;
        private String title;
        private String coverpic;
        private int user_id;
        private String avatar;
        private String nickname;
        private String content;
        private int status;
        private int star;
        private int likes;
        private String created_at;
        private String updated_at;
        private String is_admire;
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

        public int getAnid() {
            return anid;
        }

        public void setAnid(int anid) {
            this.anid = anid;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
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

        public String getCoverpic() {
            return coverpic;
        }

        public void setCoverpic(String coverpic) {
            this.coverpic = coverpic;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
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

        public String getIs_admire() {
            return is_admire;
        }

        public void setIs_admire(String is_admire) {
            this.is_admire = is_admire;
        }
    }
}
