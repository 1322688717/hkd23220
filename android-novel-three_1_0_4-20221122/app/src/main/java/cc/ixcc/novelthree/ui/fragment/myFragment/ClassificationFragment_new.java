package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import cc.ixcc.novelthree.R;

import java.util.ArrayList;

import butterknife.BindView;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.adapter.ClassificationPagerAdapter;

public class ClassificationFragment_new extends MyFragment<HomeActivity> {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mStrings = new ArrayList<>();

    public static ClassificationFragment_new

    newInstance() {
        ClassificationFragment_new fragment = new ClassificationFragment_new();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classification_new;
    }

    @Override
    protected void initView() {
        mStrings.add(getString(R.string.classification_man));
        mFragmentList.add(ClassificationDataFragment_new.newInstance(0));
        try {
            mViewPager.setAdapter(new ClassificationPagerAdapter(getChildFragmentManager(), mStrings, mFragmentList));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.mHomeActivity.GotoLastfrag();
                }
            });


        }
        catch (Exception ex)
        {
            Log.e("ClassificationFragment", ex.toString());
        }
    }

    @Override
    protected void initData() {

    }

    public void RefreshView(String str) {
        if (!str.equals("")) {
            title_name.setText(str);
        }
    }

    public void SetSelect_ClassificationTitleBean(int position) {

        ClassificationDataFragment_new mClassificationDataFragment_new = (ClassificationDataFragment_new) mFragmentList.get(0);
        mClassificationDataFragment_new.SetSelect_ClassificationTitleBean(position);

    }

}