package cc.ixcc.novelthree.jsReader.Activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.bean.BookDetailBean;
import cc.ixcc.novelthree.bean.PayChapterBean;
import cc.ixcc.novelthree.bean.RewordBean;
import cc.ixcc.novelthree.bean.RewordRuleBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.jsReader.Contract.ReadContract;
import cc.ixcc.novelthree.jsReader.Dialog.ReadSettingDialog;
import cc.ixcc.novelthree.jsReader.Dialog.VipPageDialog;
import cc.ixcc.novelthree.jsReader.adapter.CategoryAdapter;
import cc.ixcc.novelthree.jsReader.model.bean.BookChapterBean;
import cc.ixcc.novelthree.jsReader.model.bean.BookRecordBean;
import cc.ixcc.novelthree.jsReader.model.bean.CollBookBean;
import cc.ixcc.novelthree.jsReader.model.local.BookRepository;
import cc.ixcc.novelthree.jsReader.model.local.ReadSettingManager;
import cc.ixcc.novelthree.jsReader.page.PageLoader;
import cc.ixcc.novelthree.jsReader.page.PageView;
import cc.ixcc.novelthree.jsReader.page.TxtChapter;
import cc.ixcc.novelthree.jsReader.presenter.ReadPresenter;
import cc.ixcc.novelthree.jsReader.utils.BitmapUtils;
import cc.ixcc.novelthree.jsReader.utils.BrightnessUtils;
import cc.ixcc.novelthree.jsReader.utils.Constant;
import cc.ixcc.novelthree.jsReader.utils.LogUtils;
import cc.ixcc.novelthree.jsReader.utils.ScreenUtils;
import cc.ixcc.novelthree.jsReader.utils.StringUtils;
import cc.ixcc.novelthree.jsReader.utils.SystemBarUtils;
import cc.ixcc.novelthree.jsReader.utils.ToastUtils;
import cc.ixcc.novelthree.treader.util.AppUtils;
import cc.ixcc.novelthree.ui.activity.my.ThirdLoginActivity;
import cc.ixcc.novelthree.ui.activity.my.TopUpActivity;
import cc.ixcc.novelthree.ui.activity.my.vip.OpenVipActivity;
import cc.ixcc.novelthree.ui.dialog.AddShelveDialog;
import cc.ixcc.novelthree.ui.dialog.RewordBottomDialog;
import cc.ixcc.novelthree.ui.dialog.TipDialog;
import cc.ixcc.novelthree.utils.GsonUtils;
import cc.ixcc.novelthree.utils.KtUtils;
import cc.ixcc.novelthree.utils.SpUtil;
import cc.ixcc.novelthree.utils.StringUtil;
import com.tencent.mmkv.MMKV;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.ixcc.novelthree.view.DialogView;

import static android.view.View.GONE;
import static android.view.View.LAYER_TYPE_SOFTWARE;
import static android.view.View.VISIBLE;

import org.jetbrains.annotations.NotNull;

/**
 * Created by newbiechen on 17-5-16.
 */
public class JsReadActivity extends BaseMVPActivity<ReadContract.Presenter> implements ReadContract.View {
    private static final String TAG = "JsReadActivity";
    public static final int REQUEST_MORE_SETTING = 1;
    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";
    public static final String START_CHAPTER = "start_chapter";
    public static final String SHELVE = "is_shelve";
    public static final String ISCATALOGUE = "is_catalogue";

    // 注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI = Settings.System.getUriFor("screen_auto_brightness_adj");

    private static final int WHAT_CATEGORY = 1;
    private static final int WHAT_CHAPTER = 2;

    @BindView(R.id.read_dl_slide)
    DrawerLayout mDlSlide;
    /*************top_menu_view*******************/
    @BindView(R.id.read_abl_top_menu)
    AppBarLayout mAblTopMenu;
    /***************content_view******************/
    @BindView(R.id.read_pv_page)
    PageView mPvPage;
    /***************bottom_menu_view***************************/
    @BindView(R.id.read_tv_page_tip)
    TextView mTvPageTip;

    @BindView(R.id.read_ll_bottom_menu)
    LinearLayout mLlBottomMenu;
    @BindView(R.id.read_tv_pre_chapter)
    TextView mTvPreChapter;
    @BindView(R.id.read_sb_chapter_progress)
    SeekBar mSbChapterProgress;
    @BindView(R.id.read_tv_next_chapter)
    TextView mTvNextChapter;
    @BindView(R.id.read_tv_category)
    TextView mTvCategory;
    @BindView(R.id.read_tv_night_mode)
    TextView mTvNightMode;
    @BindView(R.id.flContainer)
    LinearLayout mFlContainer;

    @BindView(R.id.read_tv_dashang)
    TextView ReadtvDashang;

