package cc.ixcc.novelthree.ui.adapter.myAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Admin on 2018/6/19.
 */

public class TabViewpagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments=null;
    private List<String> title;

    public TabViewpagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> title) {
        super(fm);
        this.fragments=fragments;
        this.title=title;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=fragments.get(position);
       return fragment;
    }

}
