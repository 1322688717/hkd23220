package cc.ixcc.novelthree.treader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import cc.ixcc.novelthree.Constants;

import com.tencent.mmkv.MMKV;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.ChapterBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.treader.base.BaseActivity;
import cc.ixcc.novelthree.treader.db.BookList;
import cc.ixcc.novelthree.treader.dialog.LightSettingDialog;
import cc.ixcc.novelthree.treader.dialog.PageModeDialog;
import cc.ixcc.novelthree.treader.dialog.SettingDialog;
import cc.ixcc.novelthree.treader.util.AppUtils;
import cc.ixcc.novelthree.treader.util.BrightnessUtil;
import cc.ixcc.novelthree.treader.util.PageFactory;
import cc.ixcc.novelthree.treader.view.PageWidget;
import cc.ixcc.novelthree.ui.activity.my.ThirdLoginActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.BookDirectoryAdapter;
import cc.ixcc.novelthree.ui.dialog.AddShelveDialog;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.ixcc.novelthree.utils.SpUtil;

import static cc.ixcc.novelthree.treader.Config.BOOK_BG_NIGHT;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class ReadActivity extends BaseActivity {
    private static final String TAG = "ReadActivity";
    private final static String EXTRA_BOOK = "bookList";
    private final static int MESSAGE_CHANGEPROGRESS = 1;

    @BindView(R.id.bookpage)
    PageWidget bookpage;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.tv_pre)
    TextView tv_pre;
    @BindView(R.id.sb_progress)
    SeekBar sb_progress;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.tv_directory)
    TextView tv_directory;
    @BindView(R.id.tv_dayornight)
    TextView tv_dayornight;
    @BindView(R.id.tv_pagemode)
    TextView tv_pagemode;
    @BindView(R.id.tv_setting)
    TextView tv_setting;
    @BindView(R.id.bookpop_bottom)
    LinearLayout bookpop_bottom;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
//    @BindView(R.id.tv_stop_read)
//    TextView tv_stop_read;
    @BindView(R.id.rl_read_bottom)
    RelativeLayout rl_read_bottom;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.book_img)
    RoundedImageView book_img;
    @BindView(R.id.book_name)
    TextView book_name;
    @BindView(R.id.author_name)
    TextView author_name;
    @BindView(R.id.zhang)
    TextView zhang;
    @BindView(R.id.sort)
    TextView sort;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.rv_directory)
    WrapRecyclerView rv_directory;
    @BindView(R.id.directory_layout)
    LinearLayout directory_layout;
    BookDirectoryAdapter mDirectoryAdapter;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
