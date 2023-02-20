package cc.ixcc.novelthree.ui.activity.my;

import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.SearchInfoBean;
import cc.ixcc.novelthree.bean.SearchResultBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.helper.BindEventBus;
import cc.ixcc.novelthree.helper.KeyboardUtils;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.adapter.RecommendAdapter_new;
import cc.ixcc.novelthree.ui.adapter.SearchHistoryAdapter;
import cc.ixcc.novelthree.ui.adapter.SearchHotAdapter;
import cc.ixcc.novelthree.ui.adapter.SearchResultAdapter;
import com.jiusen.base.BaseAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * desc   : 搜索书架
 */
@BindEventBus
public final class SearchShelveActivity extends MyActivity
        implements
        KeyboardWatcher.SoftKeyboardStateListener {

    @BindView(R.id.refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.ll_default)
    LinearLayout mLLDefault;
    @BindView(R.id.ll_result)
    LinearLayout mLLResult;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.rv_history)
    RecyclerView mHistoryRv;
    @BindView(R.id.rv_hot)
    RecyclerView mSearchHotRv;
    @BindView(R.id.rv_tuijian)
    RecyclerView mTuiJianRv;
    @BindView(R.id.rv_result)
    RecyclerView mResultRv;
    @BindView(R.id.toolbar)
    LinearLayout mToolBar;
    @BindView(R.id.ll_no_more)
    LinearLayout mNoMore;
    @BindView(R.id.rl_recommend)
    RelativeLayout rlRecommend;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private SearchHotAdapter mSearchHotAdapter;
    private RecommendAdapter_new mtuijianAdapter;
    private SearchResultAdapter mResultAdapter;
    private SearchInfoBean mSearchInfoBean;
    private int page = 1;
    private List<SearchResultBean> mSearchResultList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_shelve;
    }

    @Override
    protected void initView() {
        initHistoryRv();
        initSearchHotRv();
        initTuijianRv();
        initResultRv();
        setOnClickListener(R.id.tv_cancel, R.id.tv_clear);
        ImmersionBar.setTitleBar(this, mToolBar);
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchChange(s.toString());
            }
        });
        mEtContent.setOnEditorActionListener((arg0, arg1, arg2) -> {
            if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                searchChange(mEtContent.getText().toString());
                searchkey(mEtContent.getText().toString());
                page = 1;
                KeyboardUtils.hideKeyboard(mEtContent);
            }
            return false;
        });
        searchChange("");
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                searchkey(mEtContent.getText().toString());
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                searchkey(mEtContent.getText().toString());
            }
        });
        getInfo();
        rlRecommend.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
        mTuiJianRv.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
    }

    boolean isFirst = true;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateCopyright(String envent) {
//        Log.e("EventBus", "BookShelveFragment--Copyright接受数据" + envent);
//        rlRecommend.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
//        mTuiJianRv.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
        if (envent.equals("-1")) {
            rlRecommend.setVisibility(View.VISIBLE);
            mTuiJianRv.setVisibility(View.VISIBLE);
        } else {
            rlRecommend.setVisibility(View.GONE);
            mTuiJianRv.setVisibility(View.GONE);
        }
    }
    public void getInfo() {
        HttpClient.getInstance().get(AllApi.searchinfo, AllApi.searchinfo)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        mSearchInfoBean = new Gson().fromJson(info[0], SearchInfoBean.class);
                        setView();
                    }