    @BindView(R.id.read_tv_setting)
    TextView mTvSetting;
    /***************left slide*******************************/
    @BindView(R.id.read_iv_category)
    ListView mLvCategory;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tvBookName)
    TextView tvBookName;
    @BindView(R.id.tvAuth)
    TextView tvAuth;
    @BindView(R.id.tvTotalPage)
    TextView tvTotalPage;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_listen)
    ImageView ivLister;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    private boolean order = true;
    /*****************view******************/
    private ReadSettingDialog mSettingDialog;
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CategoryAdapter mCategoryAdapter;
    private CollBookBean mCollBook;
    private boolean isShelve;
    private int StartChapter;
    private TxtChapter mStartChapter;
    private long startReadTime;
    private Context mContext;
    private MMKV mmkv = MMKV.defaultMMKV();
    private UserBean mInfoBean;
    private VipPageDialog vipPageDialog;
    private Bitmap adBitmap;
    private List<Bitmap> adBitmapList = new ArrayList<>();


    private  UserBean userBean1;

    private int readMills = 0;
    private CountDownTimer cdt = new CountDownTimer(2400000000L, 1000L) {
        @Override
        public void onTick(long millisUntilFinished) {
            readMills++;
        }

        @Override
        public void onFinish() {

        }
    };

    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_CATEGORY:
                    mLvCategory.setSelection(mPageLoader.getChapterPos());
                    break;
                case WHAT_CHAPTER:
                    mPageLoader.openChapter();
                    break;
            }
        }
    };

    // 接收电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageLoader.updateBattery(level);
            }
            // 监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                mPageLoader.updateTime();
            }
        }
    };

    // 由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            // 判断当前是否跟随屏幕亮度，如果不是则返回
            if (selfChange || !mSettingDialog.isBrightFollowSystem()) return;

            // 如果系统亮度改变，则修改当前 Activity 亮度
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "亮度模式改变");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(JsReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(JsReadActivity.this, BrightnessUtils.getScreenBrightness(JsReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(JsReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setDefaultBrightness(JsReadActivity.this);
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

    /***************params*****************/
    private boolean isCollected = false; // isFromSDCard
    private boolean is = false; // isFromSDCard
    private boolean isNightMode = false;
    private boolean isFullScreen = false;
    private boolean isRegistered = false;
    private String mBookId;
    private boolean isFirstLoadAd=true;

    public static JsReadActivity jsReadActivity = null;
    public static void startActivity(Context context, CollBookBean collBook, boolean isCollected) {
        Log.e("tagfuck", "isCollected: " + isCollected + " ,startChapter: ");
        context.startActivity(new Intent(context, JsReadActivity.class)
                .putExtra(EXTRA_IS_COLLECTED, isCollected)
                .putExtra(EXTRA_COLL_BOOK, collBook));
    }

    public static void startActivity(Context context, CollBookBean collBook, boolean isCollected, int startChapter, String shelve, boolean isCatalogue) {
        Log.e("tagfuck", "isCollected: " + isCollected + " ,startChapter: " + startChapter);
        context.startActivity(new Intent(context, JsReadActivity.class)
                .putExtra(EXTRA_IS_COLLECTED, isCollected)
                .putExtra(EXTRA_COLL_BOOK, collBook)
                .putExtra(START_CHAPTER, startChapter)
                .putExtra(SHELVE, shelve)
                .putExtra(ISCATALOGUE, isCatalogue)
        );
    }

    @Override
    protected int getContentId()
    {
        return R.layout.activity_js_read;
    }

    @Override
    protected ReadContract.Presenter bindPresenter() {
        return new ReadPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mCollBook = getIntent().getParcelableExtra(EXTRA_COLL_BOOK);
        isCollected = getIntent().getBooleanExtra(EXTRA_IS_COLLECTED, false);
        StartChapter = getIntent().getIntExtra(START_CHAPTER, 0);
        isShelve = TextUtils.equals(getIntent().getStringExtra(SHELVE), "1");
        isNightMode = ReadSettingManager.getInstance().isNightMode();
        isFullScreen = ReadSettingManager.getInstance().isFullScreen();
        mBookId = mCollBook.get_id();

        if (mContext == null) {
            mContext = this;
        }
        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        inquireCoin();

    }


    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        //设置标题
        toolbar.setTitle(mCollBook.getTitle());
        tvTitleName.setText(mCollBook.getTitle());
        //半透明化StatusBar
        SystemBarUtils.transparentStatusBar(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mFlContainer.setVisibility(GONE);

        ivLister.setVisibility(Constants.IV_SHOW_LISTERVIEW?VISIBLE:GONE);
        startReadTime = System.currentTimeMillis();

        // 如果 API < 18 取消硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPvPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        //获取页面加载器
        mPageLoader = mPvPage.getPageLoader(mCollBook, StartChapter);

        for(int num=0;num< mCollBook.getBookChapters().size();num++){

            Log.d(TAG, "initWidget: "+mCollBook.getBookChapters().get(num).getIs_pay());

        }

        for(int num=0;num< mPageLoader.getChapterCategory().size();num++){

            Log.d(TAG, "initWidget: "+mPageLoader.getChapterCategory().get(num).isIs_pay());

        }


        //禁止滑动展示DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //侧边打开后，返回键能够起作用
        mDlSlide.setFocusableInTouchMode(false);
        mSettingDialog = new ReadSettingDialog(this, mPageLoader);

        setUpCategory();
        setUpAdapter();

        //夜间模式按钮的状态
        toggleNightMode();

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

        //设置当前Activity的Brightness
        //todo 亮度
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setDefaultBrightness(this);
        }
        else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "ireader:keep bright");

        //隐藏StatusBar
        mPvPage.post(() -> hideSystemBar()
        );

        //初始化TopMenu
        initTopMenu();

        //初始化BottomMenu
        initBottomMenu();
    }

    //初始化目录
    private void setUpCategory() {
        Glide.with(this).load(mCollBook.getCoverPic()).into(ivCover);
        tvBookName.setText(mCollBook.getTitle());
        tvTitleName.setText(mCollBook.getTitle());
        tvAuth.setText(mCollBook.getAuthor());
        tvTotalPage.setText((mCollBook.getIswz() == Constants.lianzai ? "Serialize" : "over") + " " + " " + mCollBook.getAllchapter() + " chapter");
        tvOrder.setText(order ? "Reverse order" : "Positive order");
    }

    private void initTopMenu() {
        if (Build.VERSION.SDK_INT >= 19) {
            mAblTopMenu.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        }
    }

    private void initBottomMenu() {
        //判断是否全屏
        if (ReadSettingManager.getInstance().isFullScreen()) {
            //还需要设置mBottomMenu的底部高度
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
            mLlBottomMenu.setLayoutParams(params);
        }
        else {
            //设置mBottomMenu的底部距离
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
            params.bottomMargin = 0;
            mLlBottomMenu.setLayoutParams(params);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: " + mAblTopMenu.getMeasuredHeight());
    }

    private void toggleNightMode() {
        if (isNightMode) {
            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_morning));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_morning);
            mTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
        else {
            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_night));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_night);
            mTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private void setUpAdapter() {
        UserBean bean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        boolean b = false;
        if (bean != null) {
            b = TextUtils.equals(bean.getIs_vip(), "1");
        }

        mCategoryAdapter = new CategoryAdapter(b);
        mLvCategory.setAdapter(mCategoryAdapter);
        mLvCategory.setFastScrollEnabled(true);
    }

    // 注册亮度观察者
    private void registerBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (!isRegistered) {
                    final ContentResolver cr = getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    isRegistered = true;
                }
            }
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "register mBrightObserver error! " + throwable);
        }
    }

    //解注册
    private void unregisterBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (isRegistered) {
                    getContentResolver().unregisterContentObserver(mBrightObserver);
                    isRegistered = false;
                }
            }
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }

    private int lastPage = -1;
    private int curPage = -1;
    private CollBookBean payBookBean;

    @Override
    protected void initClick() {
        super.initClick();

        mPageLoader.setOnPageChangeListener(
                //加载器监听
                new PageLoader.OnPageChangeListener() {
                    @Override
                    public void onChapterChange(int pos) {
                        mCategoryAdapter.setChapter(pos);
                    }

                    @Override
                    public void requestChapters(List<TxtChapter> requestChapters) {
                        mPresenter.loadChapter(mBookId, requestChapters);
                        mHandler.sendEmptyMessage(WHAT_CATEGORY);
                        //隐藏提示
                        mTvPageTip.setVisibility(GONE);
                    }

                    @Override
                    public void onCategoryFinish(List<TxtChapter> chapters) {
                        for (TxtChapter chapter : chapters) {
                            chapter.setTitle(chapter.getTitle());
                        }
                        mCategoryAdapter.refreshItems(chapters);
                    }

                    @Override
                    public void onPageCountChange(int count) {
                        mSbChapterProgress.setMax(Math.max(0, count - 1));
                        mSbChapterProgress.setProgress(0);
                        // 如果处于错误状态，那么就冻结使用
                        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING
                                || mPageLoader.getPageStatus() == PageLoader.STATUS_ERROR) {
                            mSbChapterProgress.setEnabled(false);
                        } else {
                            mSbChapterProgress.setEnabled(true);
                        }
                    }

                    @Override
                    public void onPageChange(int pos) {
                        mSbChapterProgress.post(() -> mSbChapterProgress.setProgress(pos)
                        );
                    }

                    @Override
                    public void onPayPage(CollBookBean mCollBook, int mCurChapterPos) {
                        Log.e("TAG", "onPayPage: " + mCurChapterPos);
                        payBookBean = mCollBook;
                        curPage = mCurChapterPos;
                    }
                }
        );

        mSbChapterProgress.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (mLlBottomMenu.getVisibility() == VISIBLE) {
                            //显示标题
                            mTvPageTip.setText((progress + 1) + "/" + (mSbChapterProgress.getMax() + 1));
                            mTvPageTip.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //进行切换
                        int pagePos = mSbChapterProgress.getProgress();
                        if (pagePos != mPageLoader.getPagePos()) {
                            mPageLoader.skipToPage(pagePos);
                        }
                        //隐藏提示
                        mTvPageTip.setVisibility(GONE);
                    }
                }
        );
        mPvPage.setVipListener(new PageView.VipListener() {
            @Override
            public void showPayDialog() {
                if (mSettingDialog != null && mSettingDialog.isShowing()) {
                    return;
                }
                if (vipPageDialog != null && vipPageDialog.isShowing()) {
                    return;
                }

                if (lastPage == curPage) {
                    return;
                } else {
                    lastPage = curPage;
                }
                if (mCollBook.getBookChapters().get(curPage).getIs_pay()) {
                    return;
                }
                if (mmkv.getBoolean(mCollBook.get_id(), false)) {
                    if (mCollBook.getBookChapters().get(curPage).getIs_pay() || mPageLoader.getChapterCategory().get(curPage).isIs_pay()) {
                        return;
                    }
                    HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                            .params("anime_id", mCollBook.get_id())
                            .params("chaps", curPage + 1)
                            .execute(new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {
                                    Log.e(TAG, "msg: " + msg + " ,chap: " + (curPage + 1) + " line606");
                                    if (code == 200) {
                                        mCollBook.getBookChapters().get(curPage).setIs_pay(true);
                                        mPageLoader.getChapterCategory().get(curPage).setIs_pay(true);
                                        mPageLoader.refreshChapterList(false);
                                    }
                                }
                            });
                }
                else {
                    showPageDialog(mCollBook, curPage);
                    if (vipPageDialog == null) {
                        return;
                    }
                    if (!vipPageDialog.isShowing()) {
                        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        vipPageDialog.setPageLoader(mPageLoader);
                        vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                        vipPageDialog.setChapter_left(GetNonePayCount());
                        vipPageDialog.show();
                        hideSystemBar();
                    }

                }
            }
        });

        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public void prePage() {
            }

            //下个页面
            @Override
            public void nextPage() {
            }

            @Override
            public void cancel() {
            }

            @Override
            public void buy() {
                if (mCollBook == null || curPage <= 0) {
                    return;
                }
                BookChapterBean bookChapterBean = mCollBook.getBookChapters().get(curPage);
                if (StringUtil.isVip(mInfoBean) || bookChapterBean.getCoin() <= 0 || bookChapterBean.getIs_pay()) {
                    return;
                }
                if (mSettingDialog != null && mSettingDialog.isShowing()) {
                    return;
                }
                if (vipPageDialog != null && vipPageDialog.isShowing()) {
                    return;
                }
                lastPage = curPage;
                showPageDialog(mCollBook, curPage);
                if (!vipPageDialog.isShowing()) {
                    mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                    vipPageDialog.setPageLoader(mPageLoader);
                    vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                    vipPageDialog.setChapter_left(GetNonePayCount());
                    vipPageDialog.show();
                    hideSystemBar();
                }
            }
        });

        //目录点击
        mLvCategory.setOnItemClickListener(
                (parent, view, position, id) -> {
                    if (StringUtil.isVip(mInfoBean)) {
                        //是会员
                        mDlSlide.closeDrawer(GravityCompat.START);
                        mPageLoader.skipToChapter(position);
                    }
                    else {
                        //非会员
                        List<TxtChapter> chapterCategory = mPageLoader.getChapterCategory();
                        TxtChapter txtChapter = chapterCategory.get(position);
                        if (txtChapter.getCoin() == 0) {
                            mDlSlide.closeDrawer(GravityCompat.START);
                            mPageLoader.skipToChapter(position);
                        }
                        else if (txtChapter.isIs_pay()) {
                            mDlSlide.closeDrawer(GravityCompat.START);
                            mPageLoader.skipToChapter(position);
                        }
                        else {
                            mDlSlide.closeDrawer(GravityCompat.START);
                            mPageLoader.skipToChapter(position);

                            //弹出购买页面
                            boolean totalAuto = mmkv.decodeBool("fuck_auto", false);
                            boolean auto = mmkv.decodeBool(mCollBook.get_id(), false);
                            if (auto && totalAuto) {
                                HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                                        .params("anime_id", mCollBook.get_id())
                                        .params("chaps", position + 1)
                                        .execute(new HttpCallback() {
                                            @Override
                                            public void onSuccess(int code, String msg, String[] info) {
                                                Log.e(TAG, "msg: " + msg + " ,chap: " + (position + 1) + " line719");
                                                if (code == 200) {
                                                    mCollBook.getBookChapters().get(position).setIs_pay(true);
                                                    mPageLoader.getChapterCategory().get(position).setIs_pay(true);
                                                    if (vipPageDialog != null && vipPageDialog.isShowing()) {
                                                        vipPageDialog.dismiss();
                                                    }
                                                    if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
                                                        mDlSlide.closeDrawer(GravityCompat.START);
                                                    }
                                                    mPageLoader.refreshChapterList(false);
                                                    mPageLoader.skipToChapter(position);
                                                    mCategoryAdapter.refreshItems(mPageLoader.getChapterCategory());
                                                }
                                                else {
                                                    showPageDialog(mCollBook, position);
                                                    if (!vipPageDialog.isShowing()) {
                                                        vipPageDialog.setChapter_left(GetNonePayCount());
                                                        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                                                        vipPageDialog.setPageLoader(mPageLoader);
                                                        vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                                                        vipPageDialog.show();
                                                        hideSystemBar();
                                                    }
                                                }
                                            }
                                        });
                            }
                            else {
                                //弹出购买界面
                                showPageDialog(mCollBook, position);
                                if (!vipPageDialog.isShowing()) {
                                    mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                                    vipPageDialog.setPageLoader(mPageLoader);
                                    vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                                    vipPageDialog.setChapter_left(GetNonePayCount());
                                    vipPageDialog.show();
                                    hideSystemBar();
                                }
                            }
                        }
                    }
                }
        );

        mTvCategory.setOnClickListener(
                (v) -> {
                    //移动到指定位置
                    if (mCategoryAdapter.getCount() > 0) {
                        mLvCategory.setSelection(mPageLoader.getChapterPos());
                    }
                    //切换菜单
                    toggleMenu(true);
                    //打开侧滑动栏
                    mDlSlide.openDrawer(GravityCompat.START);
                }
        );
        ReadtvDashang.setOnClickListener(
                (v) -> {
                    GetRewordPackage();
                }
        );
        mTvSetting.setOnClickListener(
                (v) -> {
                    toggleMenu(false);
                    mSettingDialog.show();
                }
        );

        mTvPreChapter.setOnClickListener(
                (v) -> {
                    if (mPageLoader.skipPreChapter()) {
                        mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
                    }
                }
        );

        mTvNextChapter.setOnClickListener(
                (v) -> {
                    if (mPageLoader.skipNextChapter()) {
                        mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
                    }
                }
        );

        mTvNightMode.setOnClickListener(
                (v) -> {
                    if (isNightMode) {
                        isNightMode = false;
                    } else {
                        isNightMode = true;
                    }
                    mPageLoader.setNightMode(isNightMode);
                    toggleNightMode();
                }
        );

        mSettingDialog.setOnDismissListener(
                dialog -> hideSystemBar()
        );

        ivBack.setOnClickListener(v -> {
            if(isShelve !=true){
                showAddShelveDialog();
            }else{
                finish();
            }
        });
        ivLister.setOnClickListener(v -> {
            BookChapterBean bookChapterBean = mCollBook.getBookChapterList().get(mPageLoader.getChapterPos());
            if (checkVip()) {
                ListenBookActivity.start(JsReadActivity.this, mCollBook, isShelve, mPageLoader.getChapterPos());
            }
            else if (bookChapterBean.getIs_pay() || bookChapterBean.getCoin() <= 0) {
                ListenBookActivity.start(JsReadActivity.this, mCollBook, isShelve, mPageLoader.getChapterPos());
            }
            else {
                showPageDialog(mCollBook, mPageLoader.getChapterPos());
                if (vipPageDialog == null) {
                    return;
                }
                if (!vipPageDialog.isShowing()) {
                    mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                    vipPageDialog.setPageLoader(mPageLoader);
                    vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                    vipPageDialog.setChapter_left(GetNonePayCount());
                    vipPageDialog.show();
                    hideSystemBar();
                }
            }
        });
        ivShare.setOnClickListener(v -> {
            SampleSHare();
        });
    }

    AddShelveDialog dialog;

    private List<RewordBean> rewordBeanList;
    private RewordBottomDialog rewordBottomDialog = null;
    int anime_id;

    private void GetRewordPackage() {
        try {
            if (rewordBottomDialog == null) {
                HttpClient.getInstance().post(AllApi.rewordPackage, AllApi.rewordPackage)
                        .execute(new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                RewordRuleBean rewordRuleBean = GsonUtils.parseJObject(info[0], RewordRuleBean.class);
                                rewordBeanList = rewordRuleBean.getList();
                                ShowRewordDialog();
                            }
                        });
            } else {
                ShowRewordDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowRewordDialog() {
        if (rewordBottomDialog == null) {
            rewordBottomDialog = new RewordBottomDialog(getContext(), rewordBeanList);
            rewordBottomDialog.setCancelable(true);
            rewordBottomDialog.setCanceledOnTouchOutside(true);
            rewordBottomDialog.setDialogCallBack(new RewordBottomDialog.RewordDialogCallBack() {
                @Override
                public void rewordClick(@NotNull String coin) {
                    commitReword(coin);
                }
            });
            rewordBottomDialog.show();
        } else {
            rewordBottomDialog.show();
        }

    }

    private Context getContext() {
        return mContext;
    }

    private void commitReword(String coin) {
        HttpClient.getInstance().post(AllApi.commitReword, AllApi.commitReword)
                .params("anid", mCollBook.get_id())
                .params("coin", coin)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        rewordBottomDialog.dismiss();
                        toast(msg);
                        getInfo(mCollBook.get_id());
                    }
                });
    }

    private void toast(CharSequence text) {

        ToastUtils.show((String) text);
    }

    private  void showAddShelveDialog(){
        dialog = AddShelveDialog.getMyDialog(JsReadActivity.this);
        dialog.setDialogCallBack(new AddShelveDialog.DialogCallBack() {
            @Override
            public void onActionClick(String code) {
                if (dialog!=null){
                    dialog.dismiss();
                }
                addshelve();
            }
            @Override
            public void onCancelClick() {
                if (dialog!=null){
                    dialog.dismiss();
                }
                finish();
            }
        });
        dialog.show();
    }

    private int GetNonePayCount()
    {
        List<TxtChapter> chapterCategory = mPageLoader.getChapterCategory();
        //先计算还有多少章节没有购买
        int nonePayChapterCount = 0;
        for (TxtChapter item: chapterCategory) {
            if (item.getCoin() > 0 && !item.isIs_pay()) {
                nonePayChapterCount++;
            }
        }
        return nonePayChapterCount;
    }

    private void CommonShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos.isEmpty()) {
            return;
        }
        List<Intent> targetIntents = new ArrayList<>();
        for (ResolveInfo info : resolveInfos) {
            ActivityInfo ainfo = info.activityInfo;
            switch (ainfo.packageName) {
                case "com.tencent.mm":
                    addShareIntent(targetIntents, ainfo);
                    break;
                case "com.tencent.mobileqq":
                    addShareIntent(targetIntents, ainfo);
                    break;
                case "com.sina.weibo":
                    addShareIntent(targetIntents, ainfo);
                    break;
            }
        }
        if (targetIntents == null || targetIntents.size() == 0) {
            return;
        }
        Intent chooserIntent = Intent.createChooser(targetIntents.remove(0), "请选择分享平台");
        if (chooserIntent == null) {
            return;
        }
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
        try {
            startActivity(chooserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "找不到该分享应用组件", Toast.LENGTH_SHORT).show();
        }
        //startActivity(Intent.createChooser(intent, getTitle()));
    }

    private void addShareIntent(List<Intent> list, ActivityInfo ainfo) {
        Intent target = new Intent(Intent.ACTION_SEND);
        target.setType("text/plain");
        target.putExtra(Intent.EXTRA_TEXT, mCollBook.getShareTitle());
        target.putExtra(Intent.EXTRA_TITLE, mCollBook.getShareTitle());
        target.putExtra(Intent.EXTRA_SUBJECT, mCollBook.getShareTitle());
        target.setPackage(ainfo.packageName);
        target.setClassName(ainfo.packageName, ainfo.name);
        list.add(target);
    }

    private void SampleSHare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mCollBook.getShareTitle());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public Bitmap createBitmap3(View v, int width, int height) {
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bitmap;
    }

    private void createBitmap(View view) {
        int w = ScreenUtils.getDisplayMetrics().widthPixels - ScreenUtils.dpToPx(40);
        view.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec((w - 40) * 2 / 3, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        adBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(adBitmap);
        c.drawColor(Color.WHITE);
        view.draw(c);
        if (adBitmap == null) {
            return;
        }
        Bitmap nextBitmap = mPvPage.getNextBitmap();
        if (nextBitmap != null) {
            Bitmap bitmap = BitmapUtils.combineBitmap(nextBitmap, adBitmap);
            mPvPage.notify();
        }
    }

    private void showPageDialog(CollBookBean mCollBook, int mCurChapterPos) {
        if (mCurChapterPos < 0) {
            return;
        }
        if (vipPageDialog == null) {
            vipPageDialog = new VipPageDialog(JsReadActivity.this, mCollBook, mCurChapterPos);
            vipPageDialog.setPageLoader(mPageLoader);
            vipPageDialog.setCancelable(false);
            vipPageDialog.setPayClickable(true);
            vipPageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideSystemBar();
                }
            });
            vipPageDialog.payListener = new VipPageDialog.PayDialogListener() {
                @Override
                public void dialogClose(boolean auto) {
                    Log.e(TAG, "auto: " + auto);
                }

                @Override
                public void byCurrent(int currentPos, int buyChapterCount) {
                    if (vipPageDialog.getPayClickable()) {
                        byPage(vipPageDialog, currentPos, buyChapterCount);
                    }
                    vipPageDialog.setPayClickable(false);
                }

                @Override
                public void goOpen() {
                    OpenVipActivity.start(JsReadActivity.this);
                }
            };
        }
        else {
            vipPageDialog.setPageLoader(mPageLoader);
            vipPageDialog.setBook(mCollBook);
            vipPageDialog.setCurrent(mCurChapterPos);
            vipPageDialog.setPayClickable(true);
            vipPageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideSystemBar();
                }
            });
            vipPageDialog.payListener = new VipPageDialog.PayDialogListener() {
                @Override
                public void dialogClose(boolean auto) {
                }

                @Override
                public void byCurrent(int current, int buyChapterCount) {
                    if (vipPageDialog.getPayClickable()) {
                        byPage(vipPageDialog, current, buyChapterCount);
                    }
                    vipPageDialog.setPayClickable(false);
                }

                @Override
                public void goOpen() {
                    OpenVipActivity.start(JsReadActivity.this);
                }
            };
        }
    }

    //购买章节
    private void byPage(VipPageDialog vipPageDialog, int current, int buyChapterCount) {
        HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                .params("anime_id", mCollBook.get_id())
                .params("chaps", current + 1)
                .params("chaps_count", buyChapterCount)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        Log.e(TAG, "msg: " + msg + " ,chap: " + (current + 1) + " line895");

                        try {
                            if (code == 200) {
                                ToastUtils.show("Purchase successful");

                                PayChapterBean bean = new Gson().fromJson(info[0], PayChapterBean.class);
                                mInfoBean.setCoin(bean.getCoins());
                                MMKV.defaultMMKV().encode(SpUtil.USER_INFO, mInfoBean);

                                if (buyChapterCount != 100) {
                                    for (int i = 0; i < buyChapterCount; i++) {
                                        if ((current + i) < mCollBook.getBookChapters().size()) {
                                            mCollBook.getBookChapters().get(current + i).setIs_pay(true);
                                            mPageLoader.getChapterCategory().get(current + i).setIs_pay(true);
                                        } else {
                                            for (BookChapterBean item : mCollBook.getBookChapters()) {
                                                if (!item.getIs_pay() && item.getCoin() > 0) {
                                                    item.setIs_pay(true);
                                                    break;
                                                }
                                            }

                                            for (TxtChapter item : mPageLoader.getChapterCategory()) {
                                                if (!item.isIs_pay() && item.getCoin() > 0) {
                                                    item.setIs_pay(true);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    //购买整本书
                                    for (BookChapterBean item : mCollBook.getBookChapters()) {
                                        item.setIs_pay(true);
                                    }

                                    for (TxtChapter item : mPageLoader.getChapterCategory()) {
                                        item.setIs_pay(true);
                                    }
                                }

                                //隐藏购买界面
                                OnHidePayPage();
//                        if (JsReadActivity.this.vipPageDialog != null && JsReadActivity.this.vipPageDialog.isShowing()) {
//                            JsReadActivity.this.vipPageDialog.dismiss();
//                        }
                                if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
                                    mDlSlide.closeDrawer(GravityCompat.START);
                                }

                                if (vipPageDialog.isAuto()) {
                                    mmkv.encode("fuck_auto", true);
                                }
                                mmkv.encode(mCollBook.get_id(), vipPageDialog.isAuto());
                                mPageLoader.refreshChapterList(false);
                                mPageLoader.skipToChapter(current);
                            } else {
                                if (JsReadActivity.this.vipPageDialog != null) {
                                    JsReadActivity.this.vipPageDialog.setPayClickable(true);
                                }
                                //隐藏购买界面
                                //OnHidePayPage();

                                ToastUtils.show("Purchase failed " + Constants.getInstance().getConfig().getCoin_name() + " not enough");
                                UserBean userBean1 = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);

                                if (userBean1.getBindStatus().equals("0")) {
                                    ThirdLoginActivity.start(JsReadActivity.this, ThirdLoginActivity.EnterIndex.PAY);
                                } else {
                                    startActivity(TopUpActivity.class);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("JsReadActivity", "JsReadActivity: " + e);
                        }
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        if (JsReadActivity.this.vipPageDialog != null) {
                            JsReadActivity.this.vipPageDialog.setPayClickable(true);
                        }
                    }
                });
    }

    public static void OnHidePayPage()
    {
        if (jsReadActivity.vipPageDialog != null && jsReadActivity.vipPageDialog.isShowing()) {
            jsReadActivity.vipPageDialog.dismiss();
        }
    }

    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {
        hideSystemBar();
        if (mAblTopMenu.getVisibility() == VISIBLE) {
            toggleMenu(true);
            return true;
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return true;
        }
        return false;
    }

    private void showSystemBar() {
        //显示
        SystemBarUtils.showUnStableStatusBar(this);
        if (isFullScreen) {
            SystemBarUtils.showUnStableNavBar(this);
        }
    }

    private void hideSystemBar() {
        //隐藏
        SystemBarUtils.hideStableStatusBar(this);
        if (isFullScreen) {
            SystemBarUtils.hideStableNavBar(this);
        }
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();

        if (mAblTopMenu.getVisibility() == VISIBLE) {
            //关闭
            mAblTopMenu.startAnimation(mTopOutAnim);
            mLlBottomMenu.startAnimation(mBottomOutAnim);
            mAblTopMenu.setVisibility(GONE);
            mLlBottomMenu.setVisibility(GONE);
            mTvPageTip.setVisibility(GONE);

            if (hideStatusBar) {
                hideSystemBar();
            }
        } else {
            mAblTopMenu.setVisibility(VISIBLE);
            mLlBottomMenu.setVisibility(VISIBLE);
            mAblTopMenu.startAnimation(mTopInAnim);
            mLlBottomMenu.startAnimation(mBottomInAnim);

            showSystemBar();
        }
    }

    //初始化菜单动画
    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        List<BookChapterBean> allChapters = mCollBook.getBookChapters();
        //显示章节
        showCategory(allChapters);
    }

    /***************************view************************************/
    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showCategory(List<BookChapterBean> bookChapters) {
        mPageLoader.getCollBook().setBookChapters(bookChapters);
        mPageLoader.refreshChapterList(false);

        // 如果是目录更新的情况，那么就需要存储更新数据
        if (mCollBook.isUpdate() && isCollected) {
            BookRepository.getInstance().saveBookChaptersWithAsync(bookChapters);
        }
    }

    @Override
    public void finishChapter() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mHandler.sendEmptyMessage(WHAT_CHAPTER);
        }
        // 当完成章节的时候，刷新列表
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorChapter() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mPageLoader.chapterError();
        }
    }

    @Override
    public void upDate(String chaps) {
        try {
            int chap = Integer.parseInt(chaps);
            Log.e(TAG, "upDate chap: " + chap);
            mCollBook.getBookChapters().get(chap - 1).setIs_pay(true);
            BookRepository.getInstance().saveBookChaptersWithAsync(mCollBook.getBookChapters());
            mPageLoader.getChapterCategory().get(chap - 1).setIs_pay(true);
            mPageLoader.refreshChapterList(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mAblTopMenu.getVisibility() == VISIBLE) {
            // 非全屏下才收缩，全屏下直接退出
            if (!ReadSettingManager.getInstance().isFullScreen()) {
                toggleMenu(true);
                return;
            }
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return;
        } else if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
            mDlSlide.closeDrawer(GravityCompat.START);
            return;
        }

        Log.e(TAG, "isLocal: " + mCollBook.isLocal() + " , isCollected:" + isCollected);
        isShelve = true;
        if (!isShelve) {
            TipDialog myDialog = TipDialog.getMyDialog(this);
            myDialog.setTitle("Add");
            myDialog.setSubmit("OK");
            myDialog.setCancel("Cancel");
            myDialog.setMessage("If you like the book, add it to Library");
            myDialog.setDialogCallBack(new TipDialog.DialogCallBack() {
                @Override
                public void onActionClick() {
                    if (AppUtils.isLogin()) {
                        //设置为已收藏
                        isCollected = true;
                        //设置阅读时间
                        mCollBook.setLastRead(StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
                        addshelve();
                        BookRepository.getInstance().saveCollBookWithAsync(mCollBook);
                    }
                    else {
                        MMKV mmkv = MMKV.defaultMMKV();
                        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        if (userBean.getBindStatus().equals("0")) {
                            ThirdLoginActivity.start(getApplicationContext(), ThirdLoginActivity.EnterIndex.OTHER);
                            return;
                        }
                    }
                }
            });
            myDialog.setCancelCallBack(new TipDialog.DialogCancelCallBack() {
                @Override
                public void onCancel() {
                    exit();
                }
            });
            myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideSystemBar();
                }
            });

            myDialog.show();
        } else {
            exit();
        }
    }

    //加入书架
    private void addshelve() {
        HttpClient.getInstance().get(AllApi.addshelve, AllApi.addshelve)
                .params("anime_id", mCollBook.get_id())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        ToastUtils.show(msg);
                        exit();
                    }
                });
    }

    // 退出
    private void exit() {
        boolean login = AppUtils.isLogin();
        Log.e(TAG, "login: " + login);
        if (login) {
            Log.e(TAG, "msg: 上传时间");
            HttpClient.getInstance().get(AllApi.updatereadtime, AllApi.updatereadtime)
                    .params("time", readMills)
                    .execute(new HttpCallback() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            Log.e(TAG, "code: " + code + " ,msg: " + msg);
                            try {
                                finish();
                                /*
                                if (msg.equals("Successful operation")) {
                                    mCommitView = DialogManager.getInstance().initView(BookDetailActivity.mBookDetailActivity, R.layout.dialog_notice_gain);
                                    TextView txt1 = mCommitView.findViewById(R.id.updataversion_title);
                                    txt1.setText("Congratulations on completing \n the welfare task");
                                    txt1.setTextSize(18);

                                    TextView goldMount = mCommitView.findViewById(R.id.updataversion_name);
                                    goldMount.setText("Click [Me] menu to view and receive");

                                    TextView tv_confirm = mCommitView.findViewById(R.id.tv_confirm);
                                    tv_confirm.setText("OK");
                                    tv_confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DialogManager.getInstance().hide(mCommitView);
                                        }
                                    });
                                    DialogManager.getInstance().show(mCommitView);
                                }
                                */
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            super.onBackPressed();
        }
    }
    private DialogView mCommitView;

    //TODO 获取用户信息
    private void inquireCoin() {
        String token = mmkv.decodeString(SpUtil.TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            mInfoBean = new UserBean();
            mInfoBean.setIs_vip("0");
            mmkv.encode(SpUtil.USER_INFO, mInfoBean);
        }
        else {
            HttpClient.getInstance().get(AllApi.userinfo, AllApi.userinfo)
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            mInfoBean = new Gson().fromJson(info[0], UserBean.class);
                            mmkv.encode(SpUtil.USER_INFO, mInfoBean);
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
    }

    public void getInfo(String anime_id) {
        HttpClient.getInstance().get(AllApi.bookinfo, AllApi.bookinfo)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        BookDetailBean bookDetailBean = new Gson().fromJson(info[0], BookDetailBean.class);
                        isShelve = bookDetailBean.getIs_shelve().equals("1");
                    }
                });
    }

    private void ResumeChapter() {
        BookRecordBean mBookRecord = BookRepository.getInstance().getBookRecord(mCollBook.get_id());
        if (mBookRecord != null && mPageLoader.getChapterPos() != mBookRecord.getChapter()) {
            mPageLoader.skipToChapter(mBookRecord.getChapter());
        }
    }

