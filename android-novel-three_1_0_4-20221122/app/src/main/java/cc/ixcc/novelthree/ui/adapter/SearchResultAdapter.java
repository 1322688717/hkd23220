package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.SearchResultBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class SearchResultAdapter extends MyAdapter<SearchResultBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public SearchResultAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.book_img)
        ImageView mBookImg;
        @BindView(R.id.book_name)
        TextView mBookName;
        @BindView(R.id.read_status)
        TextView mReadStatus;
        @BindView(R.id.author)
        TextView mAuthor;
        @BindView(R.id.book)
        LinearLayout mBookLayout;
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
            mPingFen.setVisibility(View.GONE);
            mType.setVisibility(View.GONE);
            mImgHot.setVisibility(View.GONE);
            try {
                if (TextUtils.equals(getItem(position).getIsvip(), "1")) {
                    tvVip.setVisibility(View.VISIBLE);
                } else {
                    tvVip.setVisibility(View.GONE);
                }
            }catch (Exception e){
                tvVip.setVisibility(View.GONE);
            }

//            mPingFen.setText(getItem(position).getAverage_score() + "");
//            if (getItem(position).getAverage_score() > 0) {
//                mPingFen.setVisibility(View.VISIBLE);
//            } else {
//                mPingFen.setVisibility(View.GONE);
//            }
//            mType.setText(getItem(position).getClassify());
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