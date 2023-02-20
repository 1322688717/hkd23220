package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.SearchInfoBean;
import cc.ixcc.novelthree.common.MyAdapter;

import com.jiusen.base.util.DpUtil;
import com.makeramen.roundedimageview.RoundedImageView;


import butterknife.BindView;

public final class RecommendAdapter_new extends MyAdapter<SearchInfoBean.RecommendBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public RecommendAdapter_new(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.book_img)
        RoundedImageView mBookImg;
        @BindView(R.id.book_name)
        TextView mBookName;

        ViewHolder() {
            super(R.layout.item_search_result_new);
        }

        @Override
        public void onBindView(int position) {

            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) getItemView().getLayoutParams();
            int width = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
            layoutParams.setWidth((width - DpUtil.dip2px(getContext(), 10)) / 3);
            layoutParams.setHeight(FlexboxLayoutManager.LayoutParams.WRAP_CONTENT);

            Glide.with(getContext())
                    .load(getItem(position).getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(mBookImg);
            mBookName.setText(getItem(position).getTitle());
//            mImgHot.setVisibility(getItem(position).isHot() ? View.VISIBLE : View.GONE);
//            mBookLayout.setOnClickListener(v -> {
//
//            });
        }
    }

    public interface OnClickListener {
        void addBook();
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}