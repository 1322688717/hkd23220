package cc.ixcc.novelthree.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cc.ixcc.novelthree.R;
import java.util.Timer;
import java.util.TimerTask;

import cc.ixcc.novelthree.jsReader.adapter.CategoryAdapter;
import cc.ixcc.novelthree.jsReader.page.PageLoader;
import cc.ixcc.novelthree.jsReader.page.PageView;
//import cc.ixcc.novelthree.utils.AdHelper;

public class AdTimerView extends BaseAppView {
    private int time = 15;
    private Timer timer_15;
    private TimerTask timerTask_15;
    private PageView pageView;
    private PageLoader pageLoader;
    private TextView mTvTimer;
    private LinearLayout linAd;
    private long lastTime;

    public AdTimerView(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onBaseInit() {
        super.onBaseInit();
        setContentView(R.layout.ad_timer_view);
        mTvTimer = findViewById(R.id.tv_timer);
        linAd = findViewById(R.id.lin_ad);
    }

    /**
     * 广告倒计时
     *
     * @param activity
     * @param mTvTimer
     */
    public void get15sTimer(Activity activity, TextView mTvTimer, CategoryAdapter categoryAdapter) {
        onStopTimer(timer_15, timerTask_15);
        timer_15 = new Timer();
        timerTask_15 = new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                activity.runOnUiThread(() -> {
                    if (mTvTimer != null) {
                        if (time == 0) {
                            onStopTimer(timer_15, timerTask_15);
                            if (pageLoader.skipNextChapter()) {
                                categoryAdapter.setChapter(pageLoader.getChapterPos());
                            }
                            pageView.setVisibility(VISIBLE);
                            return;
                        }
                        mTvTimer.setText("倒计时 " + time-- + "（S）");
                    }
                });
            }
        };
        timer_15.schedule(timerTask_15, 0, 1000);
    }

    public static void onStopTimer(Timer timer, TimerTask timerTask) {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
        }
    }

    public void setParams(Activity activity, PageView pageView, PageLoader pageLoader, LinearLayout mFlAd, CategoryAdapter categoryAdapter, LinearLayout mFlContainer) {
        this.pageView = pageView;
        this.pageLoader = pageLoader;
        //AdvanceAD.getAdNativ(activity, linAd);
       // new AdvanceAD(activity).loadNativeExpress(linAd);

        get15sTimer(activity, mTvTimer, categoryAdapter);
        findViewById(R.id.tv_add_videoad).setOnClickListener(v -> {
            if (!block()){
               // new AdvanceAD(activity).loadReward(null);
                //AdvanceAD.loadRewardVideoAd(activity);  //, pageLoader, categoryAdapter, mFlAd, pageView, mFlContainer);
                onStopTimer(timer_15, timerTask_15);
            }
        });
    }

    public boolean block() {
        boolean block = System.currentTimeMillis() - lastTime < 1000;
        lastTime = System.currentTimeMillis();
        return block;
    }

    @Override
    protected void onDetachedFromWindow() {
        onStopTimer(timer_15, timerTask_15);
        super.onDetachedFromWindow();
    }
}
