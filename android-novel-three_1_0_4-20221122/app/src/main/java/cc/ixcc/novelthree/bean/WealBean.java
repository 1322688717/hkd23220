package cc.ixcc.novelthree.bean;
import java.util.Iterator;
import java.util.List;

public class WealBean {
    private int coin;
    private int today_read_time;
    private String year;
    private String m;
    private String d;
    private String w;
    private int sign_total_coin;

    private List<SignConfigBean> sign_config;
    //基础任务
    private List<BasicConfigBean> basic_config;
    //新手任务
    private List<NoviceConfigBean> novice_config;
    //每日阅读任务
    private List<DailyReadConfigBean> daily_read_config;
    //日常任务
    private List<DayConfigBean> day_config;

    public void DataFiltering()
    {
        Iterator<NoviceConfigBean> iter = novice_config.iterator();
        while(iter.hasNext())
        {
            if(iter.next().getStatus().equals("1"))
            {
                iter.remove();
            }
        }

        Iterator<DayConfigBean> iterDayConfig = day_config.iterator();
        while(iterDayConfig.hasNext())
        {
            if(iterDayConfig.next().getStatus().equals("1"))
            {
                iterDayConfig.remove();
            }
        }
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getToday_read_time() {
        return today_read_time;
    }

    public void setToday_read_time(int today_read_time) {
        this.today_read_time = today_read_time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public int getSign_total_coin() {
        return sign_total_coin;
    }

    public void setSign_total_coin(int sign_total_coin) {
        this.sign_total_coin = sign_total_coin;
    }

    public List<SignConfigBean> getSign_config() {
        return sign_config;
    }

    public void setSign_config(List<SignConfigBean> sign_config) {
        this.sign_config = sign_config;
    }

    public List<DailyReadConfigBean> getDaily_read_config() {
        return daily_read_config;
    }

    public List<BasicConfigBean> getBasicConfig()
    {
        return basic_config;
    }

    public List<DayConfigBean> getDayConfig(){
        return day_config;
    }

    public List<NoviceConfigBean> getNoviceConfig(){
        return novice_config;
    }

    public void setDaily_read_config(List<DailyReadConfigBean> daily_read_config) {
        this.daily_read_config = daily_read_config;
    }

    public static class SignConfigBean {
        /**
         * title : 周一
         * image : /Public/upload/images/2006/01/112455623056004216.png
         * coin : 30
         * desc : 获得三十金币
         * is_type : 0
         */

        private String title;
        private String image;
        private String coin;
        private String desc;
        private int is_type;

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

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getIs_type() {
            return is_type;
        }

        public void setIs_type(int is_type) {
            this.is_type = is_type;
        }
    }

    public static class DailyReadConfigBean {
        /**
         * key : 5分钟
         * coin : 60
         * time : 300
         * status : 0
         */

        private String key;
        private String coin;
        private int time;
        private String status;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    //基础任务
    public static class BasicConfigBean{
        private String title;
        private String sub_title;
        private String coin;
        private String status;

        public String getTitle()
        {
            return title;
        }

        public String getSubTitle()
        {
            return sub_title;
        }

        public String getCoin()
        {
            return coin;
        }

        public String getStatus(){return status;}
    }

    //日常任务
    public static class DayConfigBean{
        private String title;
        private String sub_title;
        private String target;
        private String coin;
        private String status;
        //private int time;

        public String getTitle()
        {
            return title;
        }

        public String getSubTitle()
        {
            return sub_title;
        }

        public String getCoin()
        {
            return coin;
        }

        public String getTarget()
        {
            return target;
        }

        public String getStatus() {return status;}
    }

    //新手任务
    public static class NoviceConfigBean{
        private String title;
        private String sub_title;
        private String target;
        private String coin;
        private String status;

        public String getTitle()
        {
            return title;
        }

        public String getSubTitle()
        {
            return sub_title;
        }

        public String getCoin()
        {
            return coin;
        }

        public String getTarget()
        {
            return target;
        }

        public String getStatus() {return status;}
    }
}
