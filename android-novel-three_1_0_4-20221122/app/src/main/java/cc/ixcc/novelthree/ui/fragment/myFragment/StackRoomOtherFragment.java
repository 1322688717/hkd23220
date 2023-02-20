package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
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
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomLikeAdapter;
import cc.ixcc.novelthree.widget.CustomViewpager;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomAdHolder1;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomAdHolder2;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder1;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder2;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder3;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder4;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder5;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder6;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder7;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder8;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder9;
import cc.ixcc.novelthree.widget.viewhoder.StackRoomModeHolder10;
import com.jiusen.base.BaseAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;
import static cc.ixcc.novelthree.Constants.STACK_AD;

/**
 * 书库子viewPage
 */
/**
 * 书库子viewPage
 */
@BindEventBus
public class StackRoomOtherFragment extends MyFragment {
    @BindView(R.id.alllayout)
    LinearLayout alllayout;
    //顶部轮播图
    List<StackRoomBookBean.AdBean> bannerList = new ArrayList<>();
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.ll_like)
    LinearLayout mLLLike;
    @BindView(R.id.rv_like)
    RecyclerView mLikeRv;
    boolean isLike;
    StackRoomLikeAdapter mLikeAdapter;
    String mTag;
    String mTabTag = "";
    boolean light = true; //状态栏文字颜色
    boolean isParentVisible;
    boolean isVisible;
    int lastPosition;
    StackRoomModeHolder1 mModeHolder1;
    StackRoomModeHolder2 mModeHolder2;
    StackRoomModeHolder3 mModeHolder3;
    StackRoomModeHolder4 mModeHolder4;
    StackRoomModeHolder5 mModeHolder5;
    StackRoomModeHolder6 mModeHolder6;
    StackRoomModeHolder7 mModeHolder7;
    StackRoomModeHolder8 mModeHolder8;
    StackRoomModeHolder9 mModeHolder9;
    StackRoomModeHolder10 mModeHolder10;
    StackRoomAdHolder1 mStackRoomAd1;
    StackRoomAdHolder2 mStackRoomAd2;
    int page = 1;
    String id;
    List<StackRoomBookBean.AdBean> mSubLikeList;
    List<StackRoomBookBean.AdBean> mLikeList;
    @BindView(R.id.layout)
    ViewGroup layout;
    static CustomViewpager mViewPager;

    public static StackRoomOtherFragment newInstance(CustomViewpager viewpager, String id, String tag, boolean isLike) {
        Bundle args = new Bundle();
        args.putBoolean("islike", isLike);
        args.putString("id", id);
        args.putString("tag", tag);
        StackRoomOtherFragment fragment = new StackRoomOtherFragment();
        fragment.setArguments(args);
        mViewPager = viewpager;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stack_other;
    }

    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
//        if (isParentVisible && isVisible) initView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            //防止白页
            view = inflater.inflate(R.layout.fragment_stack_other, container, false);
        }
        mTag = getArguments().getString("tag");
        view.setTag(mTag);
        if (mViewPager != null)
            mViewPager.setObjectForPosition(view, Integer.parseInt(mTag));//添加这句（1代表的是该Fragment对象在ViewPager的位置）
        return view;
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void updateCopyright(String envent) {
//        Log.e("EventBus", "BookotherFragment--Copyright接受数据" + envent);
//        if (envent.equals("-1")) {
//            banner.setVisibility(View.VISIBLE);
//
//        } else {
//            banner.setVisibility(View.GONE);
//        }
//    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initBanner() {
        banner.setOutlineProvider(new ViewOutlineProvider() { //要在加载图片之前设置这个方法
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 10); //设置圆角
            }
        });
        banner.setClipToOutline(true);
//        bannerList = new ArrayList<>();
//        bannerList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592046681528&di=d0c12fe9079fdbf11417ab79810e3f85&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F9fo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2Fe824b899a9014c080cbfa8900d7b02087bf4f47b.jpg");
//        bannerList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592046575760&di=9ffb9391dc795fa0b563fbf1370b920d&imgtype=0&src=http%3A%2F%2Fimg.improve-yourmemory.com%2Fpic%2F54a392a139a5de0690c3f30871b24c20-5.jpg");
//        bannerList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1592046513622&di=dbd337361252ce856ce3d7a50d85be03&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2Fw1020h616%2F20180208%2F203b-fyrkuxs3382313.jpg");
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(bannerList);
        //设置轮播时间
        banner.setDelayTime(3000);

        setBlurImg(0, false);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position > bannerList.size()) position = 1;
                setBlurImg(position - 1, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int p) {
                if (bannerList != null) {
                    if (p >= 0 && p < bannerList.size()) {
                        StackRoomBookBean.AdBean bean = bannerList.get(p);
                        if (bean != null) {
                            BookDetailActivity.start(getContext(), bean.getAnid());
//                            String link = bean.getUrl();
//                            if (!TextUtils.isEmpty(link)) {
//                                WebViewActivity.forward(AppContext.sInstance, link, "");
//                            }
                        }
                    }
                }
            }
        });

        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStackMoreEvent(StackMoreEvent e) {
        if (String.valueOf(e.getPosition()).equals(mTag)) {
            ++page;
            getInfo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStackRefreshEvent(StackRefreshEvent e) {
        if (String.valueOf(e.getPosition()).equals(mTag)) {
            page = 1;
            getInfo();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void initView() {
//        if (!isParentVisible || !isVisible) return;
//        if (!isParentVisible) return;
//        EventBus.getDefault().unregister(this);
//        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isLike = arguments.getBoolean("islike");
            id = arguments.getString("id");
            mTag = arguments.getString("tag");
        }
        if (!isLike) {
            initBanner();
        } else {
            initLikeRv();
        }
        getInfo();
        showTop(false);

//        if (isParentVisible && isVisible)
//            setAndroidNativeLightStatusBar(light);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setParentVisible(boolean isVisible) {
        isParentVisible = isVisible;
        initView();
    }

    boolean isFirst = true;

    private void getInfo() {
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
                        setLikeView(bean);
                        addView(bean);
                    }

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setLikeView(StackRoomBookBean bean) {
        mLikeList = bean.getAd();
        if (isLike) {
            if (mLikeList.size() > 2) {
                mSubLikeList = mLikeList.subList(0, 3);
            } else {
                mSubLikeList = mLikeList;
            }
            mLikeAdapter.setData(mSubLikeList);
            mLikeAdapter.notifyDataSetChanged();
            setBlurImg(1, true);
        } else {
            bannerList.clear();
            for (int i = 0; i < mLikeList.size(); i++) {
                bannerList.add(mLikeList.get(i));
            }
            initBanner();
        }
    }

    private void addView(StackRoomBookBean bookbean) {
        if (page == 1)
            layout.removeAllViews();
        if (bookbean == null) return;
        for (int i = 0; i < bookbean.getColumn().size(); i++) {
            StackRoomBookBean.ColumnBean bean = bookbean.getColumn().get(i);
            if (bean.getType() == STACK_AD) {
                mStackRoomAd1 = new StackRoomAdHolder1(AppContext.sInstance, bean, layout);
                mStackRoomAd1.addToParent();
                switch (bean.getStyle()) {
                    case 10: //广告1
                        mStackRoomAd1 = new StackRoomAdHolder1(getContext(), bean, (ViewGroup) findViewById(R.id.layout));
                        mStackRoomAd1.addToParent();
                        break;
                    case 11://广告2
                        mStackRoomAd2 = new StackRoomAdHolder2(getContext(), bean, (ViewGroup) findViewById(R.id.layout));
                        mStackRoomAd2.addToParent();
                        break;
                }
            } else {
                switch (bean.getStyle()) {
                    case 0:
                        mModeHolder1 = new StackRoomModeHolder1(AppContext.sInstance, bean, layout);
                        mModeHolder1.addToParent();
                        break;
                    case 1:
                        mModeHolder2 = new StackRoomModeHolder2(AppContext.sInstance, bean, layout);
                        mModeHolder2.addToParent();
                        break;
                    case 2:
                        mModeHolder3 = new StackRoomModeHolder3(AppContext.sInstance, bean, layout);
                        mModeHolder3.addToParent();
                        break;
                    case 3:
                        mModeHolder4 = new StackRoomModeHolder4(AppContext.sInstance, bean, layout);
                        mModeHolder4.addToParent();
                        break;
                    case 4:
                        mModeHolder5 = new StackRoomModeHolder5(AppContext.sInstance, bean, layout);
                        mModeHolder5.addToParent();
                        break;
                    case 5:
                        mModeHolder6 = new StackRoomModeHolder6(AppContext.sInstance, bean, layout);
                        mModeHolder6.addToParent();
                        break;
                    case 6:
                        mModeHolder7 = new StackRoomModeHolder7(AppContext.sInstance, bean, layout);
                        mModeHolder7.addToParent();
                        break;
                    case 7:
                        StackRoomModeHolder8 mModeHolder8 = new StackRoomModeHolder8(AppContext.sInstance, bean, layout);
                        mModeHolder8.addToParent();
                        break;
                    case 8:
                        mModeHolder9 = new StackRoomModeHolder9(AppContext.sInstance, bean, layout);
                        mModeHolder9.addToParent();
                        break;
                    case 10:
                        mModeHolder10 = new StackRoomModeHolder10(AppContext.sInstance, bean, layout);
                        mModeHolder10.addToParent();
                        break;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StackTabSelectEvent e) {
        if (e.getTag().equals(mTag)) {
            mTabTag = mTag;
            showTop(true);
            isVisible = true;
            setBlurImg(isLike ? 1 : lastPosition, isLike);
            light = e.isLight();
//        initView();
//            setAndroidNativeLightStatusBar(light);
        }
    }

    private void setAndroidNativeLightStatusBar(boolean light) {
        ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(!light)
                // 解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardEnable(true).init();
    }

    public void showTop(boolean isPost) {
        if (isPost)
            EventBus.getDefault().post(new StackLikeEvent(isLike));
        mLLLike.setVisibility(isLike ? View.VISIBLE : View.GONE);
        banner.setVisibility(isLike ? View.GONE : View.VISIBLE);
    }

    private void setBlurImg(int position, boolean isLike) {
//        if (!isVisible) return;
        if (!isLike) {
            if (bannerList == null) return;
            if (position >= 0 && position < bannerList.size()) {
                EventBus.getDefault().post(new BlurImgEvent(bannerList.get(position).getImage(), mTag, isLike));
                lastPosition = position;
            }
        } else {
            if (mSubLikeList == null) return;
            if (position >= 0 && position < mSubLikeList.size()) {
                EventBus.getDefault().post(new BlurImgEvent(mSubLikeList.get(position).getCoverpic(), mTag, isLike));
            }
        }
    }

    private void removelike(int position, StackRoomBookBean.AdBean bean) {
        HttpClient.getInstance().post(AllApi.removelike, AllApi.removelike)
                .params("id", bean.getId())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        StackRoomBookBean.AdBean bean = new Gson().fromJson(info[0], StackRoomBookBean.AdBean.class);
//                        mLikeList.remove(position);
                        mLikeList.set(position, bean);
                        if (mLikeList.size() > 2) {
                            mSubLikeList = mLikeList.subList(0, 3);
                        } else {
                            mSubLikeList = mLikeList;
                        }
                        mLikeAdapter.setData(mSubLikeList);
                        setBlurImg(1, true);
                    }
                });
    }

    private void initLikeRv() {
        if (mLikeAdapter == null) {
            mLikeAdapter = new StackRoomLikeAdapter(AppContext.sInstance);
            mLikeRv.setLayoutManager(new GridLayoutManager(AppContext.sInstance, 3, GridLayoutManager.VERTICAL, false));
            mLikeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                    BookDetailActivity.start(getContext(), mSubLikeList.get(position).getId());
                }
            });
            mLikeAdapter.setOnClickListener(new StackRoomLikeAdapter.OnClickListener() {
                @Override
                public void delete(StackRoomBookBean.AdBean bean, int position) {
                    removelike(position, bean);
                }
            });
            mLikeRv.setNestedScrollingEnabled(false); //禁止滑动
            mLikeRv.setHasFixedSize(true);
            mLikeRv.setAdapter(mLikeAdapter);
        } else {
            mLikeAdapter.setData(mSubLikeList);
//            mLikeAdapter.notifyDataSetChanged();
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //设置图片圆角角度
            RoundedCorners roundedCorners = new RoundedCorners(6);
            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
            //Glide.with(mContext).load(list.get(position).getThumb()).apply(options).into(holder.shopImg);
//            Glide.with(context).load((String) path).apply(options).into(imageView);
//            Glide.with(context).load(((StackRoomBookBean.AdBean) path).getImage()).apply(options).into(imageView);
            Glide.with(context).load(((StackRoomBookBean.AdBean) path).getImage()).into(imageView);
        }
    }

    @Override
    protected void initData() {
    }
}
