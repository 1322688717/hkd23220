package cc.ixcc.novelthree.ui.adapter.myAdapter;

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

public final class StackRoomSuggestAdapter extends MyAdapter<StackRoomBookBean.ColumnBean.ListBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;

    public StackRoomSuggestAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.book_img)
        ImageView bookImg;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.pingfen)
        TextView pingfen;
        @BindView(R.id.fen)
        TextView fen;

        ViewHolder() {
            super(R.layout.item_stack_suggest);
        }

        @Override
        public void onBindView(int position) {
            Glide.with(getContext())
                    .load(getItem(position).getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(bookImg);
            name.getPaint().setFakeBoldText(true);
//            author.getPaint().setFakeBoldText(true);
            name.setText(getItem(position).getTitle());
            pingfen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            fen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            pingfen.setText(getItem(position).getAverage_score() + "");
            author.setText(getItem(position).getAuthor());
        }
    }

    public interface OnClickListener {
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}