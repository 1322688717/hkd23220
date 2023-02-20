package cc.ixcc.novelthree.bean;



public class  ActiviyNoticeInfo{
    private int type;
    private int anid;
    private int status;
    private String url;
    private String http_url;
    private String pay_id;

    public int getType(){ return type;}
    public void setType(int type){this.type = type;}

    public int getAnid(){ return anid;}
    public void setAnid(int anid){this.anid = anid;}

    public int getStatus(){ return status;}
    public void setStatus(int status){this.status = status;}

    public String getUrl(){return url;}
    public void setUrl(String url){this.url = url;}

    public String gethttp_url(){return http_url;}
    public void sethttp_url(String http_url){this.http_url = http_url;}

    public String getpay_id(){return pay_id;}
    public void setpay_id(String pay_id){this.pay_id = pay_id;}
}