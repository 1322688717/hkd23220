package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.SearchInfoBean;
import cc.ixcc.novelthree.common.MyAdapter;
import com.makeramen.roundedimageview.RoundedImageView;


import butterknife.BindView;

public final class RecommendAdapter extends MyAdapter<SearchInfoBean.RecommendBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public RecommendAdapter(Context context) {
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
        @BindView(R.id.read_status)
        TextView mReadStatus;
        @BindView(R.id.author)
        TextView mAuthor;
        @BindView(R.id.pingfen)
        TextView mPingFen;
        @BindView(R.id.tv_type)
        TextView mType;
        @BindView(R.id.img_hot)
        ImageView mImgHot;
        @BindView(R.id.tvVip)
        TextView tvVip;

        ViewHolder() {
            super(R.layout.item_search_result);
        }

        @Override
        public void onBindView(int position) {
            Glide.with(getContext())
                    .load(getItem(position).getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(mBookImg);
            mBookName.setText(getItem(position).getTitle());
            mAuthor.setText(getItem(position).getAuthor());
            mReadStatus.setText(getItem(position).getDesc());
            mPingFen.setText(getItem(position).getAverage_score() + "");
            if (getItem(position).getAverage_score() > 0) {
                mPingFen.setVisibility(View.VISIBLE);
            } else {
                mPingFen.setVisibility(View.GONE);
            }
            tvVip.setVisibility(TextUtils.equals(getItem(position).getIsvip(),"1")?View.VISIBLE:View.GONE);
            mType.setText(getItem(position).getClassify().name);
//            mImgHot.setVisibility(getItem(position).isHot() ? View.VISIBLE : View.GONE);
            mImgHot.setVisibility(View.GONE);
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