package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import cc.ixcc.novelthree.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.adapter.ClassificationPagerAdapter;

public class ClassificationFragment extends MyFragment<HomeActivity> {
    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mStrings = new ArrayList<>();

    public static ClassificationFragment newInstance() {
        ClassificationFragment fragment = new ClassificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classification;
    }

    @Override
    protected void initView() {
        mStrings.add(getString(R.string.classification_man));
        mStrings.add(getString(R.string.classification_woman));
        mFragmentList.add(ClassificationDataFragment.newInstance(1));
        mFragmentList.add(ClassificationDataFragment.newInstance(0));
        try {
            mViewPager.setAdapter(new ClassificationPagerAdapter(getFragmentManager(), mStrings, mFragmentList));
            mTablayout.setupWithViewPager(mViewPager);
        }
        catch (Exception ex)
        {
            Log.e("ClassificationFragment", ex.toString());
        }
    }

    @Override
    protected void initData() {

    }
}