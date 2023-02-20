package cc.ixcc.novelthree.jsReader.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.MemoryFile;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.msc.util.FileUtil;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.CommentBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.jsReader.Dialog.VipPageDialog;
import cc.ixcc.novelthree.jsReader.adapter.ListenCategoryAdapter;
import cc.ixcc.novelthree.jsReader.model.bean.BookChapterBean;
import cc.ixcc.novelthree.jsReader.model.bean.BookRecordBean;
import cc.ixcc.novelthree.jsReader.model.bean.CollBookBean;
import cc.ixcc.novelthree.jsReader.model.local.BookRepository;
import cc.ixcc.novelthree.jsReader.model.local.ReadSettingManager;
import cc.ixcc.novelthree.jsReader.page.TxtChapter;
import cc.ixcc.novelthree.jsReader.utils.SystemBarUtils;
import cc.ixcc.novelthree.jsReader.utils.ToastUtils;
import cc.ixcc.novelthree.treader.db.BookList;
import cc.ixcc.novelthree.treader.util.AppUtils;
import cc.ixcc.novelthree.ui.activity.my.BookPlListActivity;
import cc.ixcc.novelthree.ui.dialog.TipDialog;
import cc.ixcc.novelthree.utils.SpUtil;
import cc.ixcc.novelthree.utils.StringUtil;
import com.tencent.mmkv.MMKV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.COMMENT_RECOMMEND;
import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

public class ListenBookActivity extends MyActivity implements View.OnClickListener {
    private static final String TAG = "ListenBookActivity";
    // 当前页面的状态
    public static final int STATUS_LOADING = 1;         // 正在加载
    public static final int STATUS_FINISH = 2;          // 加载完成
    public static final int STATUS_ERROR = 3;           // 加载错误 (一般是网络加载情况)
    public static final int STATUS_EMPTY = 4;           // 空数据
    public static final int STATUS_PARING = 5;          // 正在解析 (装载本地数据)
    public static final int STATUS_PARSE_ERROR = 6;     // 本地文件解析错误(暂未被使用)
    public static final int STATUS_CATEGORY_EMPTY = 7;  // 获取到的目录为空
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan"; //xiaoyan
    //云端发音人名称列表(中文)
    private String[] mCloudVoicersEntries;
    //云端发音人名称列表(英文简称)
    private String[] mCloudVoicersValue;
    //音频流名称列表
    private String[] mCloudVoicersName;
    //定时列表
    private String[] mTimerName;
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private MemoryFile memFile;
    public volatile long mTotalSize = 0;
    private Vector<byte[]> container = new Vector<>();
    private TextView voice_value1, voice_value2, voice_value3, progress, totalprogress, bookTitle, tv_chapterName, tv_author, tv_add_shelf, tv_cur_progress, tv_review_number;
    private SeekBar seekBar1, seekBar2, seekBar3, totalSeekBar;
    private Button ttsBtnPersonType, ttsBtnPersonSelect;
    private ImageView coverImage;
    private ImageView mPlayImageView, iv_review;
    //private NumImageView mReviewImage;
    private ImageView mReviewImage;
    //起始值50
    private int voiceNumber1 = 50, voiceNumber2 = 50, voiceNumber3 = 50, totalNumber;

    //音频流类型
    private String voiceName;
    private SpannableStringBuilder builder;

    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String START_CHAPTER = "start_chapter";
    public static final String START_CHAPTER_LINK = "start_chapter_link";
    public static final String START_CHAPTER_NAME = "start_chapter_name";
    public static final String SHELVE = "is_shelve";
    private CollBookBean mCollBook;
    private int mStartChapter;

    // 当前章节列表
    protected List<TxtChapter> mChapterList;
    // 存储阅读记录类
    private BookRecordBean mBookRecord;
    // 当前章
    protected int mCurChapterPos = 0;
    //上一章的记录
    private int mLastChapterPos = 0;
    private VipPageDialog vipPageDialog;

    // 当前的状态
    protected int mStatus = STATUS_LOADING;
    // 判断章节列表是否加载完成
    protected boolean isChapterListPrepare;
    private String content;
    private boolean isPlaying = false;
    private boolean isShelve;

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
    @BindView(R.id.listen_dl_slide)
    DrawerLayout mDlSlide;
    private boolean order = true;
    private ListenCategoryAdapter mCategoryAdapter;
    // 当前章节列表
    protected List<BookChapterBean> mBookChapterList;
    private MMKV mmkv = MMKV.defaultMMKV();
    private UserBean mInfoBean;
    private boolean isFullScreen = false;
    private long curChapStartIndex = 0;
    private String savePath;
    private MediaPlayer mediaPlayer;
    private boolean iffirst = false;
    private Timer mPlayTimer;
    private TimerTask mPlayTimerTask;
    private Timer mListenTimer;
    private TimerTask mListenTimerTask;
    private boolean ifplay = false;
    private boolean isChanging = false;//互斥变量，防止定时器与SeekBar拖动时进度冲突
    private long playTime = 0;
    private long totalPlaySecond = 0;
    private String type = COMMENT_RECOMMEND;
    private CommentBean mCommentBean;
    private boolean isNextContinue = true;
    private boolean isFirstOpen = true;//是否第一次打开
    private long lastReadIndex = 0;
    private List<String> WaitingPlayQueue;

