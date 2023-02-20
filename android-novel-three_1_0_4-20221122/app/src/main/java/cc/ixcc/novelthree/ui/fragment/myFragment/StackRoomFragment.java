package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomChannelBean;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.event.BlurImgEvent;
import cc.ixcc.novelthree.event.StackLikeEvent;
import cc.ixcc.novelthree.event.StackTabSelectEvent;
import cc.ixcc.novelthree.event.StackMoreEvent;
import cc.ixcc.novelthree.event.StackRefreshEvent;
import cc.ixcc.novelthree.helper.BindEventBus;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.SearchShelveActivity;
import cc.ixcc.novelthree.ui.adapter.MyPagerAdapter;
import cc.ixcc.novelthree.widget.CustomViewpager;
import cc.ixcc.novelthree.widget.GlideBlurformation;
import com.jiusen.base.util.DpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 书库Fragment
 */
@BindEventBus
public class StackRoomFragment extends MyFragment<HomeActivity> {
    @BindView(R.id.bg_top)
    ImageView topBackground;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;
    @BindView(R.id.vp)
    CustomViewpager vp;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.tablayout)
    XTabLayout mTabLayout;
    @BindView(R.id.alllayout)
    RelativeLayout alllayout;
    @BindView(R.id.rl_top)
    LinearLayout rlTop;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private List<String> mtitle = new ArrayList<>();
    boolean isLike;
    private Drawable mdrable;
    private int mm;
    String lasturl = "-1";
    List<StackRoomChannelBean> channelBeanList = new ArrayList<>();
    HashMap<Integer, Integer> scrollYMap = new HashMap<>();
    HashMap<Integer, Fragment> fragmentMap = new HashMap<>();
    int ap = 0;
    boolean isChangeTab = true;

    public static StackRoomFragment newInstance() {
        return new StackRoomFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stack;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            //防止白页
            view = inflater.inflate(R.layout.fragment_stack, container, false);
            initView();
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
//        EventBus.getDefault().unregister(this);
//        EventBus.getDefault().register(this);
        ImmersionBar.setTitleBar(this, rlTop);
        //TODO
//        getChannel();
        initChannel();
        mm = R.drawable.t1;
        mdrable = getResources().getDrawable(mm);
        rlTop.setBackground(mdrable);
//        mdrable.setAlpha(0);
        mdrable.mutate().setAlpha(0); //设置mutate()，可防止其他页面变透明
         
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int scrollRange = DpUtil.dip2px(getContext(), 65);
                    scrollY = scrollY <= 0 ? 0 : scrollY;
                    scrollY = scrollY >= scrollRange ? scrollRange : scrollY;
                    double dd = div(scrollY, scrollRange);
                    ap = (int) (dd * 255);
                    mdrable.mutate().setAlpha(ap);
//                    scrollYMap.put(mTabLayout.getSelectedTabPosition(), scrollview.getScrollY());
                    setTopExpand(ap < 220, isLike);
                    setAndroidNativeLightStatusBar(ap < 220);
                }
            });
        }


//        scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                refresh.setEnabled(scrollview.getScrollY() == 0);
//            }
//        });
        initRefresh();
    }

    //在此进行获取数据操作
    @Override
    protected void initData() {
        getChannel();

    }

    private void initRefresh() {
        refresh.setEnableFooterTranslationContent(false); //拖动Footer的时候是否同时拖动内容（默认true）
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                EventBus.getDefault().post(new StackMoreEvent(mTabLayout.getSelectedTabPosition()));
                refresh.finishLoadMore(500);
            }
        });
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                EventBus.getDefault().post(new StackRefreshEvent(mTabLayout.getSelectedTabPosition()));
                refresh.finishRefresh(500);
//                getChannel();
                for (int i = 0; i < mtitle.size(); i++) {
                    scrollYMap.put(i, 0);
                }
            }
        });
    }

    private void getChannel() {
        channelBeanList.clear();
        HttpClient.getInstance().get(AllApi.bookchannel, AllApi.bookchannel)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        for (int i = 0; i < info.length; i++) {
                            StackRoomChannelBean resultBean = new Gson().fromJson(info[i], StackRoomChannelBean.class);
                            channelBeanList.add(resultBean);
                        }
                        showData();
                    }
                });
    }

    private void showData() {
        for (int i = 0; i < channelBeanList.size(); i++) {
            mtitle.add(channelBeanList.get(i).getTitle());
        }

        for (int i = 0; i < mtitle.size(); i++) {
            fragments.add(StackRoomOtherFragment.newInstance(vp, channelBeanList.get(i).getId() + "", i + "", channelBeanList.get(i).getType() == 0));
            scrollYMap.put(i, 0);
            if (i == 0) {
                showTopLike(channelBeanList.get(i).getType() == 0); //刷新第一页背景
            }
        }
        if (myPagerAdapter != null) {
            myPagerAdapter.notifyDataSetChanged();
        }
    }

    List<Fragment> fragments;
    MyPagerAdapter myPagerAdapter;

    private void initChannel() {
        fragments = new ArrayList<>();
        mtitle.clear();
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), getContext(), fragments, mtitle);
        initViewPagerListener();
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(mtitle.size());
        mTabLayout.setupWithViewPager(vp);//此方法就是让tablayout和ViewPager联动
