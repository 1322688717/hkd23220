package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.utils.MyObject;
import cc.ixcc.novelthree.utils.SpUtil;
import com.jiusen.base.util.DpUtil;
import com.tencent.mmkv.MMKV;

/**
 * Created by cxf on 2018/9/25.
 */

public class WebViewActivity extends MyActivity {
    public static WebViewActivity mWebActivity = null;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    //Android 5.0以下的
    private final int CHOOSE = 100;
    //Android 5.0以上的
    private final int CHOOSE_ANDROID_5 = 200;
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mValueCallback2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        mWebActivity = this;
        main();
    }

    @Override
    protected void initData() {

    }

    protected void main() {
        String url = getIntent().getStringExtra(Constants.URL);
        String title = getIntent().getStringExtra(Constants.TITLE);
        setTitle(title);
        Log.e(WebViewActivity.class.getName(), "H5-------->3" + url);
        LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebView = new WebView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = DpUtil.dip2px(getContext(), 1);
        mWebView.setLayoutParams(params);
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rootView.addView(mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(WebViewActivity.class.getName(), "H5-------->4" + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (TextUtils.isEmpty(url)) {
                    setTitle(view.getTitle());
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(newProgress);
                }
            }

//            //以下是在各个Android版本中 WebView调用文件选择器的方法
//            // For Android < 3.0
//            public void openFileChooser(ValueCallback<Uri> valueCallback) {
//                openImageChooserActivity(valueCallback);
//            }
//
//            // For Android  >= 3.0
//            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
//                openImageChooserActivity(valueCallback);
//            }
//
//            //For Android  >= 4.1
//            public void openFileChooser(ValueCallback<Uri> valueCallback,
//                                        String acceptType, String capture) {
//                openImageChooserActivity(valueCallback);
//            }

            // For Android >= 5.0
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mValueCallback2 = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                startActivityForResult(intent, CHOOSE_ANDROID_5);
                return true;
            }

        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.addJavascriptInterface(new MyObject(getActivity()), "Android");
        mWebView.loadUrl(url);
    }

    public static void forward(Context context, String url, String title, boolean addArgs) {
        if (addArgs) {
            url += "&uid=" + MMKV.defaultMMKV().decodeString(SpUtil.UID,"");
        }
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 返回键返回上一网页
     */
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