    public static void start(Context context,CollBookBean mCollBook,boolean isShelve,int start_chap){
        Intent intent = new Intent(context, ListenBookActivity.class);
        intent.putExtra(EXTRA_COLL_BOOK, mCollBook);
        intent.putExtra(SHELVE, isShelve);
        intent.putExtra(START_CHAPTER, start_chap);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_aloud_read;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.image_last).setOnClickListener(this);
        findViewById(R.id.image_next).setOnClickListener(this);
        findViewById(R.id.ll_shelf).setOnClickListener(this);
        findViewById(R.id.ll_time).setOnClickListener(this);
        findViewById(R.id.ll_review).setOnClickListener(this);
        findViewById(R.id.ll_category).setOnClickListener(this);
        iv_review = this.findViewById(R.id.iv_review);
        iv_review.setOnClickListener(this);
        mPlayImageView = this.findViewById(R.id.image_play);
        mPlayImageView.setOnClickListener(this);
        mReviewImage = this.findViewById(R.id.iv_review);
        bookTitle = this.findViewById(R.id.title_name);
        totalprogress = this.findViewById(R.id.progress_total);
        tv_chapterName = this.findViewById(R.id.tv_cur_chapter);
        tv_author = this.findViewById(R.id.tv_cur_author);
        tv_add_shelf = this.findViewById(R.id.tv_add_shelf);
        tv_review_number = this.findViewById(R.id.tv_review_number);

        totalSeekBar = this.findViewById(R.id.seek_total);
        coverImage = this.findViewById(R.id.listen_cover);

