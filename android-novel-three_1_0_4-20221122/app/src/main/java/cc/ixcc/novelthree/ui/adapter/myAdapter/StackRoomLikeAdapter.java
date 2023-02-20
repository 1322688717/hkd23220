package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class StackRoomLikeAdapter extends MyAdapter<StackRoomBookBean.AdBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;

    public StackRoomLikeAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.img)
        ImageView mBookImg;
        @BindView(R.id.name)
        TextView mBookName;
        @BindView(R.id.delete)
        LinearLayout mDelete;

        ViewHolder() {
            super(R.layout.item_stack_like);
        }

        @Override
        public void onBindView(int position) {
            Glide.with(getContext())
                    .load(getItem(position).getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(mBookImg);
            mBookName.getPaint().setFakeBoldText(true);
            mBookName.setText(getItem(position).getTitle());
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (getData().size() > 3) {
                        if (mClickListener != null) {
                            mClickListener.delete(getItem(position), position);
                        }
//                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void delete(StackRoomBookBean.AdBean bean, int position);
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}