//    @BindView(R.id.author_img)
//    ImageView authorImg;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;

    private Config config;
    private WindowManager.LayoutParams lp;
    private BookList bookList;
    private PageFactory pageFactory;
    private int screenWidth, screenHeight;

    // popwindow是否显示
    private Boolean isShow = false;
    private SettingDialog mSettingDialog;
    private LightSettingDialog mLightSettingDialog;
    private PageModeDialog mPageModeDialog;
    private Boolean mDayOrNight;

    //private boolean isSpeaking = false;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<ChapterBean.ChaptersBean> directoryList = new ArrayList<>();
    private ChapterBean chapterBean;
    boolean isSort = false; //false :正序 true:倒叙

    boolean isAddShelve = false;
    boolean isFirst = true;
    static boolean isCache = true; //是否从章节上次阅读的位置开始
    private boolean bStart = false;
    private Handler mHander = new Handler();
    private int mCount = 0;
    private int shelveicon = R.mipmap.icon_add_sj;

    // 接收电池信息更新的广播
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
            Log.e(TAG, Intent.ACTION_BATTERY_CHANGED);
            int level = intent.getIntExtra("level", 0);
            pageFactory.updateBattery(level);
        } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            Log.e(TAG, Intent.ACTION_TIME_TICK);
            pageFactory.updateTime();
        }
        }
    };

    private Runnable mCounter = new Runnable() {
        @Override
        public void run() {
            mCount++;
            mHander.postDelayed(this, 1000);
        }
    };

    @Override
    public int getLayoutRes() {
        return R.layout.activity_read;
    }

    public static void setIsCache(boolean cache) {
        isCache = cache;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 30) {
            bookpage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishShowShelveDialog();
            }
        });

        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();

        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);

        mSettingDialog = new SettingDialog(this);
        mLightSettingDialog = new LightSettingDialog(this);
        mPageModeDialog = new PageModeDialog(this);
        //获取屏幕宽高
        WindowManager manage = getWindowManager();
        Display display = manage.getDefaultDisplay();
        Point displaysize = new Point();
        display.getSize(displaysize);
        screenWidth = displaysize.x;
        screenHeight = displaysize.y;
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //隐藏
        hideSystemUI();
        //改变屏幕亮度
        if (!config.isSystemLight()) {
            BrightnessUtil.setBrightness(this, config.getLight());
        }
        //获取intent中的携带的信息
        Intent intent = getIntent();
        bookList = (BookList) intent.getSerializableExtra(EXTRA_BOOK);

        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);
        isAddShelve = (bookList.getIs_shelve()).equals(Constants.SHELVE_EXIST);
        setShelveBtn();
        try {
            pageFactory.openBook(bookList, isCache);
            mHander.post(mCounter);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to open eBook", Toast.LENGTH_SHORT).show();
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //禁止侧边滑动
        drawerLayout.addDrawerListener(mDrawerToggle);
        initDayOrNight();
        getChapterList(bookList.getAnid());
    }

    public void getChapterList(int anime_id) {
        HttpClient.getInstance().post(AllApi.bookchapter, AllApi.bookchapter)
            .isMultipart(true)
            .params("anime_id", anime_id)
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                chapterBean = new Gson().fromJson(info[0], ChapterBean.class);
                directoryList.addAll(chapterBean.getChapters());
                PageFactory.getInstance().refresh();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initDrawLeft();
                    }
                });
                }
            });
    }

    private void initDrawLeft() {
        if (chapterBean == null) return;
        Glide.with(this)
                .load(chapterBean.getCoverpic())
//                    .load(R.mipmap.book_img1)
                .into(book_img);
        book_name.setText(chapterBean.getTitle());
        author_name.setText(chapterBean.getAuthor());
        zhang.setText((chapterBean.getIswz() == Constants.lianzai ? getString(R.string.lianzai) : getString(R.string.wanjie)) + " 共" + chapterBean.getAllchapter() + "章节");
        mDirectoryAdapter = new BookDirectoryAdapter(this, false);
        mDirectoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                pageFactory.changeChapter(chapterBean.getChapters().get(position).getChaps());
                drawerLayout.closeDrawer(Gravity.LEFT);
                hideReadSetting();
            }
        });
        SortChapter();
        rv_directory.setAdapter(mDirectoryAdapter);
//        rv_directory.setNestedScrollingEnabled(false); //禁止滑动
        mDirectoryAdapter.setData(chapterBean.getChapters());
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSort = !isSort;
                SortChapter();
            }
        });
    }

    private void SortChapter() {
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setStackFromEnd(isSort);//列表再底部开始展示，反转后由上面开始展示
        layout.setReverseLayout(isSort);//列表翻转
        rv_directory.setLayoutManager(layout);
        sort.setText(isSort ? "positive order" : "reverse order");
    }

    boolean isTouch = false;

    @Override
    protected void initListener() {
//        sb_progress.getProgressDrawable().setColorFilter(Color.parseColor("#2896F0"), PorterDuff.Mode.SRC_ATOP);
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float pro;

            // 触发操作，拖动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = (float) (progress / 10000.0);
                showProgress(pro);
//                if (!isTouch)  pageFactory.changeProgress(pro);
            }

            // 表示进度条刚开始拖动，开始拖动时候触发的操作
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
            }

            // 停止拖动时候
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pageFactory.changeProgress(pro);
                isTouch = false;
            }
        });

        mPageModeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

