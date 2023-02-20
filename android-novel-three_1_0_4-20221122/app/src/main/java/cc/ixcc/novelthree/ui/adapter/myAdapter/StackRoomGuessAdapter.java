package cc.ixcc.novelthree.ui.adapter.myAdapter;

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
import cc.ixcc.novelthree.bean.BookDetailBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class StackRoomGuessAdapter extends MyAdapter<BookDetailBean.GuessBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;

    public StackRoomGuessAdapter(Context context) {
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
        @BindView(R.id.pingfen)
        TextView mPingFen;
        @BindView(R.id.fen)
        TextView fen;
        @BindView(R.id.tvVip)
        TextView tvVip;
        @BindView(R.id.pingfenbg)
        LinearLayout pingfenbg;

        ViewHolder() {
            super(R.layout.item_stack_book_detail);
        }

        @Override
        public void onBindView(int position) {
            Glide.with(getContext())
                    .load(getItem(position).getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(mBookImg);
            mBookName.getPaint().setFakeBoldText(true);
            mBookName.setText(getItem(position).getTitle());

            tvVip.setVisibility(TextUtils.equals(getItem(position).getIsvip(),"1")?View.VISIBLE:View.GONE);
            mPingFen.setVisibility(View.GONE);
            fen.setVisibility(View.GONE);
            pingfenbg.setVisibility(View.GONE);
//            mPingFen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
//            fen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
//            mPingFen.setText(getItem(position).getAverage_score() + "");
        }
    }

    public interface OnClickListener {
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}