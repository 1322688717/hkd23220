package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.content.Intent;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import cc.ixcc.novelthree.Constants;

import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.bean.ActiviyNoticeInfo;
import cc.ixcc.novelthree.bean.ChapterBean;
import cc.ixcc.novelthree.bean.ConfigBean;
import cc.ixcc.novelthree.bean.ShelveBookBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.custom.ShelveBannerLoader;
import cc.ixcc.novelthree.event.CancelSelectBookEvent;
import cc.ixcc.novelthree.event.SelectBookEvent;
import cc.ixcc.novelthree.event.SkipStackEvent;
import cc.ixcc.novelthree.helper.BindEventBus;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.jsReader.Activity.JsReadActivity;
import cc.ixcc.novelthree.jsReader.model.bean.BookChapterBean;
import cc.ixcc.novelthree.jsReader.model.bean.CollBookBean;
import cc.ixcc.novelthree.statistics.AdjustUtil;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.SearchRecordActivity;
import cc.ixcc.novelthree.ui.activity.my.SearchShelveActivity;
import cc.ixcc.novelthree.ui.activity.my.ThirdLoginActivity;
import cc.ixcc.novelthree.ui.activity.my.TopUpActivity;
import cc.ixcc.novelthree.ui.activity.my.vip.OpenVipActivity;
import cc.ixcc.novelthree.ui.adapter.BookShelveAdapter1;
import cc.ixcc.novelthree.ui.adapter.BookShelveAdapter2;
import cc.ixcc.novelthree.ui.dialog.TimeSortDialog;
import cc.ixcc.novelthree.utils.SpUtil;

import com.jiusen.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mmkv.MMKV;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;
import static cc.ixcc.novelthree.Constants.SHELVE1;
import static cc.ixcc.novelthree.Constants.SHELVE2;
import static cc.ixcc.novelthree.Constants.SHELVE_SORT_READ;
import static cc.ixcc.novelthree.Constants.SHELVE_SORT_UPDATE;

/**
 * desc   : 书架Fragment
 */
