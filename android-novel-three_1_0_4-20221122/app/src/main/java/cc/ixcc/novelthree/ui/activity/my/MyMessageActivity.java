package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.androidkun.xtablayout.XTabLayout;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.ui.adapter.MyPagerAdapter;
import cc.ixcc.novelthree.ui.fragment.myFragment.MyMessageFragment;
import cc.ixcc.novelthree.utils.RouteUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc   : 我的消息
 */
@Route(path = RouteUtil.PATH_MESSAGE)
public final class MyMessageActivity extends MyActivity
        implements
        KeyboardWatcher.SoftKeyboardStateListener {

    @BindView(R.id.tablayout)
    XTabLayout xTablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> fragments= new ArrayList<>();
    int index = 0;

    @DebugLog
    public static void start(Context context, int index) {
        Intent intent = new Intent(context, MyMessageActivity.class);
        intent.putExtra("index", index);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initView() {
        index = getIntent().getIntExtra("index",0);
        initViewPager();
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("Message");
        titles.add("Like");
        titles.add("Comment");
        fragments.add(MyMessageFragment.newInstance(Constants.CAT_TYPE_NOTICE, Constants.TYPE_NOTICE));
        fragments.add(MyMessageFragment.newInstance(Constants.CAT_TYPE_LIKE, Constants.TYPE_LIKE));
        fragments.add(MyMessageFragment.newInstance(Constants.CAT_TYPE_COMMENT, Constants.TYPE_COMMENT));
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), getContext(), fragments, titles));
        viewpager.setOffscreenPageLimit(titles.size());
        xTablayout.setupWithViewPager(viewpager);//此方法就是让tablayout和ViewPager联动
//        mTabLayout.setxTabDisplayNum(mtitle.size());
        xTablayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
        viewpager.setCurrentItem(index);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {

    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}