//    public static void OnReshPayChapterUI()
//    {
//        try{
//            //如果购买界面显示，刷新购买界面
//            if (jsReadActivity!= null && jsReadActivity.vipPageDialog != null && jsReadActivity.vipPageDialog.isShowing()) {
//                jsReadActivity.vipPageDialog.setOwnCoins(mInfoBean.getCoin());
//                jsReadActivity.vipPageDialog.show();
//            }
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }

    @Override
    protected void onResume() {
        jsReadActivity = this;
        super.onResume();
        if (mCollBook != null) {
            getInfo(mCollBook.get_id());
        }
        if (cdt != null) {
            cdt.start();
        }
        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        //检测是否是Vip
        boolean isVip = checkVip();
        if (isVip) {
            if (mPageLoader != null) {
                mPageLoader.refreshChapterList(true);
            }
            if (vipPageDialog != null && vipPageDialog.isShowing()) {
                vipPageDialog.dismiss();
            }
        }

        //如果购买界面显示，刷新购买界面
        if (vipPageDialog != null && vipPageDialog.isShowing()) {
            vipPageDialog.setOwnCoins(mInfoBean.getCoin());
            vipPageDialog.show();
        }

        mWakeLock.acquire();
        hideSystemBar();
        ResumeChapter();
        isFirstLoadAd = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        mPageLoader.saveRecord();

        if (cdt != null) {
            cdt.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);

        mHandler.removeMessages(WHAT_CATEGORY);
        mHandler.removeMessages(WHAT_CHAPTER);

        mPageLoader.closeBook();
        mPageLoader = null;
        if (cdt != null) {
            cdt.cancel();
            cdt = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isVolumeTurnPage = ReadSettingManager.getInstance().isVolumeTurnPage();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (isVolumeTurnPage) {
                    return mPageLoader.skipToPrePage();
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (isVolumeTurnPage) {
                    return mPageLoader.skipToNextPage();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SystemBarUtils.hideStableStatusBar(this);
        if (requestCode == REQUEST_MORE_SETTING) {
            boolean fullScreen = ReadSettingManager.getInstance().isFullScreen();
            if (isFullScreen != fullScreen) {
                isFullScreen = fullScreen;
                // 刷新BottomMenu
                initBottomMenu();
            }

            // 设置显示状态
            if (isFullScreen) {
                SystemBarUtils.hideStableNavBar(this);
            } else {
                SystemBarUtils.showStableNavBar(this);
            }
        }
    }

    //是否是vip
    private boolean checkVip() {
        UserBean bean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        if (bean != null && bean.getIs_vip() != null) {
            return StringUtil.isVip(bean);
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private long lastClick = 0;

    @OnClick(R.id.tvOrder)
    public void onViewClicked() {
        long millis = System.currentTimeMillis();
        if (millis - lastClick < 500) {
            return;
        }
        lastClick = millis;
        if (order) {
            tvOrder.setText("Positive order");
            List<TxtChapter> items = mCategoryAdapter.getItems();
            mCategoryAdapter.refreshItems(KtUtils.reverse(items));
        } else {
            tvOrder.setText("Reverse order");
            List<TxtChapter> items = mCategoryAdapter.getItems();
            mCategoryAdapter.refreshItems(KtUtils.reverse(items));
        }
    }
}


