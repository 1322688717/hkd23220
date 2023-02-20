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
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.ReadHistoryBean;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class SearchRecordAdapter extends RefreshAdapter<ReadHistoryBean.ListBean> {
    private OnClickListener mClickListener;

    public SearchRecordAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_search_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        ((Vh) vh).setData(mList.get(position), position);
    }

    final class Vh extends RecyclerView.ViewHolder {

        @BindView(R.id.book_img)
        RoundedImageView mBookImg;
        @BindView(R.id.book_name)
        TextView mBookName;
        @BindView(R.id.author)
        TextView mAuthor;
        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.tv_add_status)
        TextView mAddStatus;
        @BindView(R.id.img_add_status)
        ImageView imgAddStatus;

        @BindView(R.id.tvVip)
        TextView tvVip;
        @BindView(R.id.book)
        LinearLayout book;

        public Vh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(ReadHistoryBean.ListBean bean, int position) {
            itemView.setTag(position);
            Glide.with(mContext)
                    .load(bean.getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(mBookImg);
            mBookName.setText(bean.getTitle());
            mAuthor.setText(bean.getAuthor());
            mTime.setText("update:" + bean.getUpdated_at());
            tvVip.setVisibility(TextUtils.equals(bean.getIsvip(),"1") ?View.VISIBLE:View.GONE);

            imgAddStatus.setImageResource(bean.getIs_shelve().equals(Constants.SHELVE_EXIST) ? R.mipmap.icon_exist_book : R.mipmap.icon_add_book);
            mAddStatus.setTextColor(bean.getIs_shelve().equals(Constants.SHELVE_EXIST) ? mContext.getResources().getColor(R.color.books_adds) : mContext.getResources().getColor(R.color.colorButtonPressed));
            mAddStatus.setText(bean.getIs_shelve().equals(Constants.SHELVE_EXIST) ? "Added" : "Add");

            imgAddStatus.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.addBook(position, bean);
            });

            mAddStatus.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.addBook(position, bean);
            });
            book.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.readBook(position, bean);
            });
            book.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mClickListener != null) mClickListener.deleteRecord(position, bean);
                    return false;
                }
            });
        }
    }

    public interface OnClickListener {
        void deleteRecord(int position, ReadHistoryBean.ListBean bean);

        void addBook(int position, ReadHistoryBean.ListBean bean);

        void readBook(int position, ReadHistoryBean.ListBean bean);
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}