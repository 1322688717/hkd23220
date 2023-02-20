package cc.ixcc.novelthree.treader.view.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class UpDownSlideAnimation extends AnimationProvider {
    private Rect mSrcRect, mDestRect, mNextSrcRect, mNextDestRect;

    public UpDownSlideAnimation(Bitmap mCurrentBitmap, Bitmap mNextBitmap, int width, int height) {
        super(mCurrentBitmap, mNextBitmap, width, height);
        mSrcRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
        mDestRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
        mNextSrcRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
        mNextDestRect = new Rect(0, 0, mScreenWidth, mScreenHeight);
    }

    boolean first = true;

    @Override
    public void drawMove(Canvas canvas) {
        refreshDraw(canvas);
        return;
        /**
         * if (getDirection().equals(Direction.next)) {
         * //            mSrcRect.left = (int) ( - (mScreenWidth - mTouch.x));
         * //            mSrcRect.right =  mSrcRect.left + mScreenWidth;
         * //            mSrcRect.left = 0;
         * //            mSrcRect.right = mScreenWidth;
         *             int y1 = pageWidget.getCurrY();
         *             int y2 = pageWidget.getFinalY();
         * //            if (scroller.getCurrY() < mScreenHeight)
         *             int dis = (int) (myStartY - mTouch.y);
         *             if (dis > mScreenHeight) {
         *                 dis = mScreenHeight;
         *             }
         *             //计算bitmap截取的区域
         *             mSrcRect.top = 0 ;
         *             //计算bitmap在canvas显示的区域
         *             mDestRect.bottom = dis;
         *
         *             //计算下一页截取的区域
         *             mNextSrcRect.bottom = mScreenHeight;
         *             //计算下一页在canvas显示的区域
         *             mNextDestRect.top = dis;
         * //            if (first) {
         *             canvas.drawBitmap(mNextPageBitmap, mNextSrcRect, mNextDestRect, null);
         *             canvas.drawBitmap(mCurPageBitmap, mSrcRect, mDestRect, null);
         * //                first = false;
         * //            }
         *         } else {
         *             int dis = (int) (mTouch.y - myStartY);
         *             if (dis < 0) {
         *                 dis = 0;
         *                 myStartY = mTouch.y;
         *             }
         *             mSrcRect.top = mScreenHeight - dis;
         *             mDestRect.bottom = dis;
         *
         *             //计算下一页截取的区域
         *             mNextSrcRect.bottom = mScreenHeight - dis;
         *             //计算下一页在canvas显示的区域
         *             mNextDestRect.top = dis;
         *
         *             canvas.drawBitmap(mNextPageBitmap, mSrcRect, mDestRect, null);
         *             canvas.drawBitmap(mCurPageBitmap, mNextSrcRect, mNextDestRect, null);
         *         }
         */
    }

    @Override
    public void onRelaseUp() {
        super.onRelaseUp();
        margin = pre_margin;
        Log.i("test", "margin: " + margin);
    }

    private int margin = 0;
    private int pre_margin = 0;

    private void refreshDraw(Canvas canvas) {

        int dis = (int) (myStartY - mTouch.y);
        if (dis > mScreenHeight) {
            dis = mScreenHeight;
        }

        int ma_dis = dis + margin;
        int abs_dis = Math.abs(ma_dis);
        //  boolean over_half= abs_dis> mScreenHeight/2;
        if (ma_dis > 0) {
            // roll to up
            mSrcRect.top = abs_dis;
            mSrcRect.bottom = mScreenHeight;
            mDestRect.top = 0;
            mDestRect.bottom = mScreenHeight - abs_dis;

            mNextSrcRect.top = 0;
            mNextSrcRect.bottom = abs_dis;
            mNextDestRect.top = mScreenHeight - abs_dis;
            mNextDestRect.bottom = mScreenHeight;
        } else {
            //roll to down
            mSrcRect.top = 0;
            mSrcRect.bottom = mScreenHeight - abs_dis;
            mDestRect.top = abs_dis;
            mDestRect.bottom = mScreenHeight;

            mNextSrcRect.top = mScreenHeight - abs_dis;
            mNextSrcRect.bottom = mScreenHeight;
            mNextDestRect.top = 0;
            mNextDestRect.bottom = abs_dis;
        }
        canvas.drawBitmap(mCurPageBitmap, mSrcRect, mDestRect, null);
        canvas.drawBitmap(mNextPageBitmap, mNextSrcRect, mNextDestRect, null);
        this.pre_margin = dis;
        Log.i("test", "pre_margin: " + pre_margin);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (getCancel()) {
            canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(mNextPageBitmap, 0, 0, null);
        }
    }

    @Override
    public void startAnimation(Scroller scroller) {

//        int dx = 0;
//        if (getDirection().equals(Direction.next)){
//            if (getCancel()){
//                int dis = (int) ((mScreenHeight - myStartY) + mTouch.y);
//                if (dis > mScreenHeight){
//                    dis = mScreenHeight;
//                }
//                dx = mScreenHeight - dis;
//            }else{
//                dx = (int) - (mTouch.y + (mScreenHeight - myStartY));
//            }
//        }else{
//            if (getCancel()){
//                dx = (int) - Math.abs(mTouch.y - myStartY);
//            }else{
////                dx = (int) (mScreenWidth - mTouch.x);
//                dx = (int) (mScreenHeight - (mTouch.y - myStartY));
//            }
//        }
//        //滑动速度保持一致
//         int duration =  (mSpeed * Math.abs(dx)) / mScreenHeight;
//        Log.e("duration",duration + "");
////        scroller.startScroll(0,  (int)mTouch.y, 0, dx, duration);
//        scroller.startScroll((int)mTouch.y,  0, dx, 0, duration);
    }

}
