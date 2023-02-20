package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.BookDetailBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.treader.Config;
import cc.ixcc.novelthree.treader.util.PageFactory;
import cc.ixcc.novelthree.treader.view.PageWidget;

import butterknife.BindView;
import butterknife.ButterKnife;

import static cc.ixcc.novelthree.treader.Config.BOOK_BG_NIGHT;

/**
 * desc   : 封面
 */
public final class BookCoverActivity extends MyActivity {

    @BindView(R.id.bookpage)
    PageWidget bookpage;
    Config config;
    @BindView(R.id.book_img)
    ImageView bookImg;
    @BindView(R.id.book_name)
    TextView bookName;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.lianzai)
    TextView lianzai;
    @BindView(R.id.typestr)
    TextView typestr;
    @BindView(R.id.timestr)
    TextView timestr;
    @BindView(R.id.lianzaistr)
    TextView lianzaistr;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.rllayout)
    RelativeLayout rllayout;
    //页面宽
    private int mWidth;
    //页面高
    private int mHeight;
    BookDetailBean bookInfo;
    int anime_id;
    static PageFactory mPageFactory;
    static PageWidget mBookPageWidget;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_cover;
    }

    public static void Start(Context context, int anime_id, PageWidget bookPageWidget, PageFactory pageFactory) {
        Intent intent = new Intent(context, BookCoverActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("anime_id", anime_id);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
        mPageFactory = pageFactory;
        mBookPageWidget = bookPageWidget;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }


    @Override
    protected void initView() {
//        mBookPageWidget.getParent().cl(mBookPageWidget);
        anime_id = getIntent().getExtras().getInt("anime_id");
        //获取屏幕宽高
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics metric = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(metric);
//        mWidth = metric.widthPixels;
//        mHeight = metric.heightPixels;
//        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
        config = Config.getInstance();
//        rllayout.addView(mBookPageWidget);
//        bookpage.setPageMode(config.getPageMode());
//        bookpage.postInvalidate();
//        mPageFactory.setPageWidget(bookpage);
        getInfo(anime_id);
    }


    public void getInfo(int anime_id) {
        HttpClient.getInstance().get(AllApi.bookinfo, AllApi.bookinfo)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        BookDetailBean resultBean = new Gson().fromJson(info[0], BookDetailBean.class);
                        bookInfo = resultBean;
                        initFenmianView();
                    }
                });

//        HttpUtils.get(AllApi.bookinfo, map, new HttpUtils.ICallback() {
//            @Override
//            public void success(Response response) throws IOException, JSONException {
//                String res = response.body().string();
//                JSONObject jsonObject = new JSONObject(res);
//                if (jsonObject.getInt("code") == 200) {
//                    BookInfo resultBean = new Gson().fromJson(res, BookInfo.class);
//                    bookInfo = resultBean;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            initFenmianView();
//                        }
//                    });
//                } else {
//                    ToastUtils.show(jsonObject.getString("msg"));
//                }
//            }
//
//            @Override
//            public void error(IOException e) {
//                Log.i("---res error", "error: " + e);
//                ToastUtils.show(e.getMessage());
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initFenmianView() {
        Glide.with(getContext())
                .load(bookInfo.getCoverpic())
//                .load(R.mipmap.book_img1)
                .into(bookImg);
        bookName.setText(bookInfo.getTitle());
        type.setText(bookInfo.getClassify() + (bookInfo.getBtype() == Constants.manhua ? getString(R.string.manhua) : (bookInfo.getBtype() == Constants.xiaoshuo ? getString(R.string.xiaoshuo) : getString(R.string.tingshu))));
        time.setText(bookInfo.getCreated_at());
        lianzai.setText(bookInfo.getIswz() == Constants.lianzai ? getString(R.string.lianzai) : getString(R.string.wanjie));
        initDayOrNight();
    }

    private void initDayOrNight() {
        bookName.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.read_font_default));
        type.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.read_font_default));
        time.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.read_font_default));
        lianzai.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.read_font_default));
        typestr.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.board_bg));
        timestr.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.board_bg));
        lianzaistr.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.board_bg));
        setBgColor(config.getDayOrNight() ? BOOK_BG_NIGHT : config.getBookBgType());
    }

    //设置页面的背景
    public void setBgColor(int type) {
        int color = 0;
        switch (type) {
            case BOOK_BG_NIGHT:
                color = config.isHuyan() ? R.color.day_night_huyan : R.color.day_night;
                break;
            case Config.BOOK_BG_DEFAULT:
                color = config.isHuyan() ? R.color.read_bg_default_huyan : R.color.read_bg_default;
                break;
            case Config.BOOK_BG_1:
                color = config.isHuyan() ? R.color.read_bg_1_huyan : R.color.read_bg_1;
                break;
            case Config.BOOK_BG_2:
                color = config.isHuyan() ? R.color.read_bg_2_huyan : R.color.read_bg_2;
                break;
            case Config.BOOK_BG_3:
                color = config.isHuyan() ? R.color.read_bg_3_huyan : R.color.read_bg_3;
                break;
            case Config.BOOK_BG_4:
                color = config.isHuyan() ? R.color.read_bg_4_huyan : R.color.read_bg_4;
                break;
        }
        layout.setBackground(getContext().getResources().getDrawable(color));
    }


    @Override
    protected void initData() {
        bookpage.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {
//                if (isShow) {
//                    hideReadSetting();
//                } else {
//                    showReadSetting();
//                }
            }

            @Override
            public Boolean prePage() {
//                if (isShow || isSpeaking) {
//                    return false;
//                }
//
//                pageFactory.prePage();
//                if (pageFactory.isfirstPage()) {
//                    return false;
//                }

                return true;
            }

            @Override
            public Boolean nextPage() {
                Log.e("setTouchListener", "nextPage");
                Log.e("setTouchListener", "nextPage");
//                if (isShow || isSpeaking) {
//                    return false;
//                }

//                mPageFactory.nextPage();
                finish();
//                if (mPageFactory.islastPage()) {
//                    return false;
//                }
                return true;
            }

            @Override
            public void cancel() {
//                mPageFactory.cancelPage();
            }
        });
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}