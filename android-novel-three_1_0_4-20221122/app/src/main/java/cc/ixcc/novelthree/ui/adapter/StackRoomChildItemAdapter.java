package cc.ixcc.novelthree.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayoutManager;
import cc.ixcc.novelthree.Constants;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomItemBean;
import cc.ixcc.novelthree.custom.GlideImageLoader;
import cc.ixcc.novelthree.statistics.AdjustUtil;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.vip.OpenVipActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomBookAdapter;
import cc.ixcc.novelthree.utils.NumberUtils;
import cc.ixcc.novelthree.widget.ArcImageView;

import com.jiusen.base.BaseAdapter;
import com.jiusen.base.util.DpUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class StackRoomChildItemAdapter extends BaseMultiItemQuickAdapter<StackRoomItemBean, BaseViewHolder> {
    int width;

    public StackRoomChildItemAdapter(List<StackRoomItemBean> data, int w) {
        this(data);
        width = w;
    }

    public StackRoomChildItemAdapter(List<StackRoomItemBean> data)
    {
        super(data);
        // 绑定 layout 对应的 type
        addItemType(0, R.layout.item_stack_title);
        addItemType(1, R.layout.item_stack_week);
        addItemType(2, R.layout.item_stack_hot);
        addItemType(3, R.layout.item_stack_book_must);
        addItemType(4, R.layout.item_stack_book_week);
        addItemType(-1, R.layout.item_stack_bottom);
        addItemType(-2, R.layout.layout_head_like);
        addItemType(-3, R.layout.layout_head_banner);
        addItemType(-4, R.layout.layout_stack_ad);
        addItemType(-5,R.layout.library_head_banner);
        //图片筛选
        addItemType(8, R.layout.item_stack_image);
        //标签筛选
        addItemType(9, R.layout.item_stack_title_new);

        //标签筛选
        addItemType(10, R.layout.view_stack_mode10);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, StackRoomItemBean stackRoomItemBean) {
        Log.i("stackRoomItemBean", "stackRoomItemBean" + stackRoomItemBean.getViewType());
        FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) baseViewHolder.itemView.getLayoutParams();

        layoutParams.leftMargin = DpUtil.dip2px(getContext(), stackRoomItemBean.getLeftMargin());
        layoutParams.rightMargin = DpUtil.dip2px(getContext(), stackRoomItemBean.getRightMargin() - 0.1f);
        switch (baseViewHolder.getItemViewType()) {
            case 0:
                baseViewHolder.setText(R.id.title, stackRoomItemBean.getTitle());
                baseViewHolder.setVisible(R.id.more, stackRoomItemBean.isMore());
                layoutParams.setWidth(FlexboxLayoutManager.LayoutParams.MATCH_PARENT);
                layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);
                break;
            case 1:
                Glide.with(getContext()).asBitmap().load(stackRoomItemBean.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((RoundedImageView) baseViewHolder.getView(R.id.img));
                baseViewHolder.setText(R.id.name, stackRoomItemBean.getTitle());
                baseViewHolder.setText(R.id.content, stackRoomItemBean.getDesc());
                baseViewHolder.setText(R.id.author, stackRoomItemBean.getAuthor());

                baseViewHolder.setVisible(R.id.hot, stackRoomItemBean.getHots() != 0);
                baseViewHolder.setText(R.id.hot, NumberUtils.amountConversion(stackRoomItemBean.getHots()));

                //baseViewHolder.setVisible(R.id.tv_type, !TextUtils.isEmpty(stackRoomItemBean.getClassify().name));
                //baseViewHolder.setText(R.id.tv_type, stackRoomItemBean.getClassify().name);
                baseViewHolder.setVisible(R.id.tvVip, stackRoomItemBean.isIsvip());
                baseViewHolder.setVisible(R.id.fen, stackRoomItemBean.getAverage_score() > 0);
                baseViewHolder.setVisible(R.id.pingfen, stackRoomItemBean.getAverage_score() > 0);
                baseViewHolder.setText(R.id.pingfen, stackRoomItemBean.getAverage_score() + "");
                baseViewHolder.setText(R.id.tv_count, stackRoomItemBean.getIswz() == Constants.lianzai ? "serialize" : "over");
                layoutParams.setWidth(FlexboxLayoutManager.LayoutParams.MATCH_PARENT);
                layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);
                break;
            case 2:
                Glide.with(getContext()).asBitmap().load(stackRoomItemBean.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((RoundedImageView) baseViewHolder.getView(R.id.book_img));
                baseViewHolder.setText(R.id.name, stackRoomItemBean.getTitle());

//                baseViewHolder.setVisible(R.id.author, !TextUtils.isEmpty(stackRoomItemBean.getAuthor()));
//                baseViewHolder.setText(R.id.author, stackRoomItemBean.getAuthor());

                baseViewHolder.setVisible(R.id.type,( !TextUtils.isEmpty(stackRoomItemBean.getClassify().name)&&!TextUtils.equals(stackRoomItemBean.getClassify().name,"1")));
                baseViewHolder.setText(R.id.type, stackRoomItemBean.getClassify().name);

                baseViewHolder.setVisible(R.id.hot, stackRoomItemBean.getHots() != 0);
                baseViewHolder.setText(R.id.hot, NumberUtils.amountConversion(stackRoomItemBean.getHots()));

                if (stackRoomItemBean.isIsvip()) {
                    baseViewHolder.setVisible(R.id.tvVip, true);
                    baseViewHolder.setVisible(R.id.ll_pingfen, false);
                    baseViewHolder.setVisible(R.id.fen, false);
                    baseViewHolder.setVisible(R.id.pingfen, false);
                } else {
                    baseViewHolder.setVisible(R.id.tvVip, false);
                    baseViewHolder.setVisible(R.id.ll_pingfen, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setVisible(R.id.fen, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setVisible(R.id.pingfen, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setText(R.id.pingfen, stackRoomItemBean.getAverage_score() + "");
                }

                //60
                layoutParams.setWidth((width - DpUtil.dip2px(getContext(), 50)) / 2);
                layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);
                break;
            case 3:
                Glide.with(getContext()).asBitmap().load(stackRoomItemBean.getCoverpic())
                        .placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((RoundedImageView) baseViewHolder.getView(R.id.img));
                baseViewHolder.setText(R.id.name, stackRoomItemBean.getTitle());
                if (stackRoomItemBean.isIsvip()) {
                    baseViewHolder.setVisible(R.id.tvVip, true);
                    baseViewHolder.setVisible(R.id.fen, false);
                    baseViewHolder.setVisible(R.id.pingfen, false);
                    baseViewHolder.setVisible(R.id.pingfenbg, false);
                } else {
                    baseViewHolder.setVisible(R.id.tvVip, false);
                    baseViewHolder.setVisible(R.id.fen, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setVisible(R.id.pingfen, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setVisible(R.id.pingfenbg, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setText(R.id.pingfen, stackRoomItemBean.getAverage_score() + "");
                }
                //80
                layoutParams.setWidth((width - DpUtil.dip2px(getContext(), 60)) / 3);
                layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);
                break;
            case 4:
                Glide.with(getContext()).asBitmap().load(stackRoomItemBean.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((RoundedImageView) baseViewHolder.getView(R.id.img));
                baseViewHolder.setText(R.id.name, stackRoomItemBean.getTitle());
                if (stackRoomItemBean.isIsvip()) {
                    baseViewHolder.setVisible(R.id.tvVip, true);
                    baseViewHolder.setVisible(R.id.fen, false);
                    baseViewHolder.setVisible(R.id.pingfen, false);
                    baseViewHolder.setVisible(R.id.pingfenbg, false);
                } else {
                    baseViewHolder.setVisible(R.id.tvVip, false);
                    baseViewHolder.setVisible(R.id.fen, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setVisible(R.id.pingfen, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setVisible(R.id.pingfenbg, stackRoomItemBean.getAverage_score() > 0);
                    baseViewHolder.setText(R.id.pingfen, stackRoomItemBean.getAverage_score() + "");
                }
                //baseViewHolder.setVisible(R.id.fen, stackRoomItemBean.getAverage_score() > 0);
                //baseViewHolder.setVisible(R.id.pingfen, stackRoomItemBean.getAverage_score() > 0);
                //baseViewHolder.setVisible(R.id.pingfenbg, stackRoomItemBean.getAverage_score() > 0);
                //100
                baseViewHolder.setText(R.id.pingfen, stackRoomItemBean.getAverage_score() + "");
                layoutParams.setWidth((width - DpUtil.dip2px(getContext(), 70)) / 4);
                layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);
                break;
            case -2:
                try {
                    List<StackRoomItemBean.AdBean> ad = stackRoomItemBean.getAd();
                    //View view = baseViewHolder.getView(R.id.viewPoint);
                    if (mViewPointListener != null) {
                        mViewPointListener.onPointCreate(baseViewHolder.itemView);
                    }
                    baseViewHolder.setVisible(R.id.ivDelLike0, ad.size() > 0);
                    baseViewHolder.setVisible(R.id.ivDelLike1, ad.size() > 1);
                    baseViewHolder.setVisible(R.id.ivDelLike2, ad.size() > 2);
                    baseViewHolder.setVisible(R.id.likeIv0, ad.size() > 0);
                    baseViewHolder.setVisible(R.id.likeIv1, ad.size() > 1);
                    baseViewHolder.setVisible(R.id.likeIv2, ad.size() > 2);
                    baseViewHolder.setVisible(R.id.likeName0, ad.size() > 0);
                    baseViewHolder.setVisible(R.id.likeName1, ad.size() > 1);
                    baseViewHolder.setVisible(R.id.likeName2, ad.size() > 2);
                    baseViewHolder.setVisible(R.id.tvVip0, ad.size() > 0);
                    baseViewHolder.setVisible(R.id.tvVip1, ad.size() > 1);
                    baseViewHolder.setVisible(R.id.tvVip2, ad.size() > 2);
                    MultiTransformation<Bitmap> multi = new MultiTransformation<Bitmap>(new BlurTransformation(20));

/*                    Glide.with(getContext()).asBitmap().load(ad.size() >= 2 ? ad.get(1).getCoverpic() : ad.get(0).getCoverpic()).
                            diskCacheStrategy(DiskCacheStrategy.ALL).
                            apply(new RequestOptions().
                                    bitmapTransform(multi)
                            ).into((ImageView) baseViewHolder.getView(R.id.ivLikeCover));*/

                    if (ad.size() == 1) {
                        StackRoomItemBean.AdBean adBean0 = ad.get(0);
                        baseViewHolder.setText(R.id.likeName0, adBean0.getTitle());
                        Glide.with(getContext()).asBitmap().load(adBean0.getCoverpic()).
                                placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((ArcImageView) baseViewHolder.getView(R.id.likeIv0));
                        baseViewHolder.setVisible(R.id.tvVip0, adBean0.isIsvip());
                        baseViewHolder.setVisible(R.id.tvVip1, false);
                        baseViewHolder.setVisible(R.id.tvVip2, false);
                    } else if (ad.size() == 2) {
                        StackRoomItemBean.AdBean adBean0 = ad.get(0);
                        StackRoomItemBean.AdBean adBean1 = ad.get(1);
                        baseViewHolder.setText(R.id.likeName0, adBean0.getTitle());
                        Glide.with(getContext()).asBitmap().load(adBean0.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) baseViewHolder.getView(R.id.likeIv0));
                        baseViewHolder.setText(R.id.likeName1, adBean1.getTitle());
                        Glide.with(getContext()).asBitmap().load(adBean1.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) baseViewHolder.getView(R.id.likeIv1));
                        baseViewHolder.setVisible(R.id.tvVip0, adBean0.isIsvip());
                        baseViewHolder.setVisible(R.id.tvVip1, adBean1.isIsvip());
                        baseViewHolder.setVisible(R.id.tvVip2, false);
                    } else if (ad.size() == 3) {
                        StackRoomItemBean.AdBean adBean0 = ad.get(0);
                        StackRoomItemBean.AdBean adBean1 = ad.get(1);
                        StackRoomItemBean.AdBean adBean2 = ad.get(2);
                        baseViewHolder.setText(R.id.likeName0, adBean0.getTitle());
                        Glide.with(getContext()).asBitmap().load(adBean0.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) baseViewHolder.getView(R.id.likeIv0));
                        baseViewHolder.setText(R.id.likeName1, adBean1.getTitle());
                        Glide.with(getContext()).asBitmap().load(adBean1.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) baseViewHolder.getView(R.id.likeIv1));
                        baseViewHolder.setText(R.id.likeName2, adBean2.getTitle());
                        Glide.with(getContext()).asBitmap().load(adBean2.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) baseViewHolder.getView(R.id.likeIv2));
                        baseViewHolder.setVisible(R.id.tvVip0, adBean0.isIsvip());
                        baseViewHolder.setVisible(R.id.tvVip1, adBean1.isIsvip());
                        baseViewHolder.setVisible(R.id.tvVip2, adBean2.isIsvip());
                    }
                    layoutParams.setWidth(FlexboxLayoutManager.LayoutParams.MATCH_PARENT);
                    layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case -3:
                List<StackRoomItemBean.AdBean> adList = stackRoomItemBean.getAd();

                Banner banner = baseViewHolder.getView(R.id.banner);
                banner.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
                banner.setOutlineProvider(new ViewOutlineProvider() { //要在加载图片之前设置这个方法
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20); //设置圆角
                    }
                });

                banner.setClipToOutline(true);
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //设置图片集合
                banner.setImages(adList);
                //设置轮播时间
                banner.setDelayTime(5000);

                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Log.e("TAG", "banner_position:" + position);
                        if (adList != null) {
                            if (position >= 0 && position < adList.size()) {
                                StackRoomItemBean.AdBean bean = adList.get(position);
                                if(bean != null){
                                    if(bean.getId() == 2){
                                        //轮播跳转VIP 充值
                                        OpenVipActivity.start(getContext());
                                    }else{
//                                       AdjustUtil.GetInstance().SendBannerEvent(6);

                                        BookDetailActivity.start(getContext(), bean.getAnid());
                                    }
                                }
                            }
                        }
                    }
                });
                //banner设置方法全部调用完毕时最后调用
                banner.start();
                if (adList != null && adList.size() > 0) {
                    MultiTransformation<Bitmap> transform = new MultiTransformation<Bitmap>(new BlurTransformation(20));
                    /*
                    Glide.with(getContext()).asBitmap().load(adList.get(0).getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .apply(new RequestOptions().
                                    bitmapTransform(transform)
                            )
                            .into((ArcImageView) baseViewHolder.getView(R.id.ivCover)); */
                }
                break;
            case -4:
                //广告
                FrameLayout container = baseViewHolder.getView(R.id.flContainer);
                if (adView != null && adView.size() > 0) {
                    container.removeAllViews();
                    int i = stackRoomItemBean.getIndex() % adView.size();
                    Log.e("TAG", "stackRoomItemBean.getIndex(): " + stackRoomItemBean.getIndex());
                    Log.e("TAG", "adView.size(): " + adView.size());
                    Log.e("TAG", "加载广告为: " + i);

                    View view = adView.get(i);
                    if (view.getParent() != null) {
                        ((ViewGroup) view.getParent()).removeAllViews();
                    }
                    container.addView(view);
                }
                break;
            case -5:
                List<StackRoomItemBean.AdBean> library_List = stackRoomItemBean.getAd();

                Banner library_banner = baseViewHolder.getView(R.id.library_banner);

                library_banner.setOutlineProvider(new ViewOutlineProvider() { //要在加载图片之前设置这个方法
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20); //设置圆角
                    }
                });

                library_banner.setClipToOutline(true);
                library_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                //设置图片加载器
                library_banner.setImageLoader(new GlideImageLoader());
                //设置图片集合
                library_banner.setImages(library_List);
                //设置轮播时间
                library_banner.setDelayTime(5000);
                library_banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Log.e("TAG", "banner_position:" + position);
                        if (library_List != null) {
                            if (position >= 0 && position < library_List.size()) {
                                StackRoomItemBean.AdBean bean = library_List.get(position);
                                if(bean != null){
                                    if(bean.getId() == 2){
                                        //轮播跳转VIP 充值
                                        OpenVipActivity.start(getContext());
                                    }else{
                                        BookDetailActivity.start(getContext(), bean.getAnid());
                                    }
                                }
                            }
                        }
                    }
                });
                //banner设置方法全部调用完毕时最后调用
                library_banner.start();
                if (library_List != null && library_List.size() > 0) {
                    MultiTransformation<Bitmap> transform = new MultiTransformation<Bitmap>(new BlurTransformation(20));
                    /*
                    Glide.with(getContext()).asBitmap().load(adList.get(0).getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .apply(new RequestOptions().
                                    bitmapTransform(transform)
                            )
                            .into((ArcImageView) baseViewHolder.getView(R.id.ivCover)); */
                }

                break;

            case 8:
                Log.d("jjajaja", "convert:11 ");
                baseViewHolder.setText(R.id.title, stackRoomItemBean.getTitle());
                layoutParams.setWidth(FlexboxLayoutManager.LayoutParams.MATCH_PARENT);
                layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);

                ImageTagAdapter mImageTagAdapter = new ImageTagAdapter(getContext());
                RecyclerView mHotRv = baseViewHolder.getView(R.id.rv_hot_new);

