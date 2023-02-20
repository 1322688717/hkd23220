package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class StackRoomWeekAdapter extends MyAdapter<StackRoomBookBean.ColumnBean.ListBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;

    public StackRoomWeekAdapter(Context context) {
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
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.fen)
        TextView fen;

        ViewHolder() {
            super(R.layout.item_stack_week);
        }

        @Override
        public void onBindView(int position) {
            Glide.with(getContext())
                    .load(getItem(position).getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(mBookImg);
            mBookName.getPaint().setFakeBoldText(true);
            tvType.getPaint().setFakeBoldText(true);
            tvCount.getPaint().setFakeBoldText(true);
//            author.getPaint().setFakeBoldText(true);
//            content.getPaint().setFakeBoldText(true);
            mBookName.setText(getItem(position).getTitle());
            tvType.setVisibility(TextUtils.isEmpty(getItem(position).getClassify().name) ? View.GONE : View.VISIBLE);
            tvType.setText(getItem(position).getClassify().name);
            mPingFen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            fen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            mPingFen.setText(getItem(position).getAverage_score() + "");
            author.setText(getItem(position).getAuthor());
            content.setText(getItem(position).getDesc());
            tvCount.setText(getItem(position).getIswz() == Constants.lianzai ? getString(R.string.lianzai) : getString(R.string.wanjie));
        }
    }

    public interface OnClickListener {
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}