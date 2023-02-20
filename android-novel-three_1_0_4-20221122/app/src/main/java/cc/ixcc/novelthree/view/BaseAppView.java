package cc.ixcc.novelthree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BaseAppView extends FrameLayout {

    private ViewVisibilityCallback callback;

    public BaseAppView(@NonNull Context context) {
        this(context, null);
    }

    public BaseAppView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseAppView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onBaseInit();
    }

    protected void onBaseInit() {

    }

    /**
     * 设置布局
     *
     * @param layoutId 布局id
     */
    public void setContentView(int layoutId) {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(layoutId, this, true);
    }

    public void setContentView(View contentView) {
        removeAllViews();
        addView(contentView);
    }

    public void setContentView(View contentView, ViewGroup.LayoutParams params) {
        removeAllViews();
        addView(contentView, params);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (callback != null) {
            callback.onViewVisibilityChanged(this, visibility);
        }
    }

    public void setVisibilityCallback(ViewVisibilityCallback callback) {
        this.callback = callback;
    }

    public interface ViewVisibilityCallback {
        void onViewVisibilityChanged(View view, int visibility);
    }
}
