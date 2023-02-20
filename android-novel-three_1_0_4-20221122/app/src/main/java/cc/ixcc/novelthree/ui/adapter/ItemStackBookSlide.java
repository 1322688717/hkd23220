package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class ItemStackBookSlide extends MyAdapter<StackRoomBookBean.ColumnBean.ListBean, RecyclerView.ViewHolder>
{
    private OnClickListener mClickListener;
    private int mType;

    public ItemStackBookSlide(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    final class ViewHolder extends MyAdapter.ViewHolder{
        @BindView(R.id.img)
        ImageView mBookImg;
        @BindView(R.id.name)
        TextView mBookName;
        @BindView(R.id.pingfen)
        TextView mPingFen;
        @BindView(R.id.fen)
        TextView fen;

        public ViewHolder() {
            super(R.layout.item_stack_book_slide);
        }

        @Override
        public void onBindView(int position) {
            Glide.with(getContext())
                    .load(getItem(position).getCoverpic())
                    .into(mBookImg);
            mBookName.getPaint().setFakeBoldText(true);
            mBookName.setText(getItem(position).getTitle());
            mPingFen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            fen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            mPingFen.setText(getItem(position).getAverage_score() + "");
        }
    }
    public interface OnClickListener {
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}