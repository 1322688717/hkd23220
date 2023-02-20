package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomChannelBean;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.SearchShelveActivity;
import cc.ixcc.novelthree.widget.JsIndicator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.OnClick;

/**
 * 2020.8.7
 * 首页 书库 入口
 */
public class HomeStackRoomFragment extends MyFragment<HomeActivity> {
    public static String TAG = "HomeStackRoomFragment";
    ViewPager stackViewPager;
    MagicIndicator indicator;
    ImageView search;
    ConstraintLayout clBg;
    ImageView sign;

    private CommonNavigatorAdapter indicatorAdapter;
    private FragmentPagerAdapter vpAdapter;
    private ArrayList<String> mTitles;
    private ArrayList<Fragment> mFragments;
    private ArrayList<StackRoomChannelBean> stackRoomBeanList;
    private ArrayList<TextView> indicatorTvList;
    private JsIndicator mJsIndicator;

    private static HomeStackRoomFragment instance = null;
    public static HomeStackRoomFragment getInstance() {
        if(instance == null)
        {
            instance = new HomeStackRoomFragment();
        }
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_stack_room;
    }

    protected void initView() {

    }

    //TODO
    @Override
    protected void initData() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isFirst) {
            initStackView();
            getData();
            isFirst = false;
        }
    }

    private boolean isFirst = true;

    private void initStackView() {
        Log.i(TAG, "HomeStackRoomFragment: initView()");
        stackRoomBeanList = new ArrayList<>();
        stackViewPager = findViewById(R.id.stackViewPager);
        indicator = findViewById(R.id.indicator);
        search = findViewById(R.id.search);
        sign=findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              HomeActivity.OnGotoPage(2);
                HomeActivity.mHomeActivity.Gotofrag_weal();
            }
        });
        clBg = findViewById(R.id.clBg);
        initIndicator();
        initViewPager();
        ViewPagerHelper.bind(indicator, stackViewPager);
    }

    private void getData() {
        Log.i(TAG, "HomeStackRoomFragment: initData()");
        if (stackRoomBeanList != null && stackRoomBeanList.size() > 0) {
            return;
        }
        stackRoomBeanList.clear();
        HttpClient.getInstance().get(AllApi.bookchannel, AllApi.bookchannel)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        for (int i = 0; i < info.length; i++) {
                            StackRoomChannelBean resultBean = new Gson().fromJson(info[i], StackRoomChannelBean.class);
                            stackRoomBeanList.add(resultBean);
                            mTitles.add(resultBean.getTitle());
                            HomeStackRoomChildFragment childFragment = HomeStackRoomChildFragment.newInstance(resultBean.getId(), resultBean.getType(), i);
                            childFragment.setScrollProcessListener(new HomeStackRoomChildFragment.ScrollProcessListener() {
                                //滑动进度监听
                                @Override
                                public void process(float f, int position) {
                                    if (position != stackViewPager.getCurrentItem()) {
                                        return;
                                    }
                                    clBg.setBackgroundColor(Color.argb((int) (f * 255), 255, 255, 255));
//                                    if (f == 1f) {
//                                        for (TextView textView : indicatorTvList) {
//                                            if (textView != null) {
//                                                //textView.setTextColor(Color.BLACK);
//                                                textView.setTextColor(getColor(R.color.colorButtonPressed));
//
//                                            }
//                                        }


                                        if (mJsIndicator != null) {
                                            mJsIndicator.setColorAndInvalidate(getColor(R.color.colorButtonPressed));
                                        }

                                        //search.setImageResource(R.mipmap.search_icon2);
//                                    } else {
//                                        for (TextView textView : indicatorTvList) {
//                                            if (textView != null) {
//                                                textView.setTextColor(Color.WHITE);
//                                            }
//                                        }
//                                        if (mJsIndicator != null) {
//                                            mJsIndicator.setColorAndInvalidate(Color.WHITE);
//                                        }
//                                        search.setImageResource(R.mipmap.search_icon);
//                                    }
                                }
                            });
                            mFragments.add(childFragment);

                        }
                        showData();

                        for (int i=0;i<indicatorTvList.size();i++){

                            if (indicatorTvList.get(i) != null) {
                                //textView.setTextColor(Color.BLACK);
                                if (i==0){
                                    indicatorTvList.get(i).setTextColor(getColor(R.color.colorButtonPressed));
                                }else {
                                    indicatorTvList.get(i).setTextColor(getColor(R.color.maintitleSelect));
                                }
                            }
                        }
                    }
                });
    }

    //数据展示
    private void showData() {
        indicatorAdapter.notifyDataSetChanged();
        for (int i = 0; i < indicatorTvList.size(); i++) {
            int position = i;
            indicatorTvList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stackViewPager.setCurrentItem(position);
                }
            });
        }
        Log.e(TAG, "vpAdapter: " + vpAdapter.getCount());
        vpAdapter.notifyDataSetChanged();
        stackViewPager.setCurrentItem(0);
    }

    //初始化initIndicator
    private void initIndicator() {
        if (mTitles == null) {
            mTitles = new ArrayList<>();
        }
        if (indicatorTvList == null) {
            indicatorTvList = new ArrayList<>();
        }
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(getNavigatorAdapter());
        indicator.setBackgroundColor(Color.TRANSPARENT);
        indicator.setNavigator(commonNavigator);
    }

    //初始化initViewPager
    private void initViewPager() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        stackViewPager.setAdapter(getViewPagerAdapter());
        stackViewPager.setOffscreenPageLimit(2);
        //ViewPager动画完成后才走生命周期
