package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.ad.AdMobManager;
import cc.ixcc.novelthree.bean.BuyCoinInfo;
import cc.ixcc.novelthree.common.MyAdapter;
import java.text.DecimalFormat;
import butterknife.BindView;
import cc.ixcc.novelthree.ui.activity.my.TopUpActivity;

public final class BuyCoinAdapter extends MyAdapter<BuyCoinInfo.CoinListBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;
    private RelativeLayout lastlayout;
    private ImageView lastimgBottom;
    TextView lastmoney;
    TextView lastcoin;
    TextView lastcoin1;
    TextView lastVoucher_Rate;
    private int lastposition = -1;
    public static int sChargeID = 0;

    Context mContext;
    public BuyCoinAdapter(Context context) {
        super(context);

        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {
        @BindView(R.id.money)
        TextView money;
        @BindView(R.id.coin)
        TextView coin;
        @BindView(R.id.coin1)
        TextView coin1;
        @BindView(R.id.coin2)
        TextView coin2;
        @BindView(R.id.Voucher_Rate)
        TextView Voucher_Rate;
        @BindView(R.id.layout)
        RelativeLayout layout;

        ViewHolder() {
            super(R.layout.item_withdraw_coin);
        }

        @Override
        public void onBindView(int position) {

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    if (getItem(position).getChargeIndex()==-99){
//
//                        AdMobManager.GetInstance().SetOnUserEarnedRewardListener(new AdMobManager.OnUserEarnedRewardListener() {
//                            @Override
//                            public void onUserEarnedReward() {
//                                //激励视频激励成功
//                                AdMobManager.GetInstance().GetCoin("15",2);
//                            }
//                        });
//                        AdMobManager.GetInstance().showRewardedVideo((Activity) mContext);
//                        return;
//                    }

                    //sChargeID
                    if (lastlayout != null) {
                        lastlayout.setBackground(getDrawable(R.drawable.topup_frame));
                        //lastmoney.setTextColor(Color.parseColor("#eb515d"));
                        //lastcoin.setTextColor(Color.parseColor("#333333"));
                        TextPaint tp = lastcoin.getPaint();
                        tp.setFakeBoldText(false);

                        //lastcoin1.setTextColor(Color.parseColor("#FF0000"));
                        //TextPaint tp1 = lastcoin1.getPaint();
                        //tp1.setFakeBoldText(false);

                        //lastVoucher_Rate.setVisibility(View.INVISIBLE);
                        lastlayout = null;
                    }
                    if (lastposition != position) {
                        //money.setTextColor(Color.parseColor("#eb515d"));
                        //加粗
                        //coin.setTextColor(Color.parseColor("#39383d"));
                        //TextPaint tp = coin.getPaint();
                        //tp.setFakeBoldText(true);

                        //coin1.setTextColor(Color.parseColor("#6c6c6c"));
                        //TextPaint tp1 = coin1.getPaint();
                        //tp1.setFakeBoldText(true);
                        layout.setBackground(getDrawable(R.drawable.topup_selectframe1));
                        lastlayout = layout;
                        lastposition = position;
                        lastmoney = money;
                        lastcoin = coin;
                        lastcoin1 = coin1;
                        //lastVoucher_Rate = Voucher_Rate;
                        if (mLickListener != null) {
                            mLickListener.onActionClick(getItem(position));
                        }
                        else {
                            mLickListener.onClear();
                        }
                    }
                    else {
                        lastposition = -1;
                    }
                }
            });

            if (getItem(position).getChargeIndex() == -99) {
//                money.setVisibility(View.GONE);
//                coin.setVisibility(View.GONE);
//                coin1.setVisibility(View.GONE);
//                coin2.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                return;
            }

            if (TopUpActivity.money_symbol.equals("$")) {
                money.setText("US$ " + getItem(position).getRmb());
            } else {
                money.setText(TopUpActivity.money_symbol + " " + getItem(position).getRmb());
            }
            coin.setText(getFormatString(getItem(position).getCoin() + "") + " " + Constants.getInstance().getCoinName());

            if (getItem(position).gethandsel() == 0) {
                coin1.setVisibility(View.GONE);
                GridLayout.LayoutParams topContentTextView_lp = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));//此处我需要均分高度就在heignt处设0,1.0f即设置权重是1，页面还有其他一个控件,1：1高度就均分了
                coin.setLayoutParams(topContentTextView_lp);

            } else {
                // Log.e("BuyCoinAdapter",String.valueOf(getItem(position).gethandsel())+"-----"+getItem(position).getCoin()+"---"+String.valueOf(getItem(position).gethandsel() * (getItem(position).getCoin())));
                int presenter = (int) (getItem(position).gethandsel() * (getItem(position).getCoin()));
                coin1.setText("+" + presenter + " Coins");
                coin2.setText("+ " + (int) (getItem(position).gethandsel() * 100) + "%");
            }

            if (position == 0) {
                Voucher_Rate.setVisibility(View.VISIBLE);
                Voucher_Rate.setText("Reco");

                layout.callOnClick();
            } else if (position == 1) {
                Voucher_Rate.setVisibility(View.VISIBLE);
                Voucher_Rate.setText("Popular");
            }

        }
    }

    private String getFormatString(String str) {
        DecimalFormat df = new DecimalFormat("####,####");
        return df.format(Double.parseDouble(str));
    }

    public interface OnClickListener {
        void onActionClick(BuyCoinInfo.CoinListBean bean);
        void onClear();
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}