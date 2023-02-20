package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.VipHomeBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class VipChargeAdapter extends MyAdapter<VipHomeBean.PackageBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;
    private RelativeLayout lastlayout;
    private int lastposition = -1;

    public VipChargeAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.money)
        TextView money;
        @BindView(R.id.old_money)
        TextView oldMoney;
        @BindView(R.id.ivBg)
        ImageView ivBg;
        @BindView(R.id.song)
        TextView song;
        @BindView(R.id.layout)
        RelativeLayout layout;

        ViewHolder() {
            super(R.layout.item_vip_charge);
        }

        @Override
        public void onBindView(int position) {
            day.setText(getItem(position).getDay() + "days");
            money.setText("$"+getItem(position).getPrice());
            if (getItem(position).getSday()!=0) {
                song.setText("+" + getItem(position).getSday() + " days");
                song.setVisibility(View.VISIBLE);
            } else {
                song.setVisibility(View.GONE);
            }
            if (Float.parseFloat(getItem(position).getOriginal_price())==0f) {
                oldMoney.setVisibility(View.INVISIBLE);
            }else {
                oldMoney.setText(getItem(position).getOriginal_price());
                oldMoney.setVisibility(View.VISIBLE);
            }
//            if (!TextUtils.isEmpty(getItem(position).getOriginal_price())) {
//                oldMoney.setText(getItem(position).getOriginal_price());
//                oldMoney.setVisibility(View.VISIBLE);
//            } else {
//                oldMoney.setVisibility(View.GONE);
//            }
            oldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lastlayout != null) {
                        ((ImageView)lastlayout.findViewById(R.id.ivBg)).setImageResource(R.drawable.bg_pakage_white);
                        lastlayout = null;
                    }
                    if (lastposition != position) {
//                        layout.setBackground(getDrawable(R.drawable.bg_conner_gold3));
                        ((ImageView)layout.findViewById(R.id.ivBg)).setImageResource(R.drawable.bg_pakage_select);
                        lastlayout = layout;
                        if (mLickListener != null) mLickListener.onActionClick(getItem(position));
                    } else {
                        lastposition = -1;
//                        EventBus.getDefault().post(new CancelPayEvent());
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onActionClick(VipHomeBean.PackageBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}