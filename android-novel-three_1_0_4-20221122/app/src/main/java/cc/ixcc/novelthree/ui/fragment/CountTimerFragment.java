package cc.ixcc.novelthree.ui.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.tencent.mmkv.MMKV;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.ActiviyNoticeInfo;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.pay.ali.GooglePay;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.ThirdLoginActivity;
import cc.ixcc.novelthree.ui.activity.my.TopUpActivity;
import cc.ixcc.novelthree.utils.DialogUitl;
import cc.ixcc.novelthree.utils.DiscountTimerUtil;
import cc.ixcc.novelthree.utils.SpUtil;


public class CountTimerFragment extends Fragment implements DiscountTimerUtil.TimerCallback {

    ActiviyNoticeInfo discount;
    ActiviyNoticeInfo unDiscount;

    private ImageView img;
    private TextView txTimer;
    private LinearLayout timeContent;

    public CountTimerFragment() {

    }

    public CountTimerFragment(ActiviyNoticeInfo discount, ActiviyNoticeInfo unDiscount) {
        this.discount = discount;
        this.unDiscount = unDiscount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DiscountTimerUtil.getInstance().setOnTimerOverListener(this);
        return inflater.inflate(R.layout.fragment_count_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txTimer = view.findViewById(R.id.timers);
        img = view.findViewById(R.id.loaddd);
        timeContent = view.findViewById(R.id.time_content);

        if (!DiscountTimerUtil.getInstance().isOver && discount!=null) {
            timeContent.setVisibility(View.VISIBLE);
            Glide.with(view.getContext()).load(discount.getUrl()).into(img);
        } else {
            timeContent.setVisibility(View.GONE);
            img.setScaleType(ImageView.ScaleType.CENTER);
            Glide.with(view.getContext()).load(unDiscount.getUrl()).error(R.mipmap.bg_default2).into(img);
        }


        img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (discount != null && !DiscountTimerUtil.getInstance().isOver) {

                    Dialog dialog = DialogUitl.loadingDialog(getActivity(), "loading");
                    dialog.show();

                    // 延迟5秒执行
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }, 5000);


                    UserBean tempBean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                    //17支付活动 18新书推荐 19VIP活动 20URL活动
                    if (discount.getType() == 17) {
                        if (tempBean.getBindStatus().equals("0")) {
                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                        } else {
                            if (TopUpActivity.MYActivity.beandata != null){
                                for (int i = 0; i < TopUpActivity.MYActivity.beandata.getCoin_list().size(); i++) {
                                    if (TopUpActivity.MYActivity.beandata.getCoin_list().get(i).getChargeIndex() != -99){
                                        if (TopUpActivity.MYActivity.beandata.getCoin_list().get(i).getpayid().equals(discount.getpay_id())) {
                                            if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) {
                                                GooglePay.GetInstance().PayForCoin(getActivity(), TopUpActivity.MYActivity.beandata.getCoin_list().get(i).getpayid(),
                                                        TopUpActivity.MYActivity.beandata.getCoin_list().get(i).getChargeIndex(), TopUpActivity.MYActivity.beandata.getCoin_list().get(i).getRmb());
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }else {
                    BookDetailActivity.start(getContext(), unDiscount.getAnid());
                }
            }
        });
    }

    @Override
    public void updateTime(String time) {
        txTimer.post(new Runnable() {
            @Override
            public void run() {
                txTimer.setText(time);
            }
        });
    }

    @Override
    public void timeOver() {
        timeContent.post(new Runnable() {
            @Override
            public void run() {
                timeContent.setVisibility(View.GONE);
                img.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(getContext()).load(unDiscount.getUrl()).error(R.mipmap.bg_default2).into(img);
            }
        });
    }
}