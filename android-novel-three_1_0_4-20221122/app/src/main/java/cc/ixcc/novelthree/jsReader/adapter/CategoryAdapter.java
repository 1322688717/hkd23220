package cc.ixcc.novelthree.jsReader.adapter;

import android.view.View;
import android.view.ViewGroup;

import cc.ixcc.novelthree.jsReader.page.TxtChapter;

/**
 * Created by newbiechen on 17-6-5.
 */

public class CategoryAdapter extends EasyAdapter<TxtChapter> {


    private int currentSelected = 0;
    private boolean mIsVip = false;

    public CategoryAdapter(boolean isVip) {
        super();
        mIsVip = isVip;
    }

    @Override
    protected IViewHolder<TxtChapter> onCreateViewHolder(int viewType) {
        return new CategoryHolder(mIsVip);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        CategoryHolder holder = (CategoryHolder) view.getTag();

        if (position == currentSelected){
            holder.setSelectedChapter();
        }

        return view;
    }

    public void setChapter(int pos){
        currentSelected = pos;
        notifyDataSetChanged();
    }

    public int getChapter(){
        return currentSelected;
    }
}