        //seekbar滑动事件
        initSeekBarListener();

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mCloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        mCloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);
        mCloudVoicersName = getResources().getStringArray(R.array.stream_entries);
        mTimerName = getResources().getStringArray(R.array.timer_entries);

        mLvCategory = this.findViewById(R.id.read_iv_category);
        ivCover = this.findViewById(R.id.iv_cover);
        tvBookName = this.findViewById(R.id.tvBookName);
        tvAuth = this.findViewById(R.id.tvAuth);
        tvTotalPage = this.findViewById(R.id.tvTotalPage);
        tvOrder = this.findViewById(R.id.tvOrder);
        tvTitleName = this.findViewById(R.id.title_name);
        tv_cur_progress = this.findViewById(R.id.tv_cur_progress);

        mDlSlide = this.findViewById(R.id.listen_dl_slide);

        //禁止滑动展示DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //侧边打开后，返回键能够起作用
        mDlSlide.setFocusableInTouchMode(false);
        setParam();
    }

    @Override
    protected void initData() {
        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        mCollBook = getIntent().getParcelableExtra(EXTRA_COLL_BOOK);
        mStartChapter = getIntent().getIntExtra(START_CHAPTER, 0);

        isShelve = getIntent().getBooleanExtra(SHELVE, false);
        if (isShelve) {
            tv_add_shelf.setText(getString(R.string.lis_menu_added_shelf));
            findViewById(R.id.iv_add_shelf).setEnabled(false);
        } else {
            tv_add_shelf.setText(getString(R.string.lis_menu_add_shelf));
        }
        isFullScreen = ReadSettingManager.getInstance().isFullScreen();
        savePath = Environment.getExternalStorageDirectory() + "/msc/listenbooktts.wav";

        prepareBook();
        setUpCategory();
        setUpAdapter();
        LoadChapters();
        openChapter();
        getReviewCount();
        InitClick();
    }

    //初始化目录
    private void setUpCategory() {
        Glide.with(this).load(mCollBook.getCoverPic()).into(ivCover);
        tvBookName.setText(mCollBook.getTitle());
        tvTitleName.setText(mCollBook.getTitle());
        tvAuth.setText(mCollBook.getAuthor());
        tvTotalPage.setText((mCollBook.getIswz() == Constants.lianzai ? "serialize" : "over") + " " + " " + mCollBook.getAllchapter() + "chapter");
        tvOrder.setText(order ? "Reverse order" : "Positive order");
    }

    private void setUpAdapter() {
        UserBean bean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        boolean b = false;
        if (bean != null) {
            b = TextUtils.equals(bean.getIs_vip(), "1");
        }

        mCategoryAdapter = new ListenCategoryAdapter(b);

        mLvCategory.setAdapter(mCategoryAdapter);
        mLvCategory.setFastScrollEnabled(true);
    }

    private void LoadChapters() {
        mBookChapterList = mCollBook.getBookChapters();
        mCategoryAdapter.refreshItems(mBookChapterList);
        mCategoryAdapter.notifyDataSetChanged();
    }

    private void LoadNextChapter() {
        if (mStartChapter == mBookChapterList.size() - 1) {
            ToastUtils.show("It's the last chapter");
        } else {
            ++mStartChapter;
            openChapter();
        }
    }

    private void LoadLastChapter() {
        if (mStartChapter == 0) {
            ToastUtils.show("It's already the first chapter");
        } else {
            --mStartChapter;
            openChapter();
        }
    }

    private void ShowCategory() {
        //移动到指定位置
        mLvCategory.setSelection(mStartChapter);
        mCategoryAdapter.setChapter(mStartChapter);
        //打开侧滑动栏
        mDlSlide.openDrawer(GravityCompat.START);
    }

    private void InitClick() {
        //目录点击
        mLvCategory.setOnItemClickListener(
            (parent, view, position, id) -> {
                if (StringUtil.isVip(mInfoBean)) {
                    //是会员
                    mDlSlide.closeDrawer(GravityCompat.START);
                    mStartChapter = position;
                    openChapter();
                }
                else {
                    //非会员
                    BookChapterBean bookChapterBean = mBookChapterList.get(position);
                    if (bookChapterBean.getCoin() == 0) {
                        mDlSlide.closeDrawer(GravityCompat.START);
                        mStartChapter = position;
                        openChapter();
                    }
                    else if (bookChapterBean.getIs_pay()) {
                        mDlSlide.closeDrawer(GravityCompat.START);
                        mStartChapter = position;
                        openChapter();
                    }
                    else {
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
                                                bookChapterBean.setIs_pay(true);
                                                if (vipPageDialog != null && vipPageDialog.isShowing()) {
                                                    vipPageDialog.dismiss();
                                                }
                                                if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
                                                    mDlSlide.closeDrawer(GravityCompat.START);
                                                }
                                                mStartChapter = position;
                                                openChapter();
                                                mCategoryAdapter.refreshItems(mBookChapterList);
                                            } else {
                                                showPageDialog(mCollBook, position);
                                                if (!vipPageDialog.isShowing()) {
                                                    vipPageDialog.show();
                                                    hideSystemBar();
                                                }
                                            }
                                        }
                                    });
                        }
                        else {
                            showPageDialog(mCollBook, position);
                            if (!vipPageDialog.isShowing()) {
                                vipPageDialog.show();
                                hideSystemBar();
                            }
                        }
                    }
                }
            }
        );
    }

    private void showPageDialog(CollBookBean mCollBook, int mCurChapterPos) {
        if (mCurChapterPos < 0) {
            return;
        }
        if (vipPageDialog == null) {
            vipPageDialog = new VipPageDialog(ListenBookActivity.this, mCollBook, mCurChapterPos);
            vipPageDialog.setCancelable(false);
            vipPageDialog.setPayClickable(true);
            vipPageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideSystemBar();
                }
            });
//            vipPageDialog.payListener = new VipPageDialog.PayDialogListener() {
//                @Override
//                public void dialogClose(boolean auto) {
//                    Log.e(TAG, "auto: " + auto);
//                }
//
//                @Override
//                public void byCurrent(int current) {
//                    if (vipPageDialog.getPayClickable()) {
//                        byPage(vipPageDialog, current);
//                    }
//                    vipPageDialog.setPayClickable(false);
//                }
//
//                @Override
//                public void goOpen() {
//                    OpenVipActivity.start(ListenBookActivity.this);
//                }
//            };
        }
        else {
            vipPageDialog.setBook(mCollBook);
            vipPageDialog.setCurrent(mCurChapterPos);
            vipPageDialog.setPayClickable(true);
            vipPageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideSystemBar();
                }
            });
