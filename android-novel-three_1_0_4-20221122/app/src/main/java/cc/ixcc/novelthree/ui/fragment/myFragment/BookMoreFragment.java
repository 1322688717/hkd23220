package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.BookMoreBean;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.BookMoreAdapter;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

/**
 * 更多viewPage
 */
public class BookMoreFragment extends MyFragment<HomeActivity> {
    String type = "";
    String id;
    @BindView(R.id.rv_book)
    WrapRecyclerView rvBook;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.scroll_view)
    LinearLayout scrollView;
    BookMoreAdapter adapter;
    int page = 1;
    List<BookMoreBean.ListBean> booklist = new ArrayList<>();

    public static BookMoreFragment newInstance(String id, String type) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("type", type);
        BookMoreFragment fragment = new BookMoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book_more;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getString("id");
            type = arguments.getString("type");
        }

        setAndroidNativeLightStatusBar(false);
        initMoreRv();
        getInfo();
    }

    @Override
    protected void initData() {

    }

    private void initMoreRv() {
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getInfo();
            }
        });
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getInfo();
            }
        });

        adapter = new BookMoreAdapter(getContext());
        rvBook.setAdapter(adapter);
        rvBook.setNestedScrollingEnabled(false);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(getContext(), booklist.get(position).getAnid());
            }
        });
    }

    private void getInfo() {
        HttpClient.getInstance().get(AllApi.getbookmore, AllApi.getbookmore)
            .params("channel_id", id)
            .params("type", type)
            .params("page", page)
            .params("page_size", PAGE_SIZE)
                .params("copyright", AppContext.sInstance.getTenjinFlag())
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    BookMoreBean bean = new Gson().fromJson(info[0], BookMoreBean.class);
                    List<BookMoreBean.ListBean> list = bean.getList();
                    if (page == 1) booklist.clear();
                    booklist.addAll(list);
                    adapter.setData(booklist);
                    if (page == 1) {
                        refresh.finishRefresh(500);
                    }
                    else {
                        refresh.finishLoadMore(500);
                    }
                }
            });
    }

    private void setAndroidNativeLightStatusBar(boolean light) {
        // 默认状态栏字体颜色为黑色
        // 解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
        ImmersionBar.with(this).statusBarDarkFont(!light).keyboardEnable(true).init();
    }
}
