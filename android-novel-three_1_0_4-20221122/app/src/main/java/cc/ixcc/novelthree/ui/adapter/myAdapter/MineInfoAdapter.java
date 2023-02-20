package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.MineInfoBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class MineInfoAdapter extends MyAdapter<MineInfoBean.ListBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;

    public MineInfoAdapter(Context context) {
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
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.img)
        ImageView img;

        ViewHolder() {
            super(R.layout.item_mine_info);
        }

        @Override
        public void onBindView(int position) {
            title.setText(getItem(position).getName());
            Glide.with(getContext()).load(getItem(position).getIcon()).into(img);
            title.getPaint().setFakeBoldText(true); //PingFang-SC-Medium效果
            layout.setOnClickListener(view -> {
                if (mClickListener != null) mClickListener.itemClick(getItem(position), position);
            });
        }
    }

    public interface OnClickListener {
        void itemClick(MineInfoBean.ListBean bean, int position);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mClickListener = lickListener;
    }
}