package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class SearchHotAdapter extends MyAdapter<String, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public SearchHotAdapter(Context context) {
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
        @BindView(R.id.tv_index)
        TextView mBookIndex;

        ViewHolder() {
            super(R.layout.item_search_hot);
        }

        @Override
        public void onBindView(int position) {

            if (position == 0) {
                mBookIndex.setBackgroundResource(R.drawable.bg_index_red);
            } else if (position == 1) {
                mBookIndex.setBackgroundResource(R.drawable.bg_index_orange);
            } else if (position == 2) {
                mBookIndex.setBackgroundResource(R.drawable.bg_index_yellow);
            } else {
                mBookIndex.setBackgroundResource(R.drawable.bg_index_gray);
            }

            mBookName.setText(getItem(position));
            mBookIndex.setText((position + 1)+"");
        }
    }

    public interface OnClickListener {
        void addBook();
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}