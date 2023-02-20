package cc.ixcc.novelthree.http;

import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.utils.SpUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.tencent.mmkv.MMKV;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cxf on 2018/9/17.
 */

public class HttpClient {

    private static final int TIMEOUT = 10000;
    private static HttpClient sInstance;
    private OkHttpClient mOkHttpClient;
    private String mLanguage;//语言
    private String mUrl;

    private HttpClient() {
        mUrl = AllApi.HOST + "api/";
    }

    public static HttpClient getInstance() {
        if (sInstance == null) {
            synchronized (HttpClient.class) {
                if (sInstance == null) {
                    sInstance = new HttpClient();
                }
            }
        }
        return sInstance;
    }

    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        builder.retryOnConnectionFailure(true);

        //输出HTTP请求 响应信息
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("http");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", MMKV.defaultMMKV().decodeString(SpUtil.TOKEN,""))
                        .addHeader("Connection", "keep-alive")
                        .build();
                return chain.proceed(request);
            }
        });
        mOkHttpClient = builder.build();

        OkGo.getInstance().init(AppContext.sInstance)
                .setOkHttpClient(mOkHttpClient)
                .setCacheMode(CacheMode.NO_CACHE)
                .setRetryCount(1);

    }

    public GetRequest<Data> get(String serviceName, String tag) {
        return OkGo.<Data>get(mUrl + serviceName).tag(tag).params(Constants.LANGUAGE, mLanguage);
    }

    public PostRequest<Data> post(String serviceName, String tag) {
        return OkGo.<Data>post(mUrl + serviceName).tag(tag).params(Constants.LANGUAGE, mLanguage);
    }

    public void post(String url, Map<String, String> requestParams, Callback callback) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            bodyBuilder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    public void cancel(String tag) {
        OkGo.cancelTag(mOkHttpClient, tag);
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }
}
