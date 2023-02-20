package cc.ixcc.novelthree.jsReader.adapter;

import android.view.View;
import android.view.ViewGroup;

import cc.ixcc.novelthree.jsReader.model.bean.BookChapterBean;

/**
 * Created by newbiechen on 17-6-5.
 */

public class ListenCategoryAdapter extends EasyAdapter<BookChapterBean> {


    private int currentSelected = 0;
    private boolean mIsVip = false;

    public ListenCategoryAdapter(boolean isVip) {
        super();
        mIsVip = isVip;
    }

    @Override
    protected IViewHolder<BookChapterBean> onCreateViewHolder(int viewType) {
        return new ListenCategoryHolder(mIsVip);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ListenCategoryHolder holder = (ListenCategoryHolder) view.getTag();

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
