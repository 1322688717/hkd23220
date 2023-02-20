package cc.ixcc.novelthree.ad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public class CanvasImageButton {

    /** 按钮图片 **/
    private Bitmap mBitButton = null;

    /** 图片绘制的XY坐标 **/
    public int mPosX = 0;
    public int mPosY = 0;
    /** 图片绘制的宽高 **/
    public int mWidth = 0;
    public int mHeight = 0;

    public CanvasImageButton(Context context, int frameBitmapID) {
        mBitButton =((BitmapDrawable)context.getResources().getDrawable(frameBitmapID)).getBitmap();
        mWidth = mBitButton.getWidth();
        mHeight = mBitButton.getHeight();
    }

    public CanvasImageButton(Context context, int frameBitmapID, int x, int y) {
        mBitButton =((BitmapDrawable)context.getResources().getDrawable(frameBitmapID)).getBitmap();
        mPosX = x;
        mPosY = y;
        mWidth = mBitButton.getWidth();
        mHeight = mBitButton.getHeight();
    }

    public CanvasImageButton(Context context, Bitmap frameBitmap, int x, int y) {
        mBitButton = frameBitmap;
        mPosX = x;
        mPosY = y;
        mWidth = mBitButton.getWidth();
        mHeight = mBitButton.getHeight();
    }

    /**
     * 绘制图片按钮
     *
     * @param canvas
     * @param paint
     */
    public void DrawImageButton(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitButton, mPosX, mPosY, paint);
    }


    public void DrawImageButton(Canvas canvas, int x, int y, Paint paint) {
        mPosX = x;
        mPosY = y;
        canvas.drawBitmap(mBitButton, mPosX, mPosY, paint);
    }

    /**
     * 绘制图片按钮
     *
     * @param canvas
     * @param paint
     */
    public void DrawMatrixImageButton(Canvas canvas, Matrix matrix, Paint paint) {
        Bitmap newBmp = Bitmap.createBitmap(mBitButton, 0, 0,
                mBitButton.getWidth(), mBitButton.getHeight(), matrix, true);
        canvas.drawBitmap(newBmp, mPosX, mPosY, paint);
    }

    /**
     * 判断是否点中图片按钮
     *
     * @param x
     * @param y
     */
    public boolean IsClick(int x, int y) {
        boolean isClick = false;
        if (x >= mPosX && x <= mPosX + mWidth && y >= mPosY
                && y <= mPosY + mHeight) {
            isClick = true;
        }
        return isClick;
    }

    public Rect GetRect() {
        return new Rect(mPosX, mPosY, mPosX + mWidth, mPosY + mHeight);
    }

}