//
//                    @Override
//                    public boolean showLoadingDialog() {
//                        return isFirst;
//                    }
//
//                    @Override
//                    public Dialog createLoadingDialog() {
//                        return DialogUitl.loadingDialog(getContext(), "");
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                        isFirst = false;
//                    }
                });
    }

    private void setView() {
        if (mSearchInfoBean == null) return;
        List<String> searchlist = mSearchInfoBean.getSearch();
        if (searchlist.size() > 10) {
            searchlist = searchlist.subList(0, 10);
        }
        List<SearchInfoBean.RecommendBean> recommendlist = mSearchInfoBean.getRecommend();
        if (recommendlist.size() > 6) {
            recommendlist = recommendlist.subList(0, 6);
        }
//
//        Random random = new Random();
//        int n = random.nextInt(list.size());
//        list.get(n);

        mSearchHistoryAdapter.setData(searchlist);
        mSearchHotAdapter.setData(mSearchInfoBean.getHot_keywords());
        mtuijianAdapter.setData(recommendlist);
    }

    private void searchChange(String content) {
        if (TextUtils.isEmpty(content)) {
            mSearchResultList.clear();
            mResultAdapter.notifyDataSetChanged();
        }
        mLLDefault.setVisibility(TextUtils.isEmpty(content) ? View.VISIBLE : View.GONE);
        mLLResult.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        mRefreshLayout.setEnableRefresh(!TextUtils.isEmpty(content));
        mRefreshLayout.setEnableLoadMore(!TextUtils.isEmpty(content));
    }

    private void initHistoryRv() {
        mSearchHistoryAdapter = new SearchHistoryAdapter(this);
        mHistoryRv.setLayoutManager(new FlexboxLayoutManager(this));
//        mHistoryRv.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mSearchHistoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                mEtContent.setText(mSearchHistoryAdapter.getItem(position));
                mEtContent.setSelection(mSearchHistoryAdapter.getItem(position).length());
                searchkey(mSearchHistoryAdapter.getItem(position));
            }
        });
        mHistoryRv.setAdapter(mSearchHistoryAdapter);
        mHistoryRv.setNestedScrollingEnabled(false); //禁止滑动
    }

    private void initSearchHotRv() {
        mSearchHotAdapter = new SearchHotAdapter(this);
        //mSearchHotRv.setLayoutManager(new FlexboxLayoutManager(this));
        mSearchHotRv.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        mSearchHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                mEtContent.setText(mSearchHotAdapter.getItem(position));
                mEtContent.setSelection(mSearchHotAdapter.getItem(position).length());
                searchkey(mSearchHotAdapter.getItem(position));
            }
        });
        mSearchHotRv.setAdapter(mSearchHotAdapter);
        mSearchHotRv.setNestedScrollingEnabled(false); //禁止滑动
    }

    private void initTuijianRv() {

        mtuijianAdapter = new RecommendAdapter_new(this);

        mTuiJianRv.setLayoutManager(new FlexboxLayoutManager(this));
//        mTuiJianRv.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        mtuijianAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(getContext(), mtuijianAdapter.getItem(position).getAnid());
            }
        });
        mTuiJianRv.setAdapter(mtuijianAdapter);
        mTuiJianRv.setNestedScrollingEnabled(false); //禁止滑动
    }

    private void initResultRv() {
        mResultAdapter = new SearchResultAdapter(this);
        mResultAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(getContext(), mResultAdapter.getItem(position).getAnid());
            }
        });
        mResultRv.setAdapter(mResultAdapter);
        mResultRv.setNestedScrollingEnabled(false); //禁止滑动
    }

    private void clearsearch() {
        HttpClient.getInstance().get(AllApi.clearsearch, AllApi.clearsearch)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        mSearchHistoryAdapter.getData().clear();
                        mSearchHistoryAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onRightClick(View v) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_clear:
                clearsearch();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {

    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    private void searchkey(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
//            toast("请输入搜索内容");
            return;
        }
        HttpClient.getInstance().post(AllApi.search, AllApi.search)
                .params("keyword", keyword)
                .params("page", page)
                .params("page_size", PAGE_SIZE)
                .params("copyright", AppContext.sInstance.getTenjinFlag())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        List<SearchResultBean> list = new ArrayList<>();
                        for (int i = 0; i < info.length; i++) {
                            SearchResultBean bean = new Gson().fromJson(info[i], SearchResultBean.class);
                            list.add(bean);
                        }
                        if (page == 1) {
                            mSearchResultList.clear();
                        }
                        mSearchResultList.addAll(list);
                        mResultAdapter.setData(mSearchResultList);
                        if (page == 1) {
                            mNoMore.setVisibility(View.GONE);
                            mRefreshLayout.finishRefresh(500);
                        } else {
                            if (list == null || list.size() == 0) {
                                mNoMore.setVisibility(View.VISIBLE);
//                mRefreshLayout.finishLoadMoreWithNoMoreData(); //没有更多数据
                            } else {
                                mNoMore.setVisibility(View.GONE);
                            }
                            mRefreshLayout.finishLoadMore(500);
                        }
                        getInfo();
                    }
                });
    }
}