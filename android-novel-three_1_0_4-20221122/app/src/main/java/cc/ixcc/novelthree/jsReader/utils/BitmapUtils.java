package cc.ixcc.novelthree.jsReader.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class BitmapUtils {
    public static Bitmap createBitmapFromView(View view,int width,int height) {
        //是ImageView直接获取
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        }
        view.clearFocus();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            view.setDrawingCacheEnabled(true);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            canvas.setBitmap(null);
        }
        return bitmap;
    }

    /**
     * 将2张图片合成
     * @param downBitmap  底部图片
     * @param upBitmap  置顶的图片
     * @return
     */
    public static Bitmap compoundBitmap(Bitmap downBitmap,Bitmap upBitmap)
    {
        Bitmap mBitmap = downBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //如果遇到黑色，则显示downBitmap里面的颜色值，如果不是则显示upBitmap里面的颜色值
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        //首先保证downBitmap和 upBitmap是一致的高宽大小
        if(mBitmapWidth==upBitmap.getWidth() && mBitmapHeight==upBitmap.getHeight())
        {
            for (int i = 0; i < mBitmapHeight; i++) {
                for (int j = 0; j < mBitmapWidth; j++) {
                    //获得Bitmap 图片中每一个点的color颜色值
                    //将需要填充的颜色值如果不是
                    //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
                    //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000
                    //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了
                    int color = upBitmap.getPixel(j, i);
                    //将颜色值存在一个数组中 方便后面修改
                    if (color != Color.BLACK) {
                        mBitmap.setPixel(j, i, upBitmap.getPixel(j, i));  //将白色替换成透明色
                    }
                }
            }
        }
//        downBitmap.recycle();
//        upBitmap.recycle();
        return mBitmap;
    }

    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2f,
                (bgHeight +ScreenUtils.dpToPx(60)- fgHeight) / 2f, null);
        canvas.save();
        canvas.restore();
        return newmap;
    }

    /**
     * 把view转成图片
     *
     * @param view
     */
    public static Bitmap viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = viewConversionBitmap(view);


        view.destroyDrawingCache();

        return cachebmp;
    }


    /**
     * view转bitmap
     */
    public static Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        if (w <=0 || h<=0) {
            Log.e("TAG","View宽高为0");
            return null;
        }


        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }
}
