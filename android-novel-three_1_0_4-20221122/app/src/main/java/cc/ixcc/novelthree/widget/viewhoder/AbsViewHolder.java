package cc.ixcc.novelthree.widget.viewhoder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import butterknife.ButterKnife;

public abstract class AbsViewHolder implements LifeCycleListener {
    private String mTag;
    protected Context mContext;
    protected ViewGroup mParentView;
    protected View mContentView;

    public AbsViewHolder(Context context, ViewGroup parentView) {
        Log.e(mTag, "当前的AbsViewHolder路径=" + getClass().getName());
        mTag = getClass().getSimpleName();
        mContext = context;
        mParentView = parentView;
        mContentView = LayoutInflater.from(context).inflate(getLayoutId(), mParentView, false);
        init();
        ButterKnife.bind(this,mContentView);
    }

    public AbsViewHolder(Context context, ViewGroup parentView, Object... args) {
        Log.e(mTag, "当前的AbsViewHolder路径=" + getClass().getName());
        mTag = getClass().getSimpleName();
        processArguments(args);
        mContext = context;
        mParentView = parentView;
        mContentView = LayoutInflater.from(context).inflate(getLayoutId(), mParentView, false);
        init();
        ButterKnife.bind(this,mContentView);
    }

    protected void processArguments(Object... args) {

    }

    protected abstract int getLayoutId();

    public abstract void init();

    protected <T extends View> T findViewById(int res) {
        return mContentView.findViewById(res);
    }

    public View getContentView() {
        return mContentView;
    }

    public void addToParent() {
        if (mParentView != null && mContentView != null) {
            mParentView.addView(mContentView);
        }
    }

    public void removeFromParent() {
        ViewParent parent = mContentView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mContentView);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        Log.e(mTag, "release-------->");
    }

    @Override
    public void onCreate() {
//        Log.e(mTag, "lifeCycle-----onCreate----->");
    }

    @Override
    public void onStart() {
//        Log.e(mTag, "lifeCycle-----onStart----->");
    }

    @Override
    public void onReStart() {
        Log.e(mTag, "lifeCycle-----onReStart----->");
    }

    @Override
    public void onResume() {
        Log.e(mTag, "lifeCycle-----onResume----->");
    }

    @Override
    public void onPause() {
        Log.e(mTag, "lifeCycle-----onPause----->");
    }

    @Override
    public void onStop() {
        Log.e(mTag, "lifeCycle-----onStop----->");
    }

    @Override
    public void onDestroy() {
        Log.e(mTag, "lifeCycle-----onDestroy----->");
    }

}
