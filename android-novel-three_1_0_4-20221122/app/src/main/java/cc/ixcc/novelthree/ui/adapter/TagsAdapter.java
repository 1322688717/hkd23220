package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class TagsAdapter extends MyAdapter<String, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public TagsAdapter(Context context) {
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


        ViewHolder() {
            super(R.layout.item_tag_history);
        }

        @Override
        public void onBindView(int position) {
            mBookName.setText(getItem(position));
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