package cc.ixcc.novelthree.bean;

public class IcornPayBackBean {
    private String content;
    private String responseCode;
    private String responseMessage;
    private String sign;

    public String getContent(){return content;}

    public class PayBackItem
    {
        private String code;
        private String message;
        private String txnId;
        private String status;
        private String webUrl;

        public String getCode(){return code;}
        public String getMessage(){return message;}
        public String getTxnId(){return txnId;}
        public String getStatus(){return status;}
        public String getWebUrl(){return webUrl;}
    }
}