//        setSliderTransformDuration(500);

        stackViewPager.addOnPageChangeListener(getPageChangeListener());
    }

    private ViewPager.OnPageChangeListener getPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("TAG", "选中:" + position);
                HomeStackRoomChildFragment fragment = (HomeStackRoomChildFragment) mFragments.get(position);
                fragment.setProcess();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private FragmentPagerAdapter getViewPagerAdapter() {
        if (vpAdapter == null) {
//            vpAdapter = new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            vpAdapter = new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                @NonNull
                @Override
                public Fragment getItem(int position) {
                    return mFragments.get(position);
                }

                @Override
                public int getCount() {
                    return mFragments.size();
                }
            };
        }
        return vpAdapter;
    }

    private CommonNavigatorAdapter getNavigatorAdapter() {
        if (indicatorAdapter == null) {
            indicatorAdapter = new CommonNavigatorAdapter() {
                @Override
                public int getCount() {
                    return mTitles.size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, int index) {
                    CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                    TextView textView = new TextView(context);
                    textView.setText(mTitles.get(index));
                    textView.setTextSize(18);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.WHITE);
                    int padding = UIUtil.dip2px(context, 10);
                    textView.setPadding(padding, 0, padding, 0);
                    textView.setSingleLine();
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    indicatorTvList.add(textView);
                    commonPagerTitleView.setContentView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                        @Override
                        public void onSelected(int index, int totalCount) {
                            textView.getPaint().setFakeBoldText(true);
                            textView.setTextColor(getColor(R.color.colorButtonPressed));
                            textView.invalidate();
                        }

                        @Override
                        public void onDeselected(int index, int totalCount) {
                            textView.getPaint().setFakeBoldText(false);
                            textView.setTextColor(getColor(R.color.maintitleSelect));
                            textView.invalidate();
                        }

                        @Override
                        public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                        }

                        @Override
                        public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                        }
                    });
                    return commonPagerTitleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    JsIndicator jsIndicator = new JsIndicator(context);
                    jsIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                    jsIndicator.setXOffset(UIUtil.dip2px(context, 15));
                    jsIndicator.setRoundRadius(UIUtil.dip2px(context, 2));
                    jsIndicator.setColors(Color.WHITE);
                    mJsIndicator = jsIndicator;
                    return jsIndicator;
                }
            };

        }
        return indicatorAdapter;
    }

    public void setSliderTransformDuration(int duration) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getActivity(), null, duration);
            mScroller.set(stackViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            this(context, interpolator);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }


    @OnClick(R.id.search)
    public void onViewClicked() {
        startActivity(SearchShelveActivity.class);
    }


}