@BindEventBus
public final class BookShelveFragment extends MyFragment<HomeActivity> implements View.OnClickListener {
    @BindView(R.id.search)
    LinearLayout mSearch;
    @BindView(R.id.toolbar)
    LinearLayout mToolBar;
    @BindView(R.id.rl_sign)
    RelativeLayout mRlSign;
    @BindView(R.id.more)
    ImageView mMore;
    @BindView(R.id.tv_all_select)
    TextView mAllSelect;
    @BindView(R.id.select_number)
    TextView mSelectNumber;
    @BindView(R.id.tv_cancel)
    TextView mCancel;
    @BindView(R.id.rl_select_status)
    LinearLayout mSelectStatus;
    @BindView(R.id.rv_book)
    WrapRecyclerView mBookRecycleView;
    @BindView(R.id.rl_refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.sign)
    LinearLayout sign;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.gostack)
    TextView gostack;
    @BindView(R.id.no_data)
    LinearLayout noData;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.info2)
    TextView info2;
    @BindView(R.id.no_network)
    LinearLayout noNetwork;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sign_name)
    TextView signName;

    @BindView(R.id.banner)
    Banner banner;

    private BookShelveAdapter1 mAdapter1;
    private BookShelveAdapter2 mAdapter2;
    private int type;//对应不同排版
    private List<ShelveBookBean.ListBean> mDataList = new ArrayList<>();
    private List<ShelveBookBean.ListBean> mSelectDataList = new ArrayList<>(); //选中书本
    private LayoutInflater mInflater;
    private PopupWindow mMenuWindow;
    private boolean isAllSelect = false; //是否全选
    private boolean isSelectShow = false; //是否在选择
    int page = 1;
    int sort = SHELVE_SORT_READ;
    boolean isFirst = true;

    private static BookShelveFragment instance = null;

    public static BookShelveFragment getInstance() {
        if (instance == null) {
            instance = new BookShelveFragment();
        }
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_shelve;
    }

    @Override
    protected void initView() {
        // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
        ImmersionBar.setTitleBar(getAttachActivity(), mToolBar);
        title.getPaint().setFakeBoldText(true);
        //signName.setText("Sign in"); // + Constants.getInstance().getCoinName()

//        EventBus.getDefault().unregister(this);
//        EventBus.getDefault().register(this);
        setOnClickListener(R.id.sign, R.id.search, R.id.more, R.id.tv_all_select, R.id.tv_cancel, R.id.gostack);
        mInflater = LayoutInflater.from(getContext());
        initMenu();
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getInfo();
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getInfo();
            }
        });

        //初始化
        HttpClient.getInstance().post(AllApi.appinit, AllApi.appinit)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        ConfigBean config = new Gson().fromJson(info[0], ConfigBean.class);
                        Constants.getInstance().setConfig(config);
                        MMKV.defaultMMKV().encode(SpUtil.CONFIG, config);
                        setType(Integer.parseInt(config.getShelve_style()));
                    }
                });


        initBanner();
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateCopyright(String envent) {
        Log.e("EventBus", "BookShelveFragment--Copyright接受数据" + envent);
        if (envent.equals("-1")) {
            banner.setVisibility(View.VISIBLE);
        } else {
            banner.setVisibility(View.GONE);
        }
    }

    public void ShowBanner(HomeActivity mHomeActivity) {
        //如果有运营位则显示 没有则隐藏
        if (mHomeActivity.adList_shelve.size() > 0) {
            banner.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
//            banner.setVisibility(View.VISIBLE);

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
            banner.setImages(mHomeActivity.adList_shelve);
            //设置轮播时间
            banner.setDelayTime(5000);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Log.e("TAG", "banner_position:" + position);
                    if (mHomeActivity.adList_shelve != null) {
                        if (position >= 0 && position < mHomeActivity.adList_shelve.size()) {
                            ActiviyNoticeInfo bean = mHomeActivity.adList_shelve.get(position);
                            if (bean != null) {
//                                AdjustUtil.GetInstance().SendBannerEvent(2);

                                UserBean tempBean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                                //5书架支付活动 6书架新书推荐 7书架VIP活动 8书架URL活动
                                switch (bean.getType()) {
                                    case 5:
                                        if (tempBean.getBindStatus().equals("0")) {
                                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                                        } else {
                                            startActivity(TopUpActivity.class);
                                        }
                                        break;
                                    case 6:
                                        BookDetailActivity.start(getContext(), bean.getAnid());
                                        break;
                                    case 7:
                                        if (tempBean.getBindStatus().equals("0")) {
                                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                                        } else {
                                            OpenVipActivity.start(getContext());
                                        }
                                        break;
                                    case 8:
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setType(int t) {
        type = t;
        if (type == SHELVE1) {
            mAdapter1 = new BookShelveAdapter1(getContext());
            mBookRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
            mBookRecycleView.setAdapter(mAdapter1);
            mAdapter1.setOnClickListener(new BookShelveAdapter1.OnClickListener() {
                @Override
                public void addBook() {
                    //跳转书库
                    EventBus.getDefault().post(new SkipStackEvent());
                }

                @Override
                public void selectBook(boolean select, ShelveBookBean.ListBean bean) {
                    selectBook2(select, bean);
                }

                @Override
                public void readBook(ShelveBookBean.ListBean bean) {
                    read(bean.getAnid(), bean.getRead_chaps(), Constants.SHELVE_EXIST);
                }
            });
        } else if (type == SHELVE2) {
            mAdapter2 = new BookShelveAdapter2(getContext());
            mBookRecycleView.setAdapter(mAdapter2);
            mAdapter2.setOnClickListener(new BookShelveAdapter2.OnClickListener() {
                @Override
                public void addBook() {
                    //跳转书库
                    EventBus.getDefault().post(new SkipStackEvent());
                }

                @Override
                public void selectBook(boolean select, ShelveBookBean.ListBean bean) {
                    selectBook2(select, bean);
                }

                @Override
                public void readBook(ShelveBookBean.ListBean bean) {
                    read(bean.getAnid(), bean.getRead_chaps(), Constants.SHELVE_EXIST);
                }
            });
        }
        getInfo();
    }

    //TODO 替换为JsReadActivity
    public void read(int anime_id, int chaps, String isshelve) {
        HttpClient.getInstance().post(AllApi.bookchapter, AllApi.bookchapter)
                .isMultipart(true)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        ChapterBean chapterBean = new Gson().fromJson(info[0], ChapterBean.class);
                        int bookId = chapterBean.getAnid();
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

                        //最后更新时间
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
                        JsReadActivity.startActivity(getContext(), collBookBean, false, chaps, isshelve, false);
                    }
                });
    }

    private void initMenu() {
        View v = mInflater.inflate(R.layout.menu_book_shelve, null);
        mMenuWindow = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mMenuWindow.setOutsideTouchable(true);
        mMenuWindow.setOnDismissListener(new poponDismissListener());
        mMenuWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.color.black));
        TextView readRecordView = v.findViewById(R.id.read_record);
        TextView arrangeShelfView = v.findViewById(R.id.arrange_shelf);
        TextView bookRangeView = v.findViewById(R.id.book_range);
        readRecordView.setOnClickListener(v1 -> {
            startActivity(SearchRecordActivity.class);
            dismissMenu();
        });
        arrangeShelfView.setOnClickListener(v1 -> {
            if (mDataList.size() == 0) {
                dismissMenu();
                return;
            }
            EventBus.getDefault().post(new SelectBookEvent(-1));
            dismissMenu();
        });
        bookRangeView.setOnClickListener(v1 -> {
            if (mDataList.size() == 0) {
                dismissMenu();
                return;
            }
            showTimeSortDialog();
            dismissMenu();
        });
    }

    private void showTimeSortDialog() {
        final TimeSortDialog dialog = TimeSortDialog.getMyDialog(getAttachActivity());
        //回调实现点击
        dialog.setDialogCallBack(new TimeSortDialog.DialogCallBack() {
            @Override
            public void onUpdateTimeClick() {
                sort = SHELVE_SORT_UPDATE;
                getInfo();
                dialog.dismiss();
            }

            @Override
            public void onReadTimeClick() {
                sort = SHELVE_SORT_READ;
                getInfo();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getInfo() {
        Log.e("TAG", "getInfo");
        HttpClient.getInstance().get(AllApi.bookshelf, AllApi.bookshelf)
                .params("page", page)
                .params("page_size", PAGE_SIZE)
                .params("sort", sort)
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        showNonetwork(false);
                        ShelveBookBean bean = new Gson().fromJson(info[0], ShelveBookBean.class);
                        setView(bean);
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        showNonetwork(true);
                        mRefreshLayout.finishRefresh(500);
                        mRefreshLayout.finishLoadMore(500);
                    }
                });
    }

    private void setView(ShelveBookBean bean) {
        if (time == null) return;
        List<ShelveBookBean.ListBean> list = bean.getList();
        if (page == 1) {
            time.setText(bean.getToday_read_time() + "");
            mDataList.clear();
            if (type == SHELVE1) {
                if (mAdapter1 != null) {
                    mAdapter1.clearData();
                }
            } else if (type == SHELVE2) {
                if (mAdapter2 != null) {
                    mAdapter2.clearData();
                }
            }
            mDataList.addAll(list);
            addPlace(true);
            allSelect(false, true);
            showNodata();
            mRefreshLayout.finishRefresh(500);
        } else {
            addPlace(false);
            mDataList.addAll(list);
            addPlace(true);
            allSelect(false, false);
            showNodata();
            mRefreshLayout.finishLoadMore(500);
        }
    }

    private void showNodata() {
        boolean isnodata = mDataList == null || mDataList.size() == 0;
        noData.setVisibility(isnodata ? View.VISIBLE : View.GONE);
        mBookRecycleView.setVisibility(isnodata ? View.GONE : View.VISIBLE);
    }

    private void showNonetwork(boolean nonetwork) {
        if (noNetwork == null) return;
        noNetwork.setVisibility(nonetwork ? View.VISIBLE : View.GONE);
        noData.setVisibility(nonetwork ? View.GONE : View.VISIBLE);
        mBookRecycleView.setVisibility(nonetwork ? View.GONE : View.VISIBLE);
    }

    private void setAndroidNativeLightStatusBar(boolean light) {
        ImmersionBar.with(this).statusBarDarkFont(!light).keyboardEnable(true).init();
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private void showMenu() {
        if (mMenuWindow != null) {
            dismissMenu();
            PopupWindowCompat.showAsDropDown(mMenuWindow, mMore, -255, -30, Gravity.START);
        }
        backgroundAlpha(0.15f);
    }

    private void dismissMenu() {
        if (mMenuWindow != null) {
            mMenuWindow.dismiss();
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getAttachActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getAttachActivity().getWindow().setAttributes(lp);
    }

    private void selectBook2(boolean select, ShelveBookBean.ListBean bean) {
        boolean isExist = false;
        int position = -1;
        for (int i = 0; i < mSelectDataList.size(); i++) {
            ShelveBookBean.ListBean bookBean = mSelectDataList.get(i);
            if (bookBean.getId() == bean.getId()) {
                isExist = true;
                position = i;
                break;
            }
        }
        if (select && !isExist) {
            mSelectDataList.add(bean);
        } else if (!select && position != -1) {
            mSelectDataList.remove(position);
        }
        bean.setCheck(select);
        if (type == SHELVE1) mAdapter1.notifyDataSetChanged();
        else if (type == SHELVE2) mAdapter2.notifyDataSetChanged();
        mSelectNumber.setText("Delete ( " + mSelectDataList.size() + " )");
        changeStatus();
    }

    @Override
    protected void initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SelectBookEvent e) {
        setSelectedBook(e.getPosition(), true);
    }

    public void setSelectedBook(int position, boolean select) {
        isAllSelect = false;
        isSelectShow = select;
        mSelectDataList.clear();
        for (ShelveBookBean.ListBean bean : mDataList) {
            bean.setCheckshow(select);
            bean.setCheck(false);
        }
        if (type == SHELVE1) {
            if (mAdapter1 != null) {
                mAdapter1.setData(mDataList);
            }
        } else if (type == SHELVE2) {
            if (mAdapter2 != null) {
                mAdapter2.setData(mDataList);
            }
        }
        if (select && position != -1)
            selectBook2(select, mDataList.get(position));
        mToolBar.setVisibility(select ? View.GONE : View.VISIBLE);
        mRlSign.setVisibility(select ? View.GONE : View.VISIBLE);
        mSelectStatus.setVisibility(select ? View.VISIBLE : View.GONE);
        ImmersionBar.setTitleBar(getAttachActivity(), select ? mSelectStatus : mToolBar);
    }

    public void allSelect(boolean allSelect, boolean isRefresh) {
        List<ShelveBookBean.ListBean> list = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            boolean isExist = false;
            ShelveBookBean.ListBean bean = mDataList.get(i);
            for (int j = 0; j < mSelectDataList.size(); j++) {
                if (bean.getId() == mSelectDataList.get(j).getId()) {
                    bean.setCheckshow(isSelectShow);
                    bean.setCheck(true);
                    if (isRefresh) {
                        list.add(bean);
                    }
                    isExist = true;
                    break;
                }
            }
            if (isExist) continue;
            bean.setCheckshow(isSelectShow);
            bean.setCheck(allSelect);
            if (allSelect && i < mDataList.size() - 1) {
                mSelectDataList.add(bean);
            }
        }
        if (isRefresh) {
            mSelectDataList = list;
        }
        mSelectNumber.setText("Delete ( " + mSelectDataList.size() + " )");
        if (type == SHELVE1) {
            if (mAdapter1 != null) {
                mAdapter1.setData(mDataList);
            }
        } else if (type == SHELVE2) {
            if (mAdapter1 != null) {
                mAdapter2.setData(mDataList);
            }
        }
        changeStatus();
    }

    public void changeStatus() {
        mAllSelect.setText(mSelectDataList.size() == mDataList.size() - 1 ? "Unselect all" : "select all");
        isAllSelect = mSelectDataList.size() == mDataList.size() - 1;
    }

    public List<ShelveBookBean.ListBean> getSelectBookList() {
        return mSelectDataList;
    }

    public void removeShelve() {
        String id = "";
        for (ShelveBookBean.ListBean bean : mSelectDataList) {
            id += bean.getAnid() + ",";
        }
        if (TextUtils.isEmpty(id)) {
            toast("Please select a book");
            return;
        }
        id = id.substring(0, id.length() - 1);
        removeshelve(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isVisibleToUser) return;
        try {
            if (mRefreshLayout.getState() == RefreshState.None) {
                mRefreshLayout.autoRefresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAndroidNativeLightStatusBar(false);
            }
        }, 10);
    }

    boolean isVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "getInfo setUserVisibleHint");
                    getInfo();
                    setAndroidNativeLightStatusBar(false);
                }
            }, 10);
            //相当于Fragment的onResume，为true时，Fragment已经可见
        } else {
            //相当于Fragment的onPause，为false时，Fragment不可见
        }
    }

    private void removeshelve(String id) {
        HttpClient.getInstance().get(AllApi.removeshelve, AllApi.removeshelve)
                .params("id", id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        cancel();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                startActivity(SearchShelveActivity.class);
                break;
            case R.id.tv_all_select:
                mSelectDataList.clear();
                allSelect(!isAllSelect, false);
                break;
            case R.id.tv_cancel:
                cancel();
                break;
            case R.id.more:
                showMenu();
                break;
            case R.id.sign:
                //HomeActivity.OnGotoPage(2);
                HomeActivity.mHomeActivity.Gotofrag_weal();
                break;
            case R.id.gostack:
                EventBus.getDefault().post(new SkipStackEvent());
                break;
            default:
                break;
        }
    }

    private void cancel() {
        EventBus.getDefault().post(new CancelSelectBookEvent());
        setSelectedBook(-1, false);
        mSelectNumber.setText("Delete ( " + 0 + " )");
        getInfo();
    }

    private void addPlace(boolean isAdd) {
        if (mDataList.size() <= 0) return;
        if (isAdd) {
            ShelveBookBean.ListBean bookBean = new ShelveBookBean.ListBean();
            bookBean.setId(-1);
            bookBean.setCoverpic("");
            bookBean.setTitle("Added placeholder");
            mDataList.add(mDataList.size(), bookBean);
        } else {
            mDataList.remove(mDataList.size() - 1);
        }
    }
}