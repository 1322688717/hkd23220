package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class SearchHistoryAdapter extends MyAdapter<String, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public SearchHistoryAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.tv_name)
        TextView mBookName;
        @BindView(R.id.img_hot)
        ImageView mImgHot;

        ViewHolder() {
            super(R.layout.item_search_history);
        }

        @Override
        public void onBindView(int position) {
            mBookName.setText(getItem(position));

            mImgHot.setVisibility(View.GONE);
//            mImgHot.setVisibility(getItem(position).isHot() ? View.VISIBLE : View.GONE);
        }
    }

    public interface OnClickListener {
        void addBook();
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}