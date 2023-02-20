package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.Constants;

import com.tencent.mmkv.MMKV;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.bean.ActiviyNoticeInfo;
import cc.ixcc.novelthree.bean.BookDetailBean;
import cc.ixcc.novelthree.bean.ChapterBean;
import cc.ixcc.novelthree.bean.RewordBean;
import cc.ixcc.novelthree.bean.RewordRuleBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.custom.ShelveBannerLoader;
import cc.ixcc.novelthree.helper.BindEventBus;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.http.MainHttpUtil;
import cc.ixcc.novelthree.jsReader.Activity.JsReadActivity;
import cc.ixcc.novelthree.jsReader.model.bean.BookChapterBean;
import cc.ixcc.novelthree.jsReader.model.bean.CollBookBean;
import cc.ixcc.novelthree.statistics.AdjustUtil;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.treader.util.AppUtils;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.vip.OpenVipActivity;
import cc.ixcc.novelthree.ui.adapter.TagsAdapter;
import cc.ixcc.novelthree.ui.adapter.myAdapter.BookEvaluateAdapter;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomGuessAdapter;
import cc.ixcc.novelthree.ui.dialog.AddShelveDialog;
import cc.ixcc.novelthree.ui.dialog.RewordBottomDialog;
import cc.ixcc.novelthree.ui.dialog.TipDialog;
import cc.ixcc.novelthree.utils.GsonUtils;
import cc.ixcc.novelthree.utils.NumberUtils;
import cc.ixcc.novelthree.utils.SpUtil;
import cc.ixcc.novelthree.utils.StarBar;
import com.jiusen.base.BaseAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import cc.ixcc.novelthree.widget.PileAvertView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 书详情
 */
/**
 * 书详情
 */
@BindEventBus
public class BookDetailActivity extends MyActivity {
    @BindView(R.id.book_img)
    ImageView bookImg;
    @BindView(R.id.book_title)
    TextView bookTitle;
    @BindView(R.id.book_author)
    TextView bookAuthor;
    @BindView(R.id.book_status)
    TextView bookStatus;
    @BindView(R.id.book_num)
    TextView bookNum;
    @BindView(R.id.read_num)
    LinearLayout readNum;
    @BindView(R.id.book_people)
    LinearLayout bookPeople;
    @BindView(R.id.book_evaluate)
    TextView bookEvaluate;
    @BindView(R.id.starBar)
    StarBar starBar;
    @BindView(R.id.update_time)
    TextView updateTime;
    @BindView(R.id.book_pl)
    TextView bookPl;
    @BindView(R.id.book_rv)
    RecyclerView bookRv;
    @BindView(R.id.book_youlike)
    RecyclerView bookLikeRv;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.bottom)
    RelativeLayout bottom;
    @BindView(R.id.read)
    TextView read;
    @BindView(R.id.hots)
    TextView hots;
    @BindView(R.id.tags)
    FlexboxLayout tags;
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.book_ml)
    TextView book_ml;
    //    @BindView(R.id.comment_num)
