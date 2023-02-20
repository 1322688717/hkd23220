package cc.ixcc.novelthree.utils;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class DiscountTimerUtil {

    private DiscountTimerUtil(){}

    private static final DiscountTimerUtil timerUtil = new DiscountTimerUtil();

    private TimerCallback over;

    public  boolean isOver = false;

    public static DiscountTimerUtil getInstance() {
        return timerUtil;
    }

    public void setOnTimerOverListener(TimerCallback over){
        this.over = over;
    }

    Timer timer;

    private int time = 60 * 30 * 1000;

    public String getFormatTime(){
        int sumSecond = time / 1000;
        int second = sumSecond % 60;
        int m = sumSecond / 60;
        StringBuilder stringBuilder = new StringBuilder();
        if (second >= 10)
            stringBuilder.append(m).append(":").append(second);
        else  stringBuilder.append(m).append(":0").append(second);
        return stringBuilder.toString();
    }

    public void startTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time > 0){
                    if (over != null){
                        over.updateTime(getFormatTime());
                    }
                    time -= 1000;
                }else {
                    timer.cancel();
                    isOver = true;
                    if (over != null){
                        over.timeOver();
                    }
                }
            }
        },1000,1000);
    }

    public interface TimerCallback{
        void updateTime(String time);
        void timeOver();
    }
}
