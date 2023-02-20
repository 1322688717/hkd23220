package cc.ixcc.novelthree.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.io.InputStream;

public class ArcImageView extends AppCompatImageView {
    /*
     *弧形高度
     */
    private int mArcHeight= 30;

    public ArcImageView(Context context) {
        this(context, null);
    }

    private Paint mPaint;
//    private Bitmap bitmap = readBitmapFromResource(getContext(),R.drawable.fore_like);
    public ArcImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getHeight()-mArcHeight);
        path.quadTo(getWidth() / 2, getHeight()+mArcHeight, getWidth(), getHeight() - mArcHeight);
        path.lineTo(getWidth(), 0);
        path.close();
        canvas.clipPath(path);
//        canvas.clipPath(path, Region.Op.DIFFERENCE);
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
//        canvas.drawBitmap(bitmap,0,0,mPaint);
        super.onDraw(canvas);
    }

    public Bitmap readBitmapFromResource(Context context, int resId) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.ARGB_8888;
        op.inDither = false;
        op.inScaled = false;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, op);
    }

}