//        mPageModeDialog.setPageModeListener(new PageModeDialog.PageModeListener() {
//            @Override
//            public void changePageMode(int pageMode) {
//                bookpage.setPageMode(pageMode);
//            }
//        });

        mSettingDialog.setPageModeListener(new PageModeDialog.PageModeListener() {
            @Override
            public void changePageMode(int pageMode) {
                bookpage.setPageMode(pageMode);
            }
        });

        mSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });
        mLightSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideSystemUI();
            }
        });

        mLightSettingDialog.setSettingListener(new LightSettingDialog.SettingListener() {
            @Override
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if (!isSystem) {
                    BrightnessUtil.setBrightness(ReadActivity.this, brightness);
                } else {
                    int bh = BrightnessUtil.getScreenBrightness(ReadActivity.this);
                    BrightnessUtil.setBrightness(ReadActivity.this, bh);
                }
            }

            @Override
            public void changehuyan(Boolean isHuyan) {
                pageFactory.setDayOrNight(mDayOrNight);
                initDayOrNight();
            }

            @Override
            public void changeFontSize(int fontSize) {
                pageFactory.changeFontSize(fontSize);
            }

            @Override
            public void changeTypeFace(Typeface typeface) {
                pageFactory.changeTypeface(typeface);
            }

            @Override
            public void changeBookBg(int type) {
            }
        });

        mSettingDialog.setSettingListener(new SettingDialog.SettingListener() {
            @Override
            public void changeSystemBright(Boolean isSystem, float brightness) {
                if (!isSystem) {
                    BrightnessUtil.setBrightness(ReadActivity.this, brightness);
                } else {
                    int bh = BrightnessUtil.getScreenBrightness(ReadActivity.this);
                    BrightnessUtil.setBrightness(ReadActivity.this, bh);
                }
            }

            @Override
            public void changeFontSize(int fontSize) {
                pageFactory.changeFontSize(fontSize);
            }

            @Override
            public void changeTypeFace(Typeface typeface) {
                pageFactory.changeTypeface(typeface);
            }

            @Override
            public void changeBookBg(int type) {
                pageFactory.changeBookBg(type);
                config.setDayOrNight(false);
                initDayOrNight();
            }
        });

        pageFactory.setPageEvent(new PageFactory.PageEvent() {
            @Override
            public void changeProgress(float progress, BookList bookList2) {
                bookList = bookList2;
                Message message = new Message();
                message.what = MESSAGE_CHANGEPROGRESS;
                message.obj = progress;
                mHandler.sendMessage(message);
            }
        });

        bookpage.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {
                if (isShow) {
                    hideReadSetting();
                } else {
                    showReadSetting();
                }
            }

            @Override
            public Boolean prePage() {
                if (System.currentTimeMillis() - mLastPreTime < MAX_LONG_PRESS_TIME)
                    return true; //避免双击翻了两页
                mLastPreTime = System.currentTimeMillis();
                if (isShow) {
                    return false;
                }

                pageFactory.prePage();
                if (pageFactory.isfirstPage()) {
                    return false;
                }

                return true;
            }

            @Override
            public Boolean nextPage() {
                if (System.currentTimeMillis() - mLastNextTime < MAX_LONG_PRESS_TIME)
                    return true;  //避免双击翻了两页
                mLastNextTime = System.currentTimeMillis();
                Log.e("setTouchListener", "nextPage");
                if (isShow) {
                    return false;
                }

                pageFactory.nextPage();
                if (pageFactory.islastPage()) {
                    return false;
                }
                return true;
            }

            @Override
            public void cancel() {
                pageFactory.cancelPage();
            }
        });

    }

    long mLastPreTime = 0;
    long mLastNextTime = 0;
    private int MAX_LONG_PRESS_TIME = 350;// 长按/双击最长等待时间

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CHANGEPROGRESS:
                    float progress = (float) msg.obj;
                    setSeekBarProgress(progress);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!isShow) {
            hideSystemUI();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageFactory.clear();
        bookpage = null;
        unregisterReceiver(myReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow) {
                hideReadSetting();
                return true;
            }
            if (mSettingDialog.isShowing()) {
                mSettingDialog.hide();
                return true;
            }
            if (mLightSettingDialog.isShowing()) {
                mLightSettingDialog.hide();
                return true;
            }
            if (mPageModeDialog.isShowing()) {
                mPageModeDialog.hide();
                return true;
            }
            finishShowShelveDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishShowShelveDialog() {
        if (!AppUtils.isLogin()) {
            finish();
            return;
        }
        if (!isAddShelve && isFirst) {
            showAddShelveDialog();
            isFirst = false;
        }
        else {
            updatereadtime(mCount);
        }
    }

    private void updatereadtime(int count) {
        HttpClient.getInstance().get(AllApi.updatereadtime, AllApi.updatereadtime)
                .params("time", count)
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        mHander.removeCallbacks(mCounter);
                        finish();
                    }
                });
    }

    private void setShelveBtn() {
        shelveicon = isAddShelve ? R.mipmap.icon_exist_shelve : R.mipmap.icon_add_sj;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.read, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_shelve).setIcon(shelveicon);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_shelve) {
            if (!isAddShelve) {
                addshelve(false);
            } else {
                removeshelve();
            }
        } else if (id == R.id.action_read_book2) {
            // 分享对话框
            if (chapterBean != null) {
//                new ShareDialog.Builder(this)
//                        // 分享标题
//                        .setShareTitle(chapterBean.getSharetitle())
//                        // 分享描述
//                        .setShareDescription(chapterBean.getSharedesc())
//                        // 分享缩略图
//                        .setShareLogo(chapterBean.getCoverpic())
//                        // 分享链接
//                        .setShareUrl(chapterBean.getShare_link())
//                        .setListener(new UmengShare.OnShareListener() {
//
//                            @Override
//                            public void onSucceed(Platform platform) {
////                            toast("分享成功");
//                            }
//
//                            @Override
//                            public void onError(Platform platform, Throwable t) {
////                            toast("分享出错");
//                            }
//
//                            @Override
//                            public void onCancel(Platform platform) {
////                            toast("分享取消");
//                            }
//                        })
//                        .show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    AddShelveDialog dialog;

    private void showAddShelveDialog() {
        //获取实体类
        dialog = AddShelveDialog.getMyDialog(ReadActivity.this);
        //可以设置很大不同的动画
        //inviteCodeDialog.getWindow().setWindowAnimations(R.style.Dialog_Anim_Style);
        //inviteCodeDialog.getWindow().setWindowAnimations(R.style.Dialog_Anim_Style2);
        //回调实现点击
        dialog.setDialogCallBack(new AddShelveDialog.DialogCallBack() {
            @Override
            public void onActionClick(String code) {
                addshelve(true);
            }

            @Override
            public void onCancelClick() {
                dialog.dismiss();
                updatereadtime(mCount);
            }
        });
        dialog.show();
    }

    private void addshelve(boolean isfinish) {
        MMKV mmkv = MMKV.defaultMMKV();
        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        if (userBean.getBindStatus().equals("0")) {
            //MyLoginActivity.start(getApplicationContext());
            ThirdLoginActivity.start(getApplicationContext(), ThirdLoginActivity.EnterIndex.OTHER);
            return;
        }
        HttpClient.getInstance().get(AllApi.addshelve, AllApi.addshelve)
                .params("anime_id", bookList.getAnid())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        ToastUtils.show(msg);
                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                        isAddShelve = true;
                        bookList.setIs_shelve(Constants.SHELVE_EXIST);
                        setShelveBtn();
                        if (isfinish) {
                            updatereadtime(mCount);
                        }
                    }
                });
    }

    private void removeshelve() {
        MMKV mmkv = MMKV.defaultMMKV();
        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        if (userBean.getBindStatus().equals("0")) {
            ThirdLoginActivity.start(getApplicationContext(), ThirdLoginActivity.EnterIndex.OTHER);
            return;
        }

//        if (!AppUtils.isLogin()) {
//            MyLoginActivity.start(getApplicationContext());
//            return;
//        }
        HttpClient.getInstance().get(AllApi.removeshelve, AllApi.removeshelve)
                .params("id", bookList.getAnid())
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        ToastUtils.show(msg);
                        isAddShelve = false;
                        bookList.setIs_shelve(Constants.SHELVE_NO_EXIST);
                        setShelveBtn();
                    }
                });
    }

    public static boolean openBook(final BookList bookList, Activity context) {
        if (bookList == null) {
            throw new NullPointerException("BookList can not be null");
        }

        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_BOOK, bookList);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        context.startActivity(intent);
        return true;
    }

