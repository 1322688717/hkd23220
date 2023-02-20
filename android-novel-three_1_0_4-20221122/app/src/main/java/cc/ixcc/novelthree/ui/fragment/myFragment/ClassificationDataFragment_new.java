package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.http.okhttp.utils.L;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cc.ixcc.novelthree.bean.ClassficationBookBean;
import cc.ixcc.novelthree.bean.ClassificationTitleBean;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.Data;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.adapter.ClassificationBookAdapter;
import cc.ixcc.novelthree.ui.adapter.ClassificationTitleAdapter;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

public class ClassificationDataFragment_new extends MyFragment<HomeActivity> implements OnItemClickListener {
    @BindView(R.id.rlv_title)
    RecyclerView mRlvTitle;
    @BindView(R.id.rlv_content)
    RecyclerView mRlvContent;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int page;
    private int novelId = -1;
    private ClassificationTitleAdapter classificationTitleAdapter;
    private ClassificationBookAdapter classificationBookAdapter;
    private int issex;

    public static ClassificationDataFragment_new newInstance(int issex) {
        ClassificationDataFragment_new fragment = new ClassificationDataFragment_new();
        Bundle args = new Bundle();
        args.putInt("issex", issex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classification_data_new;
    }

    @Override
    protected void initView() {
        issex = getArguments().getInt("issex", 1);
        classificationTitleAdapter = new ClassificationTitleAdapter();
        classificationBookAdapter = new ClassificationBookAdapter();
        mRlvTitle.setAdapter(classificationTitleAdapter);
        mRlvContent.setAdapter(classificationBookAdapter);
        classificationTitleAdapter.setOnItemClickListener(this);
        classificationBookAdapter.setOnItemClickListener((adapter, view, position) -> BookDetailActivity.start(getContext(), classificationBookAdapter.getItem(position).getId()));
        initRefreshLayout();
        initTitleData(issex);
    }

    private void initTitleData(int issex) {
        HttpClient.getInstance().get(AllApi.bookclass, AllApi.bookclass)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        List<String> strings = Arrays.asList(info);
                        classificationTitleAdapter.setNewInstance(null);
                        if (strings.size() > 0) {
                            HomeActivity.mHomeActivity.Classifications.clear();
                            for (String string : strings) {
                                ClassificationTitleBean data = new Gson().fromJson(string, ClassificationTitleBean.class);
                                //最热和最新不属于分类
                                if (data.getNovel_name().equals("Hot") || data.getNovel_name().equals("Newest")){
                                    continue;
                                }
                                //如果标签背景图为空也不加入分类列表
                                if (data.getCover()!=null && data.getCover().equals("")){
                                    continue;
                                }
                                Log.d("xcs",data.toString());
                                HomeActivity.mHomeActivity.Classifications.add(data);
                                classificationTitleAdapter.addData(data);

//                                if(issex == 1 && data.sex.equals("1"))
//                                {
//                                    classificationTitleAdapter.addData(data);
//                                }
//                                else if(issex == 0 && data.sex.equals("2"))
//                                {
//                                    classificationTitleAdapter.addData(data);
//                                }
                            }
                            classificationTitleAdapter.getItem(0).setSelect(true);
                            novelId = classificationTitleAdapter.getItem(0).getNovel_id();
                            refreshLayout.autoRefresh();

                        }
                    }

                    @Override
                    public void onError(Response<Data> response) {
                        Throwable t = response.getException();
                        L.e("网络请求错误---->" + t.getClass() + " : " + t.getMessage());
                        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException) {
                            ToastUtils.show("Network request failed");
                        }

                        super.onError(response);
                        hideDialog();
                    }
                });

    }

    private void initRefreshLayout() {
     //   refreshLayout.autoRefresh();
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            initDetails();
        });
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 0;
            initDetails();
        });
    }

    @Override
    protected void initData() {

    }

    private void initDetails() {
        if (novelId == -1) return;
        if (issex != 1)
        {
            issex = 2;
        }
        Application application = AppContext.sInstance;
        AppContext appContext = (AppContext) application;
//        Log.e("Tenjinfacebook",appContext.getTenjinFlag()+"");
        HttpClient.getInstance().post(AllApi.getbooks, AllApi.getbooks)
                .params("p", page)
                .params("size", PAGE_SIZE)
                .params("issex", issex)
                .params("novel_id", novelId)
                .params("copyright", appContext.getTenjinFlag())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        hideDialog();
                        List<String> strings = Arrays.asList(info);
                        if (page == 0) {
                            classificationBookAdapter.setNewInstance(null);
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                        if (strings.size() > 0) {
                            for (String string : strings) {
                                classificationBookAdapter.addData(new Gson().fromJson(string, ClassficationBookBean.class));
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        hideDialog();
                        if (page == 0) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                });

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        ArrayList<ClassificationTitleBean> selectList = new ArrayList<>();
        for (ClassificationTitleBean datum : classificationTitleAdapter.getData()) {
            if (datum.getNovel_id() == (classificationTitleAdapter.getItem(position).getNovel_id())) {
                datum.setSelect(true);
                novelId = datum.getNovel_id();

                HomeActivity.mHomeActivity.RefreshClassification_title(datum.getNovel_name());
            } else {
                datum.setSelect(false);
            }
            selectList.add(datum);
        }
        classificationTitleAdapter.setNewInstance(selectList);
        page = 0;
        refreshLayout.autoRefresh();
    }

    public void SetSelect_ClassificationTitleBean(int position)
    {
        ArrayList<ClassificationTitleBean> selectList = new ArrayList<>();
        for (ClassificationTitleBean datum : classificationTitleAdapter.getData()) {
            if (datum.getNovel_id() == (classificationTitleAdapter.getItem(position).getNovel_id())) {
                datum.setSelect(true);
                novelId = datum.getNovel_id();

                HomeActivity.mHomeActivity.RefreshClassification_title(datum.getNovel_name());

                //MoveToPosition((LinearLayoutManager)mRlvTitle.getLayoutManager(),mRlvTitle,position);

                MoveToPosition((LinearLayoutManager)mRlvTitle.getLayoutManager(),position);
            } else {
                datum.setSelect(false);
            }
            selectList.add(datum);
        }
        classificationTitleAdapter.setNewInstance(selectList);
        page = 0;
        refreshLayout.autoRefresh();
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

    public  void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

}