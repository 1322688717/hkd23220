package cc.ixcc.novelthree.jsReader.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.jsReader.page.TxtChapter;


/**
 * Created by newbiechen on 17-5-16.
 */

public class CategoryHolder extends ViewHolderImpl<TxtChapter> {

    private TextView mTvChapter;
    private ImageView mIvPay;
    private boolean mIsVip = false;

    public CategoryHolder(boolean isVip) {
        super();
        mIsVip = isVip;
    }

    @Override
    public void initView() {
        mTvChapter = findById(R.id.category_tv_chapter);
        mIvPay = findById(R.id.iv_pay);
    }

    @Override
    public void onBind(TxtChapter value, int pos){
//        //首先判断是否该章已下载
//        Drawable drawable = null;
//
//        //目录显示设计的有点不好，需要靠成员变量是否为null来判断。
//        //如果没有链接地址表示是本地文件
//        if (value.getLink() == null){
//            drawable = ContextCompat.getDrawable(getContext(),R.drawable.selector_category_load);
//        }
//        else {
//            if (value.getBookId() != null
//                    && BookManager
//                    .isChapterCached(value.getBookId(),value.getTitle())){
//                drawable = ContextCompat.getDrawable(getContext(),R.drawable.selector_category_load);
//            }
//            else {
//                drawable = ContextCompat.getDrawable(getContext(), R.drawable.selector_category_unload);
//            }
//        }

//        mTvChapter.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        mTvChapter.setSelected(false);
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(),R.color.nb_text_default));
        mTvChapter.setText(value.getTitle());
        mIvPay.setVisibility(mIsVip? View.VISIBLE:(value.getCoin()==0?View.GONE:(value.isIs_pay()?View.GONE:View.VISIBLE)));
//        value.isIs_vip()
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category;
    }

    public void setSelectedChapter(){
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(),R.color.tagSelected));
        mTvChapter.setSelected(true);
    }
}
