package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;

import butterknife.BindView;
import cc.ixcc.novelthree.bean.WealBean;
import cc.ixcc.novelthree.common.MyAdapter;

public final class TaskNoviceAdapter extends MyAdapter<WealBean.NoviceConfigBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public TaskNoviceAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {
        @BindView(R.id.time)
        TextView title;
        @BindView(R.id.coin)
        TextView coin;
        @BindView(R.id.info)
        TextView sub_title;
        @BindView(R.id.status)
        TextView status;

        ViewHolder() {
            super(R.layout.item_weal_record);
        }

        @Override
        public void onBindView(int position) {
            title.setText(getItem(position).getTitle());
            coin.setText("+" + getItem(position).getCoin() +" Coins");
            sub_title.setText(getItem(position).getSubTitle());
            status.setText("Complete");
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
        void onJump(WealBean.NoviceConfigBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}