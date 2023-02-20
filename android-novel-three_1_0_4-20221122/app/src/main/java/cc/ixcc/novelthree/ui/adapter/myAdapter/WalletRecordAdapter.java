package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.WalletBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class WalletRecordAdapter extends MyAdapter<WalletBean.FinanceBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public WalletRecordAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.coin)
        TextView coin;
        @BindView(R.id.layout)
        RelativeLayout layout;

        ViewHolder() {
            super(R.layout.item_wallet_record);
        }

        @Override
        public void onBindView(int position) {
            title.setText(getItem(position).getType());
            time.setText(getItem(position).getUpdated_at());
//            coin.setText((getItem(position).getValue() > 0 ? "+" : "") + getItem(position).getValue() +  Constants.getInstance().getCoinName());
            coin.setText(getItem(position).getValue() +  Constants.getInstance().getCoinName());
        }
    }

    public interface OnClickListener {
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}