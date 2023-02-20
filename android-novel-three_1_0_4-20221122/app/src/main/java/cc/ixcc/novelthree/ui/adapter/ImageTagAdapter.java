package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.bean.ClassificationTitleBean;
import cc.ixcc.novelthree.common.MyAdapter;
import com.makeramen.roundedimageview.RoundedImageView;


import butterknife.BindView;
//SearchInfoBean.RecommendBean
public final class ImageTagAdapter extends MyAdapter<ClassificationTitleBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public ImageTagAdapter(Context context) {
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
            super(R.layout.item_image_tag_result);
        }

        @Override
        public void onBindView(int position) {

            Glide.with(getContext())
                    .load(getItem(position).getCover())
//                    .load(R.mipmap.book_img1)
                    .into(mBookImg);
//            mBookName.setText(getItem(position).getTitle());

            mBookName.setText(getItem(position).getNovel_name());

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