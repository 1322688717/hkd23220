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
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;
import cc.ixcc.novelthree.utils.NumberUtils;

public final class StackRoomHotAdapter extends MyAdapter<StackRoomBookBean.ColumnBean.ListBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;

    public StackRoomHotAdapter(Context context) {
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
        @BindView(R.id.name)
        TextView mBookName;
        @BindView(R.id.type)
        TextView mType;
        @BindView(R.id.hot)
        TextView mHot;
        @BindView(R.id.pingfen)
        TextView mPinfen;
        @BindView(R.id.fen)
        TextView fen;

        ViewHolder() {
            super(R.layout.item_stack_hot);
        }

        @Override
        public void onBindView(int position) {
            Glide.with(getContext())
                    .load(getItem(position).getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(mBookImg);
            mBookName.getPaint().setFakeBoldText(true);
            //mType.getPaint().setFakeBoldText(true);
            mHot.getPaint().setFakeBoldText(true);
            mBookName.setText(getItem(position).getTitle());
            mType.setVisibility(TextUtils.isEmpty(getItem(position).getClassify().name) ? View.GONE : View.VISIBLE);
            mType.setText(getItem(position).getClassify().name);

            mHot.setText(NumberUtils.amountConversion(getItem(position).getHots()));
            mPinfen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            fen.setVisibility(getItem(position).getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            mPinfen.setText(getItem(position).getAverage_score() + "");
        }
    }

    public interface OnClickListener {
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}