package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.VipBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class VipProfitAdapter extends MyAdapter<VipBean.VipPrivilegeBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public VipProfitAdapter(Context context) {
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
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder() {
            super(R.layout.item_vip_profit2);
        }

        @Override
        public void onBindView(int position) {
            title.setText(getItem(position).getTitle());
            content.setText(getItem(position).getContent());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }

    public interface OnClickListener {
        void onActionClick(VipBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}