package cc.ixcc.novelthree.widget;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

public class JsIndicator extends LinePagerIndicator {

    public JsIndicator(Context context) {
        super(context);
    }

    public void setColorAndInvalidate(Integer... colors){
        setColors(colors);
        // 计算颜色
        List<Integer> mColors = getColors();
        if (mColors != null && mColors.size() > 0) {
//            int currentColor = mColors.get(Math.abs(position) % mColors.size());
//            int nextColor = mColors.get(Math.abs(position + 1) % mColors.size());
//            int color = ArgbEvaluatorHolder.eval(positionOffset, currentColor, nextColor);
            getPaint().setColor(mColors.get(0));
        }
        invalidate();
    }

}
