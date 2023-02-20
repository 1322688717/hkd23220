package cc.ixcc.novelthree.http.server;

/**
 *    desc   : 测试环境
 */
public class TestServer extends ReleaseServer {

    @Override
    public String getHost() {
        return "https://www.baidu.com/";
    }
}