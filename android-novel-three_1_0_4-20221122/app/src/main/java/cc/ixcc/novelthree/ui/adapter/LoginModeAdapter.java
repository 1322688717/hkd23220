package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.MobBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class LoginModeAdapter extends MyAdapter<MobBean, RecyclerView.ViewHolder> {

    public LoginModeAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.img)
        ImageView mImg;
        @BindView(R.id.tv_info)
        TextView mTextView;

        ViewHolder() {
            super(R.layout.item_login_mode);
        }

        @Override
        public void onBindView(int position) {
            mImg.setImageResource(getItem(position).getIcon1());
            mTextView.setText(getItem(position).getName());
        }
    }
}