//    public BookPageWidget getPageWidget() {
//        return bookpage;
//    }

    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    //显示书本进度
    public void showProgress(float progress) {
        if (rl_progress.getVisibility() != View.VISIBLE) {
            rl_progress.setVisibility(View.VISIBLE);
        }
        setProgress(progress);
    }

    //隐藏书本进度
    public void hideProgress() {
        rl_progress.setVisibility(View.GONE);
    }

    public void initDayOrNight() {
        mDayOrNight = config.getDayOrNight();
        if (mDayOrNight) {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        } else {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        }
        setDayOrNight(mDayOrNight);
    }

    //改变显示模式
    public void changeDayOrNight() {
        mDayOrNight = config.getDayOrNight();
        if (mDayOrNight) {
            mDayOrNight = false;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));
        } else {
            mDayOrNight = true;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));
        }
        config.setDayOrNight(mDayOrNight);
        pageFactory.setDayOrNight(mDayOrNight);
        setDayOrNight(mDayOrNight);
    }

    //设置日间或者夜间模式
    @SuppressLint("ResourceAsColor")
    public void setDayOrNight(Boolean isNgiht) {
        if (isNgiht) {
            setBgColor(BOOK_BG_NIGHT);
        } else {
            setBgColor(config.getBookBgType());
        }
        book_name.setTextColor(config.getDayOrNight() ? getResources().getColor(R.color.read_font_night) : getResources().getColor(R.color.read_font_default));
        author_name.setTextColor(config.getDayOrNight() ? getResources().getColor(R.color.read_font_night) : getResources().getColor(R.color.board_bg));
        zhang.setTextColor(config.getDayOrNight() ? getResources().getColor(R.color.read_font_night) : getResources().getColor(R.color.board_bg));
        sort.setTextColor(config.getDayOrNight() ? getResources().getColor(R.color.read_font_night) : getResources().getColor(R.color.board_bg));
        book_name.setTextColor(config.getDayOrNight() ? getResources().getColor(R.color.read_font_night) : getResources().getColor(R.color.read_font_night));
        line.setBackground(config.getDayOrNight() ? getResources().getDrawable(R.color.read_font_default) : getResources().getDrawable(R.color.line));
        if (mDirectoryAdapter != null)
            mDirectoryAdapter.notifyDataSetChanged();
    }

    //设置页面的背景
    public void setBgColor(int type) {
        int color = 0;
        int fontcolor = 0;
        int progressbtn = 0;
        boolean night = false;
        switch (type) {
            case BOOK_BG_NIGHT:
                color = config.isHuyan() ? R.color.day_night_huyan : R.color.day_night;
                fontcolor = R.color.read_font_default;
//                fontcolor = R.color.read_font_night;
                progressbtn = R.drawable.seek_button_heiye;
                night = true;
                break;
            case Config.BOOK_BG_DEFAULT:
                color = config.isHuyan() ? R.color.read_bg_default_huyan : R.color.read_bg_default;
                fontcolor = R.color.read_font_default;
                progressbtn = R.drawable.seek_button;
                night = false;
                break;
            case Config.BOOK_BG_1:
                color = config.isHuyan() ? R.color.read_bg_1_huyan : R.color.read_bg_1;
                fontcolor = R.color.read_font_default;
                progressbtn = R.drawable.seek_button;
                night = false;
                break;
            case Config.BOOK_BG_2:
                color = config.isHuyan() ? R.color.read_bg_2_huyan : R.color.read_bg_2;
                fontcolor = R.color.read_font_default;
                progressbtn = R.drawable.seek_button;
                night = false;
                break;
            case Config.BOOK_BG_3:
                color = config.isHuyan() ? R.color.read_bg_3_huyan : R.color.read_bg_3;
                fontcolor = R.color.read_font_default;
                progressbtn = R.drawable.seek_button;
                night = false;
                break;
            case Config.BOOK_BG_4:
                color = config.isHuyan() ? R.color.read_bg_4_huyan : R.color.read_bg_4;
                fontcolor = R.color.read_font_default;
                progressbtn = R.drawable.seek_button;
                night = false;
                break;
        }
        bookpop_bottom.setBackground(getResources().getDrawable(color));
        rl_progress.setBackground(getResources().getDrawable(color));
        toolbar.setBackground(getResources().getDrawable(color));
        directory_layout.setBackground(getResources().getDrawable(color));

        tv_directory.setTextColor(getResources().getColor(fontcolor));
        tv_dayornight.setTextColor(getResources().getColor(fontcolor));
        tv_pagemode.setTextColor(getResources().getColor(fontcolor));
        tv_setting.setTextColor(getResources().getColor(fontcolor));
        tv_pre.setTextColor(getResources().getColor(fontcolor));
        tv_next.setTextColor(getResources().getColor(fontcolor));
        sb_progress.setThumb(getResources().getDrawable(progressbtn));
        setAndroidNativeLightStatusBar(night, color);
    }

    @SuppressLint("ResourceAsColor")
    private void setAndroidNativeLightStatusBar(boolean night, int bgcolor) {
        ImmersionBar.with(this)
//                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(!night)
                .statusBarColor(bgcolor)
                // 解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardEnable(true).init();
    }

    private void setProgress(float progress) {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(progress * 100.0);//format 返回的是字符串
        tv_progress.setText(bookList.getCharset() + "\n\n" + p + "%");
        tv_progress.setTextColor(getResources().getColor(config.getDayOrNight() ? R.color.read_font_night : R.color.read_font_default));
        tv_progress.setGravity(Gravity.CENTER);
    }

    public void setSeekBarProgress(float progress) {
        sb_progress.setProgress((int) (progress * 10000));
    }

    private void showReadSetting() {
        isShow = true;
        rl_progress.setVisibility(View.GONE);

        showSystemUI();
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_enter);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
        rl_bottom.startAnimation(topAnim);
        appbar.startAnimation(topAnim);
        rl_bottom.setVisibility(View.VISIBLE);
        appbar.setVisibility(View.VISIBLE);

        setDayOrNight(config.getDayOrNight());
    }

    private void hideReadSetting() {
        isShow = false;
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_exit);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit);
        if (rl_bottom.getVisibility() == View.VISIBLE) {
            rl_bottom.startAnimation(topAnim);
        }
        if (appbar.getVisibility() == View.VISIBLE) {
            appbar.startAnimation(topAnim);
        }
        if (rl_read_bottom.getVisibility() == View.VISIBLE) {
            rl_read_bottom.startAnimation(topAnim);
        }

        rl_bottom.setVisibility(View.GONE);
        rl_read_bottom.setVisibility(View.GONE);
        appbar.setVisibility(View.GONE);
        hideSystemUI();
    }

    @OnClick({R.id.tv_progress, R.id.rl_progress, R.id.tv_pre, R.id.sb_progress, R.id.tv_next, R.id.tv_directory, R.id.tv_dayornight, R.id.tv_pagemode, R.id.tv_setting, R.id.bookpop_bottom, R.id.rl_bottom})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_return:
//                finish();
//                break;
//            case R.id.ll_top:
//                break;
            case R.id.tv_progress:
                break;
            case R.id.rl_progress:
                break;
            case R.id.tv_pre:
                pageFactory.preChapter(true);
                break;
            case R.id.sb_progress:
                break;
            case R.id.tv_next:
                pageFactory.nextChapter();
                break;
            case R.id.tv_directory:
                drawerLayout.openDrawer(Gravity.LEFT);
//                Intent intent = new Intent(ReadActivity.this, MarkActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_dayornight:
                changeDayOrNight();
                break;
            case R.id.tv_pagemode:
                hideReadSetting();
                mLightSettingDialog.show();
                mLightSettingDialog.refresh();
                break;
            case R.id.tv_setting:
                hideReadSetting();
                mSettingDialog.show();
                mSettingDialog.refresh();
                break;
        }
    }
}