//            vipPageDialog.payListener = new VipPageDialog.PayDialogListener() {
//                @Override
//                public void dialogClose(boolean auto) {
////                    Log.e(TAG, "auto: " + auto);
//                }
//
//                @Override
//                public void byCurrent(int current) {
//                    if (vipPageDialog.getPayClickable()) {
//                        byPage(vipPageDialog, current);
//                    }
//                    vipPageDialog.setPayClickable(false);
//                }
//
//                @Override
//                public void goOpen() {
//                    OpenVipActivity.start(ListenBookActivity.this);
//                }
//            };
        }
    }

    //购买章节
    private void byPage(VipPageDialog vipPageDialog, int current) {
        HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                .params("anime_id", mCollBook.get_id())
                .params("chaps", current + 1)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        Log.e(TAG, "msg: " + msg + " ,chap: " + (current + 1) + " line895");

                        //TODO 做书豆不足的处理
                        if (code == 200) {
                            ToastUtils.show("Purchase successful");
                            mCollBook.getBookChapters().get(current).setIs_pay(true);
                            mBookChapterList.get(current).setIs_pay(true);
                            if (ListenBookActivity.this.vipPageDialog != null && ListenBookActivity.this.vipPageDialog.isShowing()) {
                                ListenBookActivity.this.vipPageDialog.dismiss();
                            }
                            if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
                                mDlSlide.closeDrawer(GravityCompat.START);
                            }
                            mmkv.encode(mCollBook.get_id(), vipPageDialog.isAuto());
                            mStartChapter = current;
                            openChapter();
                        }
                        else {
                            if (ListenBookActivity.this.vipPageDialog != null) {
                                ListenBookActivity.this.vipPageDialog.setPayClickable(true);
                            }
                            ToastUtils.show("Purchase failed " + Constants.getInstance().getConfig().getCoin_name() + " not enough");
                            //ChargeActivity.start(ListenBookActivity.this);
                        }
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        if (ListenBookActivity.this.vipPageDialog != null) {
                            ListenBookActivity.this.vipPageDialog.setPayClickable(true);
                        }
                    }
                });
    }

    private void hideSystemBar() {
        //隐藏
        SystemBarUtils.hideStableStatusBar(this);
        if (isFullScreen) {
            SystemBarUtils.hideStableNavBar(this);
        }
    }

    /**
     * 初始化书籍
     */
    private void prepareBook() {
        mBookRecord = BookRepository.getInstance().getBookRecord(mCollBook.get_id());

        if (mBookRecord == null) {
            mBookRecord = new BookRecordBean();
            mBookRecord.setBookId(mCollBook.get_id());
            mBookRecord.setChapter(mStartChapter);
            mBookRecord.setPagePos(0);
            mBookRecord.setPlayTime(0);
        }
        else {
            mLastChapterPos = mBookRecord.getChapter();
            lastReadIndex = mBookRecord.getPlayTime();
            mStartChapter = mBookRecord.getChapter();
        }
    }

    private void ResetPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.seekTo(0);
        }
        if (mPlayTimer != null) {
            mPlayTimer.cancel();
        }
        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
        }
        if (mListenTimer != null) {
            mListenTimer.cancel();
        }
        if (mListenTimerTask != null) {
            mListenTimerTask.cancel();
        }
        iffirst = false;
        ifplay = false;
        isChanging = false;
        playTime = 0;
        totalPlaySecond = 0;
        totalSeekBar.setProgress(0);

        tv_cur_progress.setText("0%");
        totalprogress.setText("100%");
    }

    /**
     * 打开指定章节
     * 立即播放
     */
    public void openChapter() {
        ResetPlay();
        Glide.with(this).load(mCollBook.getCoverPic()).circleCrop().into(coverImage);
        bookTitle.setText(mCollBook.getShareTitle());
        tv_chapterName.setText(mBookChapterList.get(mStartChapter).getTitle());
        tv_author.setText("Author：" + mCollBook.getAuthor());
        if (mLastChapterPos != mStartChapter) {
            lastReadIndex = 0;
        }
        BookChapterBean bookChapterBean = mBookChapterList.get(mStartChapter);
        if (checkVip()||bookChapterBean.getCoin()<=0||bookChapterBean.getIs_pay()){
            loadPage();
        }
        else {
            boolean auto = mmkv.decodeBool(mCollBook.get_id(), false);
            if (auto) {
                HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                    .params("anime_id", mCollBook.get_id())
                    .params("chaps", mStartChapter)
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 200) {
                                mCollBook.getBookChapters().get(mStartChapter).setIs_pay(true);
                                bookChapterBean.setIs_pay(true);
                                if (vipPageDialog != null && vipPageDialog.isShowing()) {
                                    vipPageDialog.dismiss();
                                }
                                if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
                                    mDlSlide.closeDrawer(GravityCompat.START);
                                }
                                openChapter();
                                mCategoryAdapter.refreshItems(mBookChapterList);
                            } else {
                                showPageDialog(mCollBook, mStartChapter);
                                if (!vipPageDialog.isShowing()) {
                                    vipPageDialog.show();
                                    hideSystemBar();
                                }
                            }
                        }
                    });
            }
            else {
                showPageDialog(mCollBook, mStartChapter);
                if (!vipPageDialog.isShowing()) {
                    vipPageDialog.show();
                    hideSystemBar();
                }
            }
        }
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString
     *            原始字符串
     * @param length
     *            指定长度
     * @return
     */
    public static ArrayList<String> getStrList(String inputString, int length) {
        ArrayList<String> resList = new ArrayList<String>();
        /**
         * 几个字一组 变量控制 大于零有意义
         */
        int num = length;

        /**
         * 可以分为几行
         */
        int lines = (inputString.length() + (num - 1)) / num;
        /**
         * 中间行
         */
        int center = ((lines + 1) / 2);
        System.out.println("长度:" + inputString.length());
        System.out.println("可以分为:" + lines + "行");
        /**
         * 转成数组
         */
        char[] chars = inputString.toCharArray();
        /**
         * 计数
         */
        int j = 0;
        /**
         * 开始加工
         */
        for (int i = 0; i < chars.length; i += num) {
            j++;
            System.out.print("中间行=" + center + "当前行:" + j);
            String s1 = String.valueOf(chars[i]);
            for (int k = 1; k <= num - 1; k++) {
                if (i + k < chars.length)
                    s1 = s1.concat(String.valueOf(chars[i + k]));
            }
            //标示中间行
            if (j == center) {
                System.out.print("--->");
            }
            //打印结果 或者用String[] 来接 在循环外返回一个数组出去
            System.out.println(s1);
            resList.add(s1);
        }
        return resList;
    }

    public void loadPage(){
        HttpClient.getInstance().post(AllApi.chapterinfo, AllApi.chapterinfo)
            .isMultipart(true)
            .params("anime_id", mCollBook.get_id())
            .params("chaps", mBookChapterList.get(mStartChapter).getLink())
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    BookList resultBean = new Gson().fromJson(info[0], BookList.class);
                    content = resultBean.getInfo().replace("\\&quot;", "");
                    curChapStartIndex = mBookChapterList.get(mStartChapter).getStart();
                    // 分隔字符串，太长会导致讯飞报错 10117缓冲区不足
                    WaitingPlayQueue = getStrList(content, 3000);
                    if (isFirstOpen && mBookRecord.getPlayTime() > 50) {
                        isFirstOpen = false;
                        String s = WaitingPlayQueue.remove(0);
                        mTts.startSpeaking(s.substring( (int) mBookRecord.getPlayTime() - 50 ), mTtsListener);
                    } else {
                        String s = WaitingPlayQueue.remove(0);
                        mTts.startSpeaking(s, mTtsListener);
                    }
                }
            });
    }

    private void StartMediaPlay() {
        if (!ifplay) {
            mPlayImageView.setImageResource(R.drawable.listen_pause_img);
            if (!iffirst) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(savePath);
                    mediaPlayer.prepare();// 准备
                    long duration = mediaPlayer.getDuration();
                    if (duration != 0) {
                        totalPlaySecond = duration / 1000;
                        totalSeekBar.setMax((int) totalPlaySecond);//设置进度条
                        totalSeekBar.setProgress(0);
                        mediaPlayer.seekTo(0);
                        tv_cur_progress.setText(formatSeconds(0));
                        totalprogress.setText(formatSeconds(totalPlaySecond));
                    }
                    iffirst = true;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            StartPlayTimer();
            if (isFirstOpen) {
                playTime = mBookRecord.getPlayTime();
                mediaPlayer.seekTo((int) mBookRecord.getPlayTime() * 1000);
                isFirstOpen = false;
            }
            mediaPlayer.start();// 开始
            ifplay = true;
        } else if (ifplay) {
            //继续
            PauseMediaPlay();
        }
    }

    private void StartPlayTimer() {
        //----------定时器记录播放进度---------//
        mPlayTimer = new Timer();
        mPlayTimerTask = new TimerTask() {
            @Override
            public void run() {
            if (isChanging == true) {
                return;
            }
            mHandler.sendEmptyMessage(0);
            }
        };
        mPlayTimer.schedule(mPlayTimerTask, 1000, 1000);
    }

    private void UpPlayTime() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(savePath);
            mediaPlayer.prepare();// 准备
            long duration = mediaPlayer.getDuration();
            if (duration != 0) {
                totalPlaySecond = duration / 1000;
                totalSeekBar.setMax((int) totalPlaySecond);//设置进度条
                totalSeekBar.setProgress(0);
                mediaPlayer.seekTo(0);
                tv_cur_progress.setText(formatSeconds(0));
                totalprogress.setText(formatSeconds(totalPlaySecond));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String formatSeconds(long seconds) {
        String standardTime;
        if (seconds <= 0) {
            standardTime = "00:00";
        } else if (seconds < 60) {
            standardTime = String.format(Locale.getDefault(), "00:%02d", seconds % 60);
        } else if (seconds < 3600) {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        } else {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60);
        }
        return standardTime;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:
                UpdatePlayShow();
                break;
            case 1:
                hideDialog();
                StartMediaPlay();
                break;
            case 2:
                UpPlayTime();
                break;
            case 3:
                UpdateReview();
                break;
            case 4:
                //PauseMediaPlay();//合成后播放
                PausePlay();//立即播放模式
                break;
            case 5:
                tv_add_shelf.setText(getString(R.string.lis_menu_added_shelf));
                findViewById(R.id.iv_add_shelf).setEnabled(false);
                break;
        }
        }
    };

    private void UpdatePlayShow() {
        if (isPlaying) return;
        if (playTime < totalPlaySecond) {
            ++playTime;
            tv_cur_progress.setText(formatSeconds(playTime));
            totalSeekBar.setProgress((int) playTime);
            totalprogress.setText(formatSeconds((totalPlaySecond - playTime)));
        }
        else {
            if (isNextContinue) {
                LoadNextChapter();
            }
            else {
                StopMediaPlay();
            }
        }
    }

    private void PauseMediaPlay() {
        mPlayImageView.setImageResource(R.drawable.listen_play_img);
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        ifplay = false;
        if (mPlayTimer != null) {
            mPlayTimer.cancel();
        }
        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
        }
        if (mListenTimer != null) {
            mListenTimer.cancel();
        }
        if (mListenTimerTask != null) {
            mListenTimerTask.cancel();
        }
    }

    private void PausePlay() {
        mPlayImageView.setImageResource(R.drawable.listen_play_img);
        if (mTts != null) {
            mTts.pauseSpeaking();
        }
        if (mPlayTimer != null) {
            mPlayTimer.cancel();
        }
        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
        }
        if (mListenTimer != null) {
            mListenTimer.cancel();
        }
        if (mListenTimerTask != null) {
            mListenTimerTask.cancel();
        }
    }

    private void StopMediaPlay() {
        mPlayImageView.setImageResource(R.drawable.listen_play_img);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        ifplay = false;
        if (mPlayTimer != null) {
            mPlayTimer.cancel();
        }
        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
        }
        if (mListenTimer != null) {
            mListenTimer.cancel();
        }
        if (mListenTimerTask != null) {
            mListenTimerTask.cancel();
        }
    }

    private void StopPlay() {
        mPlayImageView.setImageResource(R.drawable.listen_play_img);
        if (mTts != null) {
            mTts.stopSpeaking();
        }
        if (mPlayTimer != null) {
            mPlayTimer.cancel();
        }
        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
        }
        if (mListenTimer != null) {
            mListenTimer.cancel();
        }
        if (mListenTimerTask != null) {
            mListenTimerTask.cancel();
        }
    }

    private void UpdateReview() {
        int reviewCount = mCommentBean.getTotal_score_person();
        if (mCommentBean != null) {
            int left = iv_review.getPaddingLeft();
            int top = iv_review.getPaddingTop();
            int bottom = iv_review.getPaddingBottom();
            if (reviewCount >= 0 && reviewCount < 10) {
                findViewById(R.id.iv_review).setPadding(left, top, 12, bottom);
            }
            else {
                findViewById(R.id.iv_review).setPadding(left, top, 24, bottom);
            }
            tv_review_number.setText(mCommentBean.getTotal_score_person() + "");
        }
    }

    private void initSeekBarListener() {
        //播放
        totalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (null == mTts) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            showToast("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        switch (view.getId()) {
            // 暂停播放/继续播放
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_share:
                SampleSHare();
                break;
            case R.id.image_last:
                LoadLastChapter();
                break;
            case R.id.image_next:
                LoadNextChapter();
                break;
            case R.id.image_play:
                if (isPlaying) {
                    mPlayImageView.setImageResource(R.drawable.listen_play_img);
                    mTts.pauseSpeaking();
                } else {
                    mPlayImageView.setImageResource(R.drawable.listen_pause_img);
                    mTts.resumeSpeaking();
                }
                isPlaying = !isPlaying;
                break;
            case R.id.ll_shelf:
                AddToShelf();
                break;
            case R.id.ll_time:
                showTimerSelectDialog();
                break;
            case R.id.ll_review:
                showReview();
                break;
            case R.id.ll_category:
                ShowCategory();
                break;
        }
    }

    //展示评论
    private void showReview() {
        try {
            String id = mCollBook.get_id();
            BookPlListActivity.start(this,Integer.parseInt(id));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void SampleSHare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mCollBook.getShareTitle());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void AddToShelf() {
        if (isShelve) return;
        TipDialog myDialog = TipDialog.getMyDialog(this);
        myDialog.setTitle("Add");
        myDialog.setSubmit("OK");
        myDialog.setCancel("Cancel");
        myDialog.setMessage("Add to Library if you like it");
        myDialog.setDialogCallBack(new TipDialog.DialogCallBack() {
            @Override
            public void onActionClick() {
                if (AppUtils.isLogin()) {
                    addshelve();
                    myDialog.dismiss();
                }
            }
        });
        myDialog.setCancelCallBack(new TipDialog.DialogCancelCallBack() {
            @Override
            public void onCancel() {
                myDialog.dismiss();
                //exit();
            }
        });
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideSystemBar();
            }
        });

        myDialog.show();
    }

    //加入书架
    private void addshelve() {
        HttpClient.getInstance().get(AllApi.addshelve, AllApi.addshelve)
            .params("anime_id", mCollBook.get_id())
            .execute(new HttpCallback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    ToastUtils.show("Added successfully");
                    mHandler.sendEmptyMessage(5);
                    //exit();
                }
            });
    }

    /**
     * 保存阅读记录
     */
    public void saveRecord() {
        if (!AppUtils.isLogin()) {
            return;
        }

        if (mBookChapterList.isEmpty()) {
            return;
        }

        mBookRecord.setBookId(mCollBook.get_id());
        mBookRecord.setChapter(mStartChapter);

        addHistory(mCollBook.get_id(), (mStartChapter + 1) + "");
        //存储到数据库
    }

    //添加阅读记录
    private void addHistory(String bookId, String link) {
        HttpClient.getInstance().post(AllApi.addreadhistory, AllApi.addreadhistory)
            .params("anime_id", bookId)
            .params("chaps", link)
            .execute(new HttpCallback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    //ToastUtils.show(msg);
                    BookRepository.getInstance().saveBookRecord(mBookRecord);
                }
            });
    }

    private void getReviewCount() {
        HttpClient.getInstance().get(AllApi.commentlist, AllApi.commentlist)
            .params("anime_id", mCollBook.get_id())
            .params("page", 0)
            .params("page_size", PAGE_SIZE)
            .params("type", type)
            .execute(new HttpCallback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    mCommentBean = new Gson().fromJson(info[0], CommentBean.class);
                    mHandler.sendEmptyMessage(3);
                }
            });
    }

    // 退出
    private void exit() {
        boolean login = AppUtils.isLogin();
        Log.e(TAG, "login: " + login);
        super.onBackPressed();
    }

    private int selectedNum = 0;
    private int selectedVoiceNum = 0;
    private int selectedTimerNum = 0;

    /**
     * 发音人选择。
     */
    private void showPresonSelectDialog() {
        new AlertDialog.Builder(this).setTitle("在线合成发音人选项")
            // 单选框有几项,各是什么名字
            .setSingleChoiceItems(mCloudVoicersEntries, selectedNum,
                new DialogInterface.OnClickListener() { // 点击单选框后的处理
                    public void onClick(DialogInterface dialog, int which) { // 点击了哪一项
                        voicer = mCloudVoicersValue[which];
                        //凯瑟琳、亨利（阿森纳）、玛丽切换文本至英文
                        ttsBtnPersonSelect.setText("发音人：" + mCloudVoicersEntries[which]);
                        selectedNum = which;
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 音频流类型选择
     */
    private void showVoiceSelectDialog() {
        new AlertDialog.Builder(this).setTitle("在线合成发音人选项")
            // 单选框有几项,各是什么名字
            .setSingleChoiceItems(mCloudVoicersName, selectedVoiceNum,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        voiceName = mCloudVoicersName[which];
                        ttsBtnPersonType.setText("音频流类型：" + voiceName);
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 定时类型选择
     */
    private void showTimerSelectDialog() {
        new AlertDialog.Builder(this).setTitle("定时方式选择")
            // 单选框有几项,各是什么名字
            .setSingleChoiceItems(mTimerName, selectedTimerNum,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedTimerNum = which;
                        isNextContinue = true;
                        switch (which) {
                            case 0:
                                if (mListenTimer != null) {
                                    mListenTimer.cancel();
                                    mListenTimer = null;
                                }
                                if (mListenTimerTask != null) {
                                    mListenTimerTask.cancel();
                                    mListenTimerTask = null;
                                }
                                break;
                            case 1:
                                mListenTimer = new Timer();
                                mListenTimerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.sendEmptyMessage(4);
                                    }
                                };
                                mListenTimer.schedule(mListenTimerTask, 1000 * 45 * 60, 1000 * 45 * 60);
                                break;
                            case 2:
                                mListenTimer = new Timer();
                                mListenTimerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.sendEmptyMessage(4);
                                    }
                                };
                                mListenTimer.schedule(mListenTimerTask, 1000 * 60 * 60, 1000 * 60 * 60);
                                break;
                            case 3:
                                mListenTimer = new Timer();
                                mListenTimerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.sendEmptyMessage(4);
                                    }
                                };
                                mListenTimer.schedule(mListenTimerTask, 1000 * 60 * 60 * 2, 1000 * 60 * 60 * 2);
                                break;
                            case 4:
                                isNextContinue = false;
                                break;
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.e(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showToast("初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
            else {
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            isPlaying = true;
            mPlayImageView.setImageResource(R.drawable.listen_pause_img);
        }

        @Override
        public void onSpeakPaused() {
            isPlaying = false;
            mPlayImageView.setImageResource(R.drawable.listen_play_img);
        }

        @Override
        public void onSpeakResumed() {
            isPlaying = true;
            mPlayImageView.setImageResource(R.drawable.listen_pause_img);
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            //记录暂停时的进度
            mPercentForBuffering = percent;
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            Log.e(TAG, "beginPos=" + beginPos + ",endPos=" + endPos);
            mPercentForPlaying = percent;
            totalSeekBar.setProgress(percent);
            tv_cur_progress.setText(percent + "%");
            totalprogress.setText((100 - percent) + "%");
            mBookRecord.setChapter(mStartChapter);
            mBookRecord.setPlayTime((lastReadIndex + beginPos));//立即播放
            if (percent == 100) {
                if (isNextContinue) {
                    LoadNextChapter();
                }
                else {
                    StopPlay();
                }
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                //showToast("播放完成");
                Log.e(TAG, "onCompleted: 播放完成" + container.size());
                try {
                    for (int i = 0; i < container.size(); i++) {
                        writeToFile(container.get(i));
                    }
                } catch (IOException e) {
                }
                //播放完成后保存至文件夹
                FileUtil.saveFile(memFile, mTotalSize, Environment.getExternalStorageDirectory() + "/listenbooktts1.pcm");

                // 继续播放
                if (!WaitingPlayQueue.isEmpty()) {
                    String s = WaitingPlayQueue.remove(0);
                    Log.e(TAG, "onCompleted-继续播放：" + s);
                    mTts.startSpeaking(s, mTtsListener);
                }
                else {
                    LoadNextChapter();
                }
            }
            else if (error != null) {
                showToast(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.e(TAG, "onEventid =" + sid);
            }
            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                Log.e(TAG, "onEvent bufis =" + buf.length);
                container.add(buf);
            }
        }
    };

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速(String 类型)
            mTts.setParameter(SpeechConstant.SPEED, voiceNumber1 + "");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, voiceNumber2 + "");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, voiceNumber3 + "");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, voiceName);
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, savePath);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
        if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
            mDlSlide.closeDrawer(GravityCompat.START);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mPlayTimer != null) {
            mPlayTimer.cancel();
            mPlayTimer = null;
        }
        if (mPlayTimerTask != null) {
            mPlayTimerTask.cancel();
            mPlayTimerTask = null;
        }
        if (mListenTimer != null) {
            mListenTimer.cancel();
            mListenTimer = null;
        }
        if (mListenTimerTask != null) {
            mListenTimerTask.cancel();
            mListenTimerTask = null;
        }
    }

    @Override
    protected void onResume() {
        //移动数据统计分析
        super.onResume();
    }

    @Override
    protected void onPause() {
        //移动数据统计分析
        super.onPause();
        saveRecord();
    }

    private void writeToFile(byte[] data) throws IOException {
        if (data == null || data.length == 0)
            return;
        try {
            if (memFile == null) {
                Log.e(TAG, "writeToFile: 写入文件夹");
                String mFilepath = Environment.getExternalStorageDirectory() + "/listenbooktts.pcm";
                memFile = new MemoryFile(mFilepath, 1920000);
                memFile.allowPurging(false);
            }
            memFile.writeBytes(data, 0, (int) mTotalSize, data.length);
            mTotalSize += data.length;
        } finally {
        }
    }

    /**
     * 展示吐司
     */
    private void showToast(final String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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
}