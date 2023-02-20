package cc.ixcc.novelthree.http;

public class ExceptionCode {
    //有网络，但请求失败，如接口地址写错404，服务器异常500等。
    public static final int CODE_REQUEST_FAIL = 1;
    //没网，无法链接服务器等
    public static final int CODE_NET_ERROR = 2;
    //成功返回数据，但数据格式不对，json解析失败
    public static final int CODE_DATA_FORMAT = 3;
}