package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.WealBean;
import cc.ixcc.novelthree.common.MyAdapter;
import butterknife.BindView;

public final class TaskDayReadAdapter extends MyAdapter<WealBean.DailyReadConfigBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public TaskDayReadAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {
        @BindView(R.id.line)
        View line;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.coin)
        TextView coin;
        @BindView(R.id.info)
        TextView info;
        @BindView(R.id.status)
        TextView status;

        ViewHolder() {
            super(R.layout.item_weal_record);
        }

        @Override
        public void onBindView(int position) {
            time.setText(getItem(position).getKey() + "s "+" reading.");
            coin.setText("+" + getItem(position).getCoin() +" Coins");
            info.setText("Read for " + getItem(position).getKey() +"s ");
            status.setText(getItem(position).getStatus().equals("0") ? "To Read" : "Claim");
            status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLickListener !=null) {
                        mLickListener.onJump(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onJump(WealBean.DailyReadConfigBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}