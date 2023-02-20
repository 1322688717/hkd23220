package cc.ixcc.novelthree.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ClassificationPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> mTitle;
    private ArrayList<Fragment> mFragment;

    public ClassificationPagerAdapter(FragmentManager fm, ArrayList<String> mTitle, ArrayList<Fragment> mFragment) {
        super(fm);
        this.mTitle = mTitle;
        this.mFragment = mFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitle != null) {
            return mTitle.get(position);
        } else {
            return "";
        }
    }
}