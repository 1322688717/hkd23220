package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class CoinInfoAdapter extends MyAdapter<String, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public CoinInfoAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.info)
        TextView info;
        @BindView(R.id.layout)
        RelativeLayout layout;

        ViewHolder() {
            super(R.layout.item_coin_info);
        }

        @Override
        public void onBindView(int position) {
            info.setText(getItem(position));
        }
    }

    public interface OnClickListener {
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}