package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.WealBean;
import cc.ixcc.novelthree.common.MyAdapter;

import butterknife.BindView;

public final class WealSignAdapter extends MyAdapter<WealBean.SignConfigBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;
    boolean isFirst = true;
    String todayWeek = "";

    public WealSignAdapter(Context context) {
        super(context);
    }

    public void setToday(String week) {
        todayWeek = week;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {
        @BindView(R.id.week)
        TextView week;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.icon_coins)
        ImageView icon_coins;
//        @BindView(R.id.line1)
//        View line1;
//        @BindView(R.id.line2)
//        View line2;
        @BindView(R.id.coin)
        TextView coin;

        ViewHolder() {
            super(R.layout.item_sign);
        }

        @Override
        public void onBindView(int position) {
            week.setText(getItem(position).getTitle());
            coin.setText(getItem(position).getCoin());
            coin.setTextSize(12);
            img.setImageResource(getItem(position).getIs_type() == 0 ? R.mipmap.icon_x3 : (getItem(position).getIs_type() == 1 ? R.mipmap.icon_duihao : R.mipmap.icon_x3));
            if (getItem(position).getIs_type() == 2 && getItem(position).getTitle().equals("Sum.")) {
                img.setImageResource(R.mipmap.icon_jinku);
                icon_coins.setVisibility(View.GONE);
            }
            if(getItem(position).getIs_type() == 1){
                coin.setVisibility(View.GONE);
                icon_coins.setVisibility(View.GONE);
                week.setTextColor(getColor(R.color.colorsing));

            }

            //line1.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
            //line2.setVisibility(position == getItemCount() - 1 ? View.INVISIBLE : View.VISIBLE);
        }
    }

    public interface OnClickListener {
//        void read(WealBean.SignConfigBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}