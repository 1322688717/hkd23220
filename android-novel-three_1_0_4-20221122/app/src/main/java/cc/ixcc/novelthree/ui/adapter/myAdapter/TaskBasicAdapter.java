package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cc.ixcc.novelthree.R;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.bean.WealBean;
import cc.ixcc.novelthree.common.MyAdapter;
import cc.ixcc.novelthree.utils.SpUtil;

public final class TaskBasicAdapter extends MyAdapter<WealBean.BasicConfigBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public TaskBasicAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {
        //标题
        @BindView(R.id.time)
        TextView title;
        //奖励金币
        @BindView(R.id.coin)
        TextView coin;
        //副标题
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

            //获取用户信息，判断是否已经绑定过邮箱
            MMKV mmkv = MMKV.defaultMMKV();
            UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
            if(userBean.getEmail() != null && !userBean.getEmail().equals(""))
            {
                coin.setVisibility(View.INVISIBLE);
                status.setText("Modify");
            }
            else
            {
                coin.setVisibility(View.VISIBLE);
            }

            status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        if (mLickListener !=null) {
                            mLickListener.bindEmail(getItem(position));
                        }
                }
            });
        }
    }

    public interface OnClickListener {
        void bindEmail(WealBean.BasicConfigBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}