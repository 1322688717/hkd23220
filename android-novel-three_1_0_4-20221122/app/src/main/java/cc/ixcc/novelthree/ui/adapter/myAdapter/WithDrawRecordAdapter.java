package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.WithDrawRecord;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class WithDrawRecordAdapter extends MyAdapter<WithDrawRecord, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public WithDrawRecordAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.coin)
        TextView coin;
        @BindView(R.id.more)
        ImageView more;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder() {
            super(R.layout.item_withdraw_record);
        }

        @Override
        public void onBindView(int position) {
            coin.setText("提现" + getItem(position).getCoin() +  Constants.getInstance().getCoinName());
            time.setText(getItem(position).getCreated_at());
            getStatus(status, getItem(position).getStatus());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLickListener != null) mLickListener.onActionClick(getItem(position));
                }
            });
        }
    }

    private void getStatus(TextView statustv, int status) {
        switch (status) { //0待审核1通过2拒绝
            case Constants.WITHDRAW_VERIFY: //审核中
                statustv.setText("审核中");
                statustv.setTextColor(Color.parseColor("#FCAD0F"));
                break;
            case Constants.WITHDRAW_FAIL: //提现失败
                statustv.setText("提现失败");
                statustv.setTextColor(Color.parseColor("#D53D3C"));
                break;
            case Constants.WITHDRAW_SUCCESS: //提现成功
                statustv.setText("提现成功");
                statustv.setTextColor(Color.parseColor("#4D77FD"));
                break;
        }
    }

    public interface OnClickListener {
        void onActionClick(WithDrawRecord bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}