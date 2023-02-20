package cc.ixcc.novelthree.bean;

import java.util.List;

public class Read {


//    private String name;
//
//
//    private boolean isSelection;
    /**
     * issex : 2
     * hobby : [{"id":1,"title":"都市","isselect":"1"},{"id":2,"title":"言情","isselect":"1"},{"id":3,"title":"校园","isselect":"1"},{"id":4,"title":"总裁","isselect":"0"},{"id":5,"title":"穿越","isselect":"0"},{"id":6,"title":"历史","isselect":"0"},{"id":7,"title":"异能","isselect":"0"},{"id":8,"title":"玄幻","isselect":"0"},{"id":9,"title":"武侠","isselect":"0"},{"id":10,"title":"科幻","isselect":"0"},{"id":11,"title":"恐怖","isselect":"0"},{"id":12,"title":"男生","isselect":"0"},{"id":13,"title":"女生","isselect":"0"}]
     */

    private String issex;
    private List<HobbyBean> hobby;


//    public boolean isSelection() {
//        return isSelection;
//    }
//
//    public void setSelection(boolean selection) {
//        isSelection = selection;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getIssex() {
        return issex;
    }

    public void setIssex(String issex) {
        this.issex = issex;
    }

    public List<HobbyBean> getHobby() {
        return hobby;
    }

    public void setHobby(List<HobbyBean> hobby) {
        this.hobby = hobby;
    }

    public static class HobbyBean {
        /**
         * id : 1
         * title : 都市
         * isselect : 1
         */

        private int id;
        private StackRoomBookBean.ColumnBean.Classify title;
        private String isselect;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public StackRoomBookBean.ColumnBean.Classify getTitle() {
            return title;
        }
        public void setTitle(StackRoomBookBean.ColumnBean.Classify title) {
            this.title = title;
        }

        public String getIsselect() {
            return isselect;
        }

        public void setIsselect(String isselect) {
            this.isselect = isselect;
        }
    }
}
