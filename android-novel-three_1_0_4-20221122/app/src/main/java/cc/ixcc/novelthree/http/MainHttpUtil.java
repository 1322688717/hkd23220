package cc.ixcc.novelthree.http;

/**
 * Created by cxf on 2018/9/17.
 */

public class MainHttpUtil {

    private static final String SALT = "76576076c1f5f657b634e966c8836a06";
    private static final String DEVICE = "android";

    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }

    public static void readhistory(int page, int pagesize, HttpCallback callback) {
        HttpClient.getInstance().get(AllApi.readhistory, AllApi.readhistory).params("page", page).params("page_size", pagesize).execute(callback);
    }

    public static void getMessage(String cattype, String type, int page, int pagesize, HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.message, AllApi.message)
                .params("cat_type", cattype)
                .params("type", type)
                .params("device", "android")
                .params("page", page)
                .params("page_size", pagesize)
                .execute(callback);

    }

    public static void writerApply(HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.writerapply, AllApi.writerapply)
                .execute(callback);
    }


    public static void deleteMessage(String type, String id, HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.deletemessage, AllApi.deletemessage)
                .params("id", id)
                .params("type", type)
                .execute(callback);

    }

    public static void readmessage(String type, String id, HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.readmessage, AllApi.readmessage)
                .params("id", id)
                .params("type", type)
                .execute(callback);

    }

    public static void withdraw(String money, String coin, String name, String account, int type, HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.withdraw, AllApi.withdraw)
                .params("money", money)
                .params("coin", coin)
                .params("name", name)
                .params("type", type)
                .params("account", account)
                .execute(callback);
    }

    public static void getReadCount(HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.getreadcount, AllApi.getreadcount)
                .execute(callback);
    }

    public static void emptyreadhistory(HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.emptyreadhistory, AllApi.emptyreadhistory)
                .execute(callback);
    }

    public static void createorder(int charge_id, HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.coincreateorder, AllApi.coincreateorder)
                .params("charge_id", charge_id)
                .params("pay_type", "0")
                .execute(callback);
    }

    public static void createViporder(int charge_id, HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.vipcreateorder, AllApi.vipcreateorder)
                .params("vip_id", charge_id)
                .params("pay_type", "0")
                .execute(callback);
    }

    public static void changeguess(int anime_id, HttpCallback callback) {
        HttpClient.getInstance().post(AllApi.changeguess, AllApi.changeguess)
            .params("anime_id", anime_id)
            .execute(callback);

    }
}




