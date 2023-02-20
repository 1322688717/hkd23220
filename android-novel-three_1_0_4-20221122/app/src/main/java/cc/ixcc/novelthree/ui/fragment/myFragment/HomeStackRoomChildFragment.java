package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.bean.StackRoomItemBean;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.custom.CustomRecyclerView;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.jsReader.utils.ScreenUtils;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.BookMoreActivity;
import cc.ixcc.novelthree.ui.adapter.StackRoomChildItemAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

public class HomeStackRoomChildFragment extends MyFragment {
    CustomRecyclerView stackRv;
    SmartRefreshLayout refreshLayout;
    private StackRoomChildItemAdapter itemAdapter;
    private ArrayList<StackRoomItemBean> stackItemList;
    private ScrollProcessListener mProcessListener;

    private int id;
    private int type;
    private int page;
    private int position;
    public static String TAG = "Stack";

    public static HomeStackRoomChildFragment newInstance(int id, int type, int i) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putInt("type", type);
        args.putInt("position", i);
        HomeStackRoomChildFragment homeStackRoomChildFragment = new HomeStackRoomChildFragment();
        homeStackRoomChildFragment.setArguments(args);
        return homeStackRoomChildFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_stack_room_child;
    }

    @Override
    protected void initView() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {

    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//    public void updateCopyright(String envent){
//        Log.e("EventBus","Copyright接受数据"+envent);
//    }

    private boolean isFirst = true;

    private void getData() {
        HttpClient.getInstance().get(AllApi.stackroombook, AllApi.stackroombook)
                .params("channel_id", id)
                .params("page", page)
                .params("page_size", PAGE_SIZE)
                .params("copyright", AppContext.sInstance.getTenjinFlag())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        StackRoomBookBean bean = new Gson().fromJson(info[0], StackRoomBookBean.class);
                        hideDialog();
                        if (bean.getColumn() == null || bean.getColumn().isEmpty()) {
                            refreshLayout.setNoMoreData(true);
                            return;
                        }
                        switch (refreshLayout.getState()) {
                            case Refreshing:
                                stackItemList.clear();
                                refreshLayout.finishRefresh();
                                break;
                            case Loading:
                                refreshLayout.finishLoadMore();
                                break;
                        }
                        showData(bean);
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        hideDialog();
                        switch (refreshLayout.getState()) {
                            case Refreshing:
                                refreshLayout.finishRefresh();
                                break;
                            case Loading:
                                refreshLayout.finishLoadMore();
                                break;
                        }
                    }
                });
    }

    private void delLike(int i) {
        HttpClient.getInstance().post(AllApi.removelike, AllApi.removelike)
                .params("id", itemAdapter.getData().get(0).getAd().get(i).getId())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        StackRoomBookBean.AdBean bean = new Gson().fromJson(info[0], StackRoomBookBean.AdBean.class);
                        List<StackRoomItemBean.AdBean> ad = itemAdapter.getData().get(0).getAd();
                        ad.remove(i);
                        ad.add(i, createAd(bean));
                        itemAdapter.notifyItemChanged(0);
                    }
                });
    }

    private int absY = 0;

    //TODO
    private void initRv() {
        if (stackItemList == null) {
            stackItemList = new ArrayList<>();
        }

        stackRv.setAdapter(getAdapter());
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext(), FlexDirection.ROW, FlexWrap.WRAP);
        stackRv.setLayoutManager(flexboxLayoutManager);
        stackRv.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 2) {
                    itemAdapter.pause();
                } else {
                    itemAdapter.resume();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                absY += dy;
                //范围(0,200)
                setProcess();
            }

        });
        stackRv.setHasFixedSize(true);
    }

    public void setProcess() {
        float f = (absY + 0f) / 200f;
        if (mProcessListener != null) {
            mProcessListener.process(Math.min(Math.max(f, 0f), 1f), position);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("TAG", "onViewCreated");
        if (isFirst) {
            initChildView();
            getData();
            isFirst = false;
        }
    }

    private int getAdWidth() {
        int w = ScreenUtils.getDisplayMetrics().widthPixels;
        return ScreenUtils.pxToDp(w);
    }

    private void initChildView() {
        Bundle arguments = getArguments();
        id = arguments.getInt("id", 0);
        type = arguments.getInt("type", 0);
        position = arguments.getInt("position", 0);
        page = 0;
        stackRv = (CustomRecyclerView) findViewById(R.id.stack_rv);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);

        initRv();
        initRefreshLayout();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume");
    }

    private StackRoomChildItemAdapter getAdapter() {
        int width = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        if (itemAdapter == null) {
            itemAdapter = new StackRoomChildItemAdapter(stackItemList, width);
            itemAdapter.addChildClickViewIds(
                    R.id.image_more,
                    R.id.more
                    , R.id.likeIv0
                    , R.id.likeName0
                    , R.id.ivDelLike0
                    , R.id.likeIv1
                    , R.id.likeName1
                    , R.id.ivDelLike1
                    , R.id.likeIv2
                    , R.id.likeName2
                    , R.id.ivDelLike2);
            itemAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    if (position == 0) {
                        return;
                    }
                    StackRoomItemBean stackRoomItemBean = stackItemList.get(position);
                    int viewType = stackRoomItemBean.getViewType();
                    if (viewType != 0 && viewType != -1 && viewType != 8) {
                        BookDetailActivity.start(getContext(), stackRoomItemBean.getId());
                    }
                }
            });

            itemAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

                    if (view.getId() == R.id.image_more) {
                        HomeActivity.mHomeActivity.Gotofrag_Classification(0);
                        return;
                    }
                    if (view.getId() == R.id.more) {

                        BookMoreActivity.start(getContext(), itemAdapter.getData().get(position).getTitle(), itemAdapter.getData().get(position).getId() + "");
                        return;
                    }
                    if (view.getId() == R.id.likeIv0 || view.getId() == R.id.likeName0) {
                        StackRoomItemBean.AdBean ad = itemAdapter.getData().get(0).getAd().get(0);
                        BookDetailActivity.start(getContext(), ad.getId());
                        return;
                    }
                    if (view.getId() == R.id.likeIv1 || view.getId() == R.id.likeName1) {
                        StackRoomItemBean.AdBean ad = itemAdapter.getData().get(0).getAd().get(1);
                        BookDetailActivity.start(getContext(), ad.getId());
                        return;
                    }
                    if (view.getId() == R.id.likeIv2 || view.getId() == R.id.likeName2) {
                        StackRoomItemBean.AdBean ad = itemAdapter.getData().get(0).getAd().get(2);
                        BookDetailActivity.start(getContext(), ad.getId());
                        return;
                    }
                    if (view.getId() == R.id.ivDelLike0) {
                        delLike(0);
                    }
                    if (view.getId() == R.id.ivDelLike1) {
                        delLike(1);
                    }
                    if (view.getId() == R.id.ivDelLike2) {
                        delLike(2);
                    }
                }
            });
        }
        return itemAdapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initRefreshLayout() {
        refreshLayout.setNoMoreData(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                getData();
            }
        });
    }

    private void showData(StackRoomBookBean bean) {
        if (bean == null) return;
        try {
            if (stackItemList != null) {
                if (stackItemList != null && stackItemList.size() == 0) {
                    List<StackRoomBookBean.AdBean> ad = bean.getAd();
                    if (ad != null) {
                        StackRoomItemBean adBean = new StackRoomItemBean();
                        List<StackRoomItemBean.AdBean> adList = new ArrayList<>();
                        for (int i = 0; i < ad.size(); i++) {
//                            if (i > 2) {
//                                break;
//                            }
                            adList.add(createAd(ad.get(i)));
                        }
                        adBean.setAd(adList);
                        adBean.setViewType(type == 0 ? -2 : -3);
                        adBean.setLeftMargin(0);
                        adBean.setRightMargin(0);
                        stackItemList.add(adBean);
                    }
                }
            }
            List<StackRoomBookBean.ColumnBean> column = bean.getColumn();
            for (StackRoomBookBean.ColumnBean columnBean : column) {
                //样式为8则为图片筛选
                if (columnBean.getStyle() == 8) {
                    // 添加图片筛选

                    StackRoomItemBean stackRoomItemBean1 = new StackRoomItemBean();
                    stackRoomItemBean1.setTitle(columnBean.getTitle());
                    stackRoomItemBean1.setMore(columnBean.getMore() == 1);
                    stackRoomItemBean1.setId(columnBean.getId());
                    stackRoomItemBean1.setViewType(columnBean.getStyle());
                    //添加标题
                    stackRoomItemBean1.setLeftMargin(20);
                    stackRoomItemBean1.setRightMargin(20);
                    stackItemList.add(stackRoomItemBean1);

                }
                else {
                    if (columnBean.getType() == 0) {
                        StackRoomItemBean stackRoomItemBean = new StackRoomItemBean();
                        stackRoomItemBean.setTitle(columnBean.getTitle());
                        stackRoomItemBean.setMore(columnBean.getMore() == 1);
                        stackRoomItemBean.setId(columnBean.getId());
                        stackRoomItemBean.setViewType(0);
                        int style = columnBean.getStyle();
                        //添加标题
                        stackRoomItemBean.setLeftMargin(20);
                        stackRoomItemBean.setRightMargin(20);
                        stackItemList.add(stackRoomItemBean);
                        List<StackRoomBookBean.ColumnBean.ListBean> list = columnBean.getList();
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                StackRoomBookBean.ColumnBean.ListBean listBean = list.get(i);
                                StackRoomItemBean item = new StackRoomItemBean();
                                item.setId(listBean.getId());
                                item.setTitle(listBean.getTitle());
                                item.setCoverpic(listBean.getCoverpic());
                                item.setAllchapter(listBean.getAllchapter());
                                item.setAuthor(listBean.getAuthor());
                                item.setRemark(listBean.getRemark());
                                item.setDesc(listBean.getDesc());
                                item.setIswz(listBean.getIswz());
                                item.setHots(listBean.getHots());
                                item.setBtype(listBean.getBtype());
                                item.setAnid(listBean.getAnid());
                                item.setClassify(listBean.getClassify());
                                item.setAverage_score(listBean.getAverage_score());
                                item.setViewType(getType(style, i));
                                item.setIsvip(TextUtils.equals(listBean.getIsvip(), "1"));
                                setMargin1(item, style, i);
                                stackItemList.add(item);
                            }
                        }
                    } else if (columnBean.getType() == 1) {
                        StackRoomItemBean stackRoomItemBean = new StackRoomItemBean();
                        stackRoomItemBean.setIndex(count);
                        stackRoomItemBean.setViewType(-4);
                        stackRoomItemBean.setLeftMargin(0);
                        stackRoomItemBean.setRightMargin(0);
                        stackItemList.add(stackRoomItemBean);
                        count++;
                    }
                }

//                StackRoomItemBean bottomItem = new StackRoomItemBean();
//                bottomItem.setViewType(-1);
//                bottomItem.setLeftMargin(20);
//                bottomItem.setRightMargin(20);
//                //添加底部分割线
//                stackItemList.add(bottomItem);

            }

            Log.e(TAG, "count: " + count);
            itemAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    int count = 0;
    private StackRoomItemBean.AdBean createAd(StackRoomBookBean.AdBean adBean) {
        StackRoomItemBean.AdBean ad = new StackRoomItemBean.AdBean();
        ad.setId(adBean.getId());
        ad.setCoin(adBean.getCoin());
        ad.setBtype(adBean.getBtype());
        ad.setTitle(adBean.getTitle());
        ad.setAuthor(adBean.getAuthor());
        ad.setCoverpic(adBean.getCoverpic());
        ad.setInfopic(adBean.getInfopic());
        ad.setRemark(adBean.getRemark());
        ad.setDesc(adBean.getDesc());
        ad.setAreas(adBean.getAreas());
        ad.setCateids(adBean.getCateids());
        ad.setIswz(adBean.getIswz());
        ad.setIsfw(adBean.getIsfw());
        ad.setIsnew(adBean.getIsnew());
        ad.setIslong(adBean.getIslong());
        ad.setIssex(adBean.getIssex());
        ad.setAllchapter(adBean.getAllchapter());
        ad.setPaychapter(adBean.getPaychapter());
        ad.setSchapter(adBean.getSchapter());
        ad.setTchapter(adBean.getTchapter());
        ad.setHots(adBean.getHots());
        ad.setThermal(adBean.getThermal());
        ad.setComments(adBean.getComments());
        ad.setLikes(adBean.getLikes());
        ad.setList_order(adBean.getList_order());
        ad.setSharetitle(adBean.getSharetitle());
        ad.setSharedesc(adBean.getSharedesc());
        ad.setIsrecommend(adBean.getIsrecommend());
        ad.setStatus(adBean.getStatus());
        ad.setIshow(adBean.getIshow());
        ad.setPrize(adBean.getPrize());
        ad.setIsbg(adBean.getIsbg());
        ad.setSelectpic(adBean.getSelectpic());
        ad.setTag(adBean.getTag());
        ad.setCreated_at(adBean.getCreated_at());
        ad.setUpdated_at(adBean.getUpdated_at());
        ad.setImage(adBean.getImage());
        ad.setUrl(adBean.getUrl());
        ad.setAnid(adBean.getAnid());
        ad.setIsvip(TextUtils.equals(adBean.getIsvip(), "1"));
        return ad;
    }

    private void setMargin1(StackRoomItemBean item, int style, int i) {
        switch (style) {
            case 0:
                item.setLeftMargin(i % 2 == 0 ? 20 : 5);
                item.setRightMargin(i % 2 == 0 ? 5 : 20);
                break;
            case 1:
                if (i == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(20);
                } else if ((i - 1) % 4 == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(10);
                } else if ((i - 1) % 4 == 3) {
                    item.setLeftMargin(10);
                    item.setRightMargin(20);
                } else {
                    item.setLeftMargin(10);
                    item.setRightMargin(10);
                }
                break;
            // 3个图片一行
            case 2:
                if (i % 3 == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(5);
                } else if (i % 3 == 1) {
                    item.setLeftMargin(5);
                    item.setRightMargin(5);
                } else if (i % 3 == 2) {
                    item.setLeftMargin(5);
                    item.setRightMargin(20);
                }
                break;
            case 3:
            {
                item.setLeftMargin(20);
                item.setRightMargin(20);
            }
            break;
            // 4个图片一行
            case 4:
                if (i % 4 == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(5);
                } else if (1 % 4 == 3) {
                    item.setLeftMargin(5);
                    item.setRightMargin(20);
                } else {
                    item.setLeftMargin(5);
                    item.setRightMargin(5);
                }
                break;
            case 5:
                switch (i) {
                    case 0:
                        item.setLeftMargin(20);
                        item.setRightMargin(5);
                        break;
                    case 1:
                        item.setLeftMargin(5);
                        item.setRightMargin(5);
                        break;
                    case 2:
                        item.setLeftMargin(5);
                        item.setRightMargin(20);
                        break;
                    default:
                        item.setLeftMargin(20);
                        item.setRightMargin(20);
                }
                break;
            case 6:
                if (i == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(5);
                } else if (i == 1) {
                    item.setLeftMargin(5);
                    item.setRightMargin(20);
                } else if ((i - 2) % 4 == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(5);
                } else if ((i - 2) % 4 == 3) {
                    item.setLeftMargin(5);
                    item.setRightMargin(20);
                } else {
                    item.setLeftMargin(5);
                    item.setRightMargin(5);
                }
                break;
            case 7:
                switch (i % 5) {
                    case 0:
                        item.setLeftMargin(20);
                        item.setRightMargin(20);
                        break;
                    case 1:
                        item.setLeftMargin(20);
                        item.setRightMargin(10);
                        break;
                    case 4:
                        item.setLeftMargin(10);
                        item.setRightMargin(20);
                        break;
                    default:
                        item.setLeftMargin(10);
                        item.setRightMargin(10);
                }
                break;
            default:
                item.setLeftMargin(20);
                item.setRightMargin(20);
        }
    }

    private void setMargin(StackRoomItemBean item, int style, int i) {
        switch (style) {
            case 0:
                item.setLeftMargin(i % 2 == 0 ? 20 : 10);
                item.setRightMargin(i % 2 == 0 ? 10 : 20);
                break;
            case 1:
                if (i == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(20);
                } else if ((i - 1) % 4 == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(10);
                } else if ((i - 1) % 4 == 3) {
                    item.setLeftMargin(10);
                    item.setRightMargin(20);
                } else {
                    item.setLeftMargin(10);
                    item.setRightMargin(10);
                }
                break;
            case 2:
                if (i % 3 == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(10);
                } else if (i % 3 == 1) {
                    item.setLeftMargin(10);
                    item.setRightMargin(10);
                } else if (i % 3 == 2) {
                    item.setLeftMargin(10);
                    item.setRightMargin(20);
                }
                break;
            case 3:
            {
                item.setLeftMargin(20);
                item.setRightMargin(20);
            }
            break;
            case 4:
                if (i % 4 == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(10);
                } else if (1 % 4 == 3) {
                    item.setLeftMargin(10);
                    item.setRightMargin(20);
                } else {
                    item.setLeftMargin(10);
                    item.setRightMargin(10);
                }
                break;
            case 5:
                switch (i) {
                    case 0:
                        item.setLeftMargin(20);
                        item.setRightMargin(10);
                        break;
                    case 1:
                        item.setLeftMargin(10);
                        item.setRightMargin(10);
                        break;
                    case 2:
                        item.setLeftMargin(10);
                        item.setRightMargin(20);
                        break;
                    default:
                        item.setLeftMargin(20);
                        item.setRightMargin(20);
                }
                break;
            case 6:
                if (i == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(10);
                } else if (i == 1) {
                    item.setLeftMargin(10);
                    item.setRightMargin(20);
                } else if ((i - 2) % 4 == 0) {
                    item.setLeftMargin(20);
                    item.setRightMargin(10);
                } else if ((i - 2) % 4 == 3) {
                    item.setLeftMargin(10);
                    item.setRightMargin(20);
                } else {
                    item.setLeftMargin(10);
                    item.setRightMargin(10);
                }
                break;
            case 7:
                switch (i % 5) {
                    case 0:
                        item.setLeftMargin(20);
                        item.setRightMargin(20);
                        break;
                    case 1:
                        item.setLeftMargin(20);
                        item.setRightMargin(10);
                        break;
                    case 4:
                        item.setLeftMargin(10);
                        item.setRightMargin(20);
                        break;
                    default:
                        item.setLeftMargin(10);
                        item.setRightMargin(10);
                }
                break;
            default:
                item.setLeftMargin(20);
                item.setRightMargin(20);
        }
    }

    private int getType(int style, int i) {
        switch (style) {
            case 0:
                return 2;
            case 1:
                return i == 0 ? 1 : 4;
            case 2:
                return 3;
            case 3:
            {
                return 1;
            }
            case 4:
                return 4;
            case 5:
                return i <= 2 ? 3 : 1;
            case 6:
                return i <= 1 ? 2 : 4;
            case 7:
                return i % 5 == 0 ? 1 : 4;

            default:
                return 1;
        }
    }

    public void setScrollProcessListener(ScrollProcessListener processListener) {
        mProcessListener = processListener;
    }

    public interface ScrollProcessListener {
        void process(float f, int position);
    }
}
