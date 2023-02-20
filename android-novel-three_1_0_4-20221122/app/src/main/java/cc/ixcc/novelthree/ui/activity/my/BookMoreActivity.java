package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.ui.adapter.MyPagerAdapter;
import cc.ixcc.novelthree.ui.fragment.myFragment.BookMoreFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc   : 书本更多
 */
public final class BookMoreActivity extends MyActivity
        implements
        KeyboardWatcher.SoftKeyboardStateListener {

    private static final String TAG = "BookMoreActivity";
    @BindView(R.id.title)
    TitleBar title;
    String titlestr = "";
    String id = "";
    @BindView(R.id.tablayout)
    XTabLayout xTablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> fragments= new ArrayList<>();

    @DebugLog
    public static void start(Context context, String title, String id) {
        Intent intent = new Intent(context, BookMoreActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        initViewPager();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_more;
    }

    @Override
    protected void initView() {
        titlestr = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        title.setTitle(titlestr);
        initViewPager();
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("");
//        titles.add("over");
//        titles.add("serialize");
        fragments.add(BookMoreFragment.newInstance(id, Constants.more_xinshu));
//        fragments.add(BookMoreFragment.newInstance(id, Constants.more_wanjie));
//        fragments.add(BookMoreFragment.newInstance(id, Constants.more_lianzai));
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