//        mTabLayout.setxTabDisplayNum(mtitle.size());
        mTabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                isChangeTab = true;
                EventBus.getDefault().post(new StackTabSelectEvent(mTabLayout.getSelectedTabPosition() + "", mdrable.getAlpha() < 220));
//                setFragmentData(mTabLayout.getSelectedTabPosition());
                scrollview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        scrollview.scrollTo(0, scrollYMap.get(tab.getPosition()));

                    }
                }, 10);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {
//                scrollYMap.put(tab.getPosition(), scrollview.getScrollY());
            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
    }

    private void initViewPagerListener() {
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vp.resetHeight(position);//每次切换页面，都重新重置高度
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.resetHeight(0);
    }

    private static final int DEF_DIV_SCALE = 10;

    public Double div(int v1, int v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLikeEvent(StackLikeEvent e) {
        showTopLike(e.isLike());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBlurEvent(BlurImgEvent e) {
//        if (!e.getTag().equals(String.valueOf(mTabLayout.getSelectedTabPosition()))) return;
        if (lasturl != null && lasturl.equals(e.getUrl())) return;
        if (e.isLike() == isLike) {
            Glide.with(getActivity())
                    .load(e.getUrl())
                    .placeholder(0)
                    .apply(RequestOptions.bitmapTransform(new GlideBlurformation(getActivity())))
                    .into(topBackground);
            lasturl = e.getUrl();
        }
    }

    private void showTopLike(boolean islike) {
        isLike = islike;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBackground.getLayoutParams(); //获取当前控件的布局对象
        params.height = islike ? DpUtil.dip2px(getContext(), 340) : DpUtil.dip2px(getContext(), 183);//设置当前控件布局的高度
        topBackground.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isVisibleToUser) return;
        alllayout.setFocusable(true);//获取焦点,防止scrollview自动滚动
        alllayout.setFocusableInTouchMode(true);//触摸是否能获取到焦点
        alllayout.requestFocus();//用于指定屏幕中的焦点View
        EventBus.getDefault().post(new StackTabSelectEvent(mTabLayout.getSelectedTabPosition() + "", mdrable.getAlpha() < 220));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAndroidNativeLightStatusBar(ap < 220);
            }
        }, 10);
    }

    boolean isExpand = true;

    private void setTopExpand(boolean isExpand, boolean islike) {
        if (!isChangeTab && (this.isExpand == isExpand)) return;
        this.isExpand = isExpand;
        isChangeTab = false;
//        if (islike == isLike) {
        search.setImageResource(isExpand ? R.mipmap.search_icon : R.mipmap.search_icon2);
        mTabLayout.setTabTextColors(Color.parseColor(isExpand ? "#FFFFFF" : "#BFC2CC"), Color.parseColor(isExpand ? "#FFFFFF" : "#0A1C33"));
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor(isExpand ? "#FFFFFF" : "#4D77FD"));
//        }
    }

    private void setAndroidNativeLightStatusBar(boolean light) {
        ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(!light)
                // 解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardEnable(true).init();
    }

    boolean isVisibleToUser;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            alllayout.setFocusable(true);//获取焦点,防止scrollview自动滚动
            alllayout.setFocusableInTouchMode(true);//触摸是否能获取到焦点
            alllayout.requestFocus();//用于指定屏幕中的焦点View
//            for (int i = 0; i < fragments.size(); i++) {
//                ((StackRoomOtherFragment) (fragments.get(i))).setParentVisible(isVisibleToUser);
//            }
//            setFragmentData(mTabLayout.getSelectedTabPosition());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setAndroidNativeLightStatusBar(ap < 220);
                }
            }, 100);
        } else {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setFragmentData(int position) {
        if (fragmentMap.get(position) == null) {
            StackRoomOtherFragment fragment = (StackRoomOtherFragment) (fragments.get(position));
            fragment.setParentVisible(true);
            fragmentMap.put(position, fragment);
        }
    }

    @OnClick({R.id.search, R.id.rl_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search:
                startActivity(SearchShelveActivity.class);
                break;
            case R.id.rl_top:
                break;
        }
    }
}