//    TextView commentNum;
    @BindView(R.id.all_comments)
    TextView allComments;
    @BindView(R.id.exist_book)
    TextView existBook;
    @BindView(R.id.add_book)
    TextView addBook;
    @BindView(R.id.change)
    TextView change;
    @BindView(R.id.coments_no)
    TextView comentsNo;
    @BindView(R.id.content2)
    TextView content2;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.btn_read)
    TextView btnRead;
    @BindView(R.id.tvVip)
    TextView tvVip;
    @BindView(R.id.btn_reword)
    TextView btnReword;
    @BindView(R.id.expand)
    LinearLayout expand;
    @BindView(R.id.is_down)
    ImageView isdown;
    @BindView(R.id.is_up)
    ImageView isup;
    @BindView(R.id.pileavert_reword)
    PileAvertView pileavert_reword;
    @BindView(R.id.reword_users)
    TextView reword_users;

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_tag)
    RecyclerView rv_tag;
    @BindView(R.id.rl_recommend_title)
    RelativeLayout rlRecommendTitle;

    private List<TextView> txs;
    public static BookDetailActivity mBookDetailActivity = null;
    private BookEvaluateAdapter evaluateAdapter;
    private StackRoomGuessAdapter guessAdapter;
    private BookDetailBean bookDetailBean;
    int anime_id;
    List<BookDetailBean.GuessBean> guessList = new ArrayList<>();
    boolean isExpand = false;
    private static final int LINES = 5; //最多展示5行。
    private BookDetailBean detailBean;
    boolean isFirst = true;
    private List<RewordBean> rewordBeanList;
    private RewordBottomDialog rewordBottomDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @DebugLog
    public static void start(Context context, int anime_id) {
        context = AppContext.applicationContext;
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("anime_id", anime_id);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mBookDetailActivity = this;
        anime_id = getIntent().getIntExtra("anime_id", 0);
        initEvaluateRv();
        initLikeRv();

        initBanner();
        rlRecommendTitle.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
        bookLikeRv.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
    }

    public void ShowTags(BookDetailBean detailBean) {

        TagsAdapter tagsAdapter = new TagsAdapter(getContext());

        rv_tag.setLayoutManager(new FlexboxLayoutManager(getContext()));
        //mHistoryRv.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        tagsAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

            }
        });
        rv_tag.setAdapter(tagsAdapter);
        rv_tag.setNestedScrollingEnabled(false); //禁止滑动

        List<String> strs = new ArrayList<>();

        tagsAdapter.setData(strs);
    }

    //初始化banner运营位
    public void initBanner() {
        try {
            if (!HomeActivity.mHomeActivity.IsPostNotice) {
                HomeActivity.mHomeActivity.GetActiviyNoticeMsg(() -> {
                    ShowBanner(HomeActivity.mHomeActivity);
                });
            } else {
                ShowBanner(HomeActivity.mHomeActivity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowBanner(HomeActivity mHomeActivity) {
        //如果有运营位则显示 没有则隐藏
        if (mHomeActivity.adList_detail.size() > 0) {
            banner.setVisibility(View.VISIBLE);

            banner.setOutlineProvider(new ViewOutlineProvider() { //要在加载图片之前设置这个方法
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20); //设置圆角
                }
            });

            banner.setClipToOutline(true);
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new ShelveBannerLoader());
            //设置图片集合
            banner.setImages(mHomeActivity.adList_detail);
            //设置轮播时间
            banner.setDelayTime(5000);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Log.e("TAG", "banner_position:" + position);
                    if (mHomeActivity.adList_detail != null) {
                        if (position >= 0 && position < mHomeActivity.adList_detail.size()) {
                            ActiviyNoticeInfo bean = mHomeActivity.adList_detail.get(position);
                            if (bean != null) {
//                                AdjustUtil.GetInstance().SendBannerEvent(4);
                                UserBean tempBean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                                //13详情页支付活动 14详情页新书推荐 15详情页VIP活动 16详情页URL活动
                                switch (bean.getType()) {
                                    case 13:
                                        if (tempBean.getBindStatus().equals("0")) {
                                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                                        } else {
                                            startActivity(TopUpActivity.class);
                                        }
                                        break;
                                    case 14:
                                        BookDetailActivity.start(getContext(), bean.getAnid());
                                        break;
                                    case 15:
                                        if (tempBean.getBindStatus().equals("0")) {
                                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                                        } else {
                                            OpenVipActivity.start(getContext());
                                        }
                                        break;
                                    case 16:
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bean.gethttp_url())));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                }
            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

    @Override
    protected void initData() {

    }

    private boolean isLoaded = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (!AppUtils.isLogin() && isLoaded) {
            return;
        } else {
            getInfo(anime_id);
        }
        isLoaded = true;
    }

    public void getInfo(int anime_id) {
        HttpClient.getInstance().get(AllApi.bookinfo, AllApi.bookinfo)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        BookDetailBean bookDetailBean = new Gson().fromJson(info[0], BookDetailBean.class);
                        detailBean = bookDetailBean;
                        if (mBookDetailActivity != null) {
                            setView(bookDetailBean);
                        }
                    }
                });
    }

    private void setView(BookDetailBean detailBean) {
        //字数
        bookNum.setText(detailBean.getWordsString());
        bookTitle.getPaint().setFakeBoldText(true);
//        comentsNo.setVisibility(detailBean.getComments().size() == 0 ? View.VISIBLE : View.GONE);
        evaluateAdapter.setData(detailBean.getComments());
        guessList = detailBean.getGuess();
        setGuessView();
        Glide.with(AppContext.sInstance).load(detailBean.getCoverpic()).into(bookImg);
        bookAuthor.setText(detailBean.getAuthor());
        bookTitle.setText(detailBean.getTitle());
        read.setText(NumberUtils.amountConversion(detailBean.getRead()));
        hots.setText(NumberUtils.amountConversion(detailBean.getHots()));

        //设置星星为整数评分
        starBar.setIntegerMark(true);
        tvVip.setVisibility(TextUtils.equals(detailBean.getIsvip(), "1") ? View.VISIBLE : View.GONE);
        starBar.setStarMark(Float.valueOf(detailBean.getAverage_star()));
        bookEvaluate.setText(detailBean.getAverage_score() + "");
        bookEvaluate.setVisibility(detailBean.getAverage_score() > 0 ? View.VISIBLE : View.GONE);
        content.setText(detailBean.getDesc());
        content2.setText(detailBean.getDesc());
        //commentNum.setText(detailBean.getTotal_comments() + "");
        updateTime.setText("Update: " + detailBean.getUpdated_at());
        allComments.setText("view all（" + detailBean.getTotal_comments() + "）");
        if (!TextUtils.isEmpty(detailBean.getTag())) {
            if (txs == null || txs.size() == 0) {
                txs = new ArrayList<TextView>();
                String[] tagss = detailBean.getTag().replace("|", "").split("/");
                for (String s : tagss) {
                    TextView textView = new TextView(this);
                    textView.setBackgroundResource(R.drawable.bg_conner_tags);
                    int dps = (int) getResources().getDisplayMetrics().density;
                    textView.setTextSize(14);
                    String ss = s.trim();
                    textView.setText(ss);
                    textView.setPadding(10 * dps, 6 * dps, 10 * dps, 6 * dps);
                    ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.rightMargin = 15 * dps;
                    layoutParams.bottomMargin = 10 * dps;
                    textView.setLayoutParams(layoutParams);
                    txs.add(textView);
                    tags.addView(textView);
                }
            }

        } else {
            tags.setVisibility(View.GONE);
        }
//        if(equals(TextUtils.isEmpty(detailBean.getTag()))){
//            tags.setVisibility(View.GONE);
//        }
        book_ml.setText(detailBean.getIswz() == Constants.lianzai ? ("Chapter " + detailBean.getAllchapter()) : "Chapter " + detailBean.getAllchapter());
        bookStatus.setText(detailBean.getIswz() == Constants.lianzai ? getString(R.string.lianzai) : getString(R.string.wanjie));
        addBook.setVisibility(detailBean.getIs_shelve().equals("1") ? View.GONE : View.VISIBLE);
        existBook.setVisibility(detailBean.getIs_shelve().equals("1") ? View.VISIBLE : View.GONE);

        List<String> urls = new ArrayList<>();
        String lastName = "";
        for (BookDetailBean.Reward reward :
                detailBean.getRewards()) {
            if (reward.getUser() != null) {
                urls.add(reward.getUser().getAvatar());
                lastName = reward.getUser().getNickname();
            }
        }
        pileavert_reword.setAvertImages(urls);
        if (lastName.length() > 5) {
            lastName = lastName.substring(lastName.length() - 5);
        }
        //reword_users.setText(String.format(getString(R.string.reword_user_format), lastName, detailBean.getRewards_total()));
        reword_users.setText(String.format(getString(R.string.reword_user_format), lastName, detailBean.getRewards_total()));

        ShowTags(detailBean);
    }

    AddShelveDialog adddialog;

    private void showAddShelveDialog() {
        addshelve();
    }

    TipDialog movedialog;

    private void showRemoveShelveDialog() {
        removeshelve();
    }

    private void addshelve() {
        HttpClient.getInstance().get(AllApi.addshelve, AllApi.addshelve)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (adddialog != null && adddialog.isShowing()) adddialog.dismiss();
                        getInfo(anime_id);
                    }
                });
    }

    private void removeshelve() {
        HttpClient.getInstance().get(AllApi.removeshelve, AllApi.removeshelve)
                .params("id", anime_id)
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (movedialog != null && movedialog.isShowing()) movedialog.dismiss();
                        getInfo(anime_id);
                    }
                });
    }

    private void changeGuess2() {
        if (guessList == null) {
            guessList = new ArrayList<>();
        } else {
            guessList.clear();
        }
        MainHttpUtil.changeguess(anime_id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                for (int i = 0; i < info.length; i++) {
                    BookDetailBean.GuessBean bookDetailBean = new Gson().fromJson(info[i], BookDetailBean.GuessBean.class);
                    guessList.add(bookDetailBean);
                }
                guessAdapter.setData(guessList);
            }
        });
    }

    private void changeGuess() {
        if (guessList.size() == 0) {
            toast("No more");
            return;
        }
        setGuessView();
    }

    private void setGuessView() {
        if (!isFirst) return;
        guessAdapter.setData(guessList);
        guessAdapter.notifyDataSetChanged();
        isFirst = false;
    }

    private void initEvaluateRv() {
        //LinearLayoutManager layoutManager = new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);  //设置方向
        bookRv.setLayoutManager(layoutManager);
        evaluateAdapter = new BookEvaluateAdapter(getContext());
        evaluateAdapter.setOnClickListener(new BookEvaluateAdapter.OnClickListener() {
            @Override
            public void onActionClick(BookDetailBean.CommentsBean bean) {
                BookPlDetailActivity.start(getContext(), bean.getId());
            }

            @Override
            public void onLikeClick(BookDetailBean.CommentsBean bean) {
                if (bean.getIs_admire().equals(Constants.LIKE)) {
                    cancelLike(bean);
                } else {
                    commentLike(bean);
                }
            }
        });
        bookRv.setAdapter(evaluateAdapter);
    }

    private void commentLike(BookDetailBean.CommentsBean bean) {
        HttpClient.getInstance().post(AllApi.commentlike, AllApi.commentlike)
                .params("comment_id", bean.getId())
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        bean.setIs_admire(Constants.LIKE);
                        bean.setLikes(bean.getLikes() + 1);
                        evaluateAdapter.notifyDataSetChanged();
                        toast(msg);
                    }
                });
    }

    private void cancelLike(BookDetailBean.CommentsBean bean) {
        HttpClient.getInstance().post(AllApi.cancellike, AllApi.cancellike)
                .params("comment_id", bean.getId())
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        bean.setIs_admire(Constants.UNLIKE);
                        bean.setLikes(bean.getLikes());
                        evaluateAdapter.notifyDataSetChanged();
                        toast(msg);
                    }
                });
    }

    private void initLikeRv() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
        bookLikeRv.setLayoutManager(gridLayoutManager);
