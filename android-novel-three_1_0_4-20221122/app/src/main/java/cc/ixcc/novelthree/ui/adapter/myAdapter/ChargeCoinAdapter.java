package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.ChargeBean;
import cc.ixcc.novelthree.common.MyAdapter;
import cc.ixcc.novelthree.event.CancelPayEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public final class ChargeCoinAdapter extends MyAdapter<ChargeBean.NormalBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;
    private RelativeLayout lastlayout;
    private ImageView lastimgBottom;
    TextView lastfuhao;
    TextView lastmoney;
    TextView lastcoin;
    private int lastposition = -1;
    boolean isvip;

    public ChargeCoinAdapter(Context context) {
        super(context);
    }

    public void setVip(boolean isvip) {
        this.isvip = isvip;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.money)
        TextView money;
        @BindView(R.id.ll_money)
        LinearLayout llMoney;
        @BindView(R.id.coin)
        TextView coin;
        @BindView(R.id.img_bottom)
        ImageView imgBottom;
        @BindView(R.id.song)
        TextView song;
        @BindView(R.id.layout)
        RelativeLayout layout;
        @BindView(R.id.fuhao)
        TextView fuhao;

        ViewHolder() {
            super(R.layout.item_charge_coin);
        }

        @Override
        public void onBindView(int position) {
            money.setText(getItem(position).getMoney());
            if (isvip) {
                coin.setText(getItem(position).getDesc());
                song.setVisibility(View.GONE);
            } else {
                if (getItem(position).getScoin() == 0) {
                    coin.setText(getItem(position).getCoin() +  Constants.getInstance().getCoinName());
                    song.setVisibility(View.GONE);
                } else {
                    coin.setText(getItem(position).getCoin() +  Constants.getInstance().getCoinName() + "+赠送" + getItem(position).getScoin() +  Constants.getInstance().getCoinName());
                    song.setVisibility(View.VISIBLE);
                    song.setText("赠送" + getItem(position).getScoin() +  Constants.getInstance().getCoinName());
                }
            }
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lastlayout != null) {
                        lastlayout.setBackground(getDrawable(R.drawable.bg_conner_green2));
                        lastimgBottom.setVisibility(View.GONE);
                        lastfuhao.setTextColor(Color.parseColor("#333333"));
                        lastmoney.setTextColor(Color.parseColor("#333333"));
                        lastcoin.setTextColor(Color.parseColor("#909399"));
                        lastlayout = null;
                    }
                    if (lastposition != position) {
                        layout.setBackground(getDrawable(R.drawable.bg_board_blue4));
                        imgBottom.setVisibility(View.VISIBLE);
                        lastlayout = layout;
                        lastimgBottom = imgBottom;
                        lastposition = position;
                        lastfuhao = fuhao;
                        lastmoney = money;
                        lastcoin = coin;
                        fuhao.setTextColor(Color.parseColor("#4D77FD"));
                        money.setTextColor(Color.parseColor("#4D77FD"));
                        coin.setTextColor(Color.parseColor("#4D77FD"));
                        if (mLickListener != null) mLickListener.onActionClick(getItem(position));
                    } else {
                        lastposition = -1;
                        EventBus.getDefault().post(new CancelPayEvent());
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onActionClick(ChargeBean.NormalBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}