//              mHotRv.setLayoutManager(new FlexboxLayoutManager(getContext()));
                RelativeLayout rlImage = baseViewHolder.getView(R.id.rl_image);
                ImageView vvImage = baseViewHolder.getView(R.id.vv_image);
                rlImage.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
                vvImage.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);

                mHotRv.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
                mImageTagAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                        HomeActivity.mHomeActivity.Gotofrag_Classification(position+1);
                    }
                });
                mHotRv.setAdapter(mImageTagAdapter);
                mHotRv.setNestedScrollingEnabled(false); //禁止滑动

                if (HomeActivity.mHomeActivity.Classifications.size()>0){
                    mImageTagAdapter.setData(HomeActivity.mHomeActivity.Classifications.subList(1,HomeActivity.mHomeActivity.Classifications.size()));
                }
                break;
            case 9:
                Log.d("jjajaja", "convert: ");
                baseViewHolder.setText(R.id.title, stackRoomItemBean.getTitle());
                layoutParams.setWidth(FlexboxLayoutManager.LayoutParams.MATCH_PARENT);
                layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);

                TagsAdapter  mSearchHistoryAdapter = new TagsAdapter(getContext());
                RecyclerView mSearchHotRv= baseViewHolder.getView(R.id.rv_hot_new);

                mSearchHotRv.setLayoutManager(new FlexboxLayoutManager(getContext()));
                //mHistoryRv.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
                mSearchHistoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

                    }
                });
                mSearchHotRv.setAdapter(mSearchHistoryAdapter);
                mSearchHotRv.setNestedScrollingEnabled(false); //禁止滑动
                List<String> strs = new ArrayList<>();
                for (int k = 0; k < 10; k++) {
                    strs.add("sdafaf" + k);
                }
                mSearchHistoryAdapter.setData(strs);

                break;
            case 10:
                Log.d("滑动10", "convert:10，10，10，10 ");
                baseViewHolder.setText(R.id.mode10_title, stackRoomItemBean.getTitle());
                layoutParams.setWidth(FlexboxLayoutManager.LayoutParams.MATCH_PARENT);
                layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);

                StackRoomBookAdapter mStackRoomBookAdapter = new StackRoomBookAdapter(getContext());
                RecyclerView mSlideRv = baseViewHolder.getView(R.id.rv_hot_new);

                mSlideRv.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
                mStackRoomBookAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                        HomeActivity.mHomeActivity.Gotofrag_Classification(position+1);
                    }
                });
                mSlideRv.setAdapter(mStackRoomBookAdapter);
                mSlideRv.setNestedScrollingEnabled(false); //禁止滑动

                break;
        }
        baseViewHolder.itemView.setLayoutParams(layoutParams);
    }

    private List<View> adView = new ArrayList<>();

    public void resume() {
        if (Glide.with(getContext()).isPaused()) {
            Glide.with(getContext()).resumeRequests();
        }
    }

    public void pause() {
        if (!Glide.with(getContext()).isPaused()) {
            Glide.with(getContext()).pauseRequests();
        }
    }

    private ViewPointListener mViewPointListener;

    public void setViewPointListener(ViewPointListener viewPointListener) {
        mViewPointListener = viewPointListener;
    }


    public void addAd(View view) {
        adView.add(view);
        Log.e("TAG", "adView: " + adView.size());
//        if (adContainer != null && adContainer.size() > 0) {
//            try {
//                adContainer.get(0).removeAllViews();
//                adContainer.get(0).addView(view);
//                adContainer.remove(0);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void shffleAd() {
        if (adView.size() > 0) {
            View remove = adView.remove(0);
            adView.add(remove);
        }
    }

    public interface ViewPointListener {
        void onPointCreate(View viewPoint);
    }
}