//        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams)bookLikeRv.getLayoutParams();
//        lp.rightMargin= DpUtil.dip2px(getContext(), 10);

        guessAdapter = new StackRoomGuessAdapter(getContext());
        guessAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(getContext(), guessAdapter.getItem(position).getId());
            }
        });
        bookLikeRv.setAdapter(guessAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_read, R.id.ll_book_ml, R.id.all_comments, R.id.comment, R.id.change, R.id.exist_book, R.id.add_book, R.id.expand, R.id.btn_reword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_read:
                read(anime_id, "1");
                break;
            case R.id.ll_book_ml:
                if (detailBean != null) {
                    BookCatalogueActivity.start(getContext(), anime_id, detailBean.getIs_shelve());
                }
                break;
            case R.id.all_comments:
                BookPlListActivity.start(getContext(), anime_id);
                break;
            case R.id.comment:
                MMKV mmkv = MMKV.defaultMMKV();
                UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                if (userBean.getBindStatus().equals("0")) {
                    ThirdLoginActivity.start(getApplicationContext(), ThirdLoginActivity.EnterIndex.OTHER);
                    return;
                }

                EvaluateBookActivity.start(getContext(), anime_id);
                break;
            case R.id.change:
                changeGuess2();
                break;
            case R.id.add_book:

                showAddShelveDialog();
                break;
            case R.id.exist_book:
                showRemoveShelveDialog();
                break;
            case R.id.expand:
                isExpand = !isExpand;
                content.setVisibility(isExpand ? View.GONE : View.VISIBLE);
                content2.setVisibility(isExpand ? View.VISIBLE : View.GONE);

                isdown.setVisibility(isExpand ? ImageView.GONE : ImageView.VISIBLE);
                isup.setVisibility(isExpand ? ImageView.VISIBLE : ImageView.GONE);

                break;
            case R.id.btn_reword:
                GetRewordPackage();
                break;
        }
    }

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

    private void commitReword(String coin) {
        HttpClient.getInstance().post(AllApi.commitReword, AllApi.commitReword)
                .params("anid", anime_id)
                .params("coin", coin)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        rewordBottomDialog.dismiss();
                        toast(msg);
                        getInfo(anime_id);
                    }
                });
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

    //TODO 替换为JsReadActivity
    public void read(int anime_id, String chaps) {
        if (detailBean == null) {
            return;
        }

        HttpClient.getInstance().post(AllApi.bookchapter, AllApi.bookchapter)
                .isMultipart(true)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        int bookId = detailBean.getId();
                        ChapterBean chapterBean = new Gson().fromJson(info[0], ChapterBean.class);
                        List<BookChapterBean> chapters = new ArrayList<>();
                        List<ChapterBean.ChaptersBean> chaptersBeanList = chapterBean.getChapters();
                        for (int i = 0; i < chaptersBeanList.size(); i++) {
                            ChapterBean.ChaptersBean chaptersBean = chaptersBeanList.get(i);
                            BookChapterBean bookChapterBean = new BookChapterBean();
                            bookChapterBean.setTitle(chaptersBean.getTitle());
                            bookChapterBean.setBookId(bookId + "");
                            bookChapterBean.setLink(chaptersBean.getChaps());
                            bookChapterBean.setUnreadble(true);
                            bookChapterBean.setCoin(Integer.parseInt(chaptersBean.getCoin()));
                            bookChapterBean.setIs_pay(TextUtils.equals(chaptersBean.getIs_pay(), "1"));
                            chapters.add(bookChapterBean);
                        }
                        CollBookBean collBookBean = new CollBookBean();
                        collBookBean.set_id(bookId + "");
                        collBookBean.setAuthor(chapterBean.getAuthor());
                        collBookBean.setTitle(chapterBean.getTitle());
                        collBookBean.setCoverPic(chapterBean.getCoverpic());
                        collBookBean.setShareTitle(chapterBean.getSharetitle());
                        collBookBean.setShortIntro(chapterBean.getSharedesc());
                        collBookBean.setShare_link(chapterBean.getShare_link());
                        collBookBean.setChaptersCount(chapterBean.getAllchapter());
                        collBookBean.setUpdated(detailBean.getUpdated_at());
                        collBookBean.setLastRead("");
                        collBookBean.setPaychapter(chapterBean.getPaychapter());
                        collBookBean.setIsLocal(false);
                        collBookBean.setHasCp(false);
                        collBookBean.setIsUpdate(false);
                        collBookBean.setLatelyFollower(0);
                        collBookBean.setRetentionRatio(0.0);
                        collBookBean.setBookChapters(chapters);
                        collBookBean.setIswz(chapterBean.getIswz());
                        collBookBean.setAllchapter(chapterBean.getAllchapter());
                        JsReadActivity.startActivity(getContext(), collBookBean, false, 0, detailBean.getIs_shelve(), false);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateCopyright(String envent) {
        if (envent.equals("-1")) {
            rlRecommendTitle.setVisibility(View.VISIBLE);
            bookLikeRv.setVisibility(View.VISIBLE);
        } else {
            rlRecommendTitle.setVisibility(View.GONE);
            bookLikeRv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        HomeActivity.onShowComplete(HomeActivity.FirstShowType.SignInfo);
    }
}
