package cc.ixcc.novelthree.http.server;

import com.hjq.http.config.IRequestServer;

/**
 *    desc   : 正式环境
 */
public class ReleaseServer implements IRequestServer {

    @Override
    public String getHost() {
        return "https://www.baidu.com/";
    }

    @Override
    public String getPath() {
        return "api/";
    }
}