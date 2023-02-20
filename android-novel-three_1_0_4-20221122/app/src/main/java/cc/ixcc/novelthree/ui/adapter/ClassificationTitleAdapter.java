package cc.ixcc.novelthree.ui.adapter;

import android.widget.TextView;

import cc.ixcc.novelthree.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import cc.ixcc.novelthree.bean.ClassificationTitleBean;

public class ClassificationTitleAdapter extends BaseQuickAdapter<ClassificationTitleBean, BaseViewHolder> {

    public ClassificationTitleAdapter() {
        super(R.layout.classification_title_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ClassificationTitleBean classificationTitleBean) {

        TextView mTitle = baseViewHolder.getView(R.id.tv_title);
        mTitle.setText(classificationTitleBean.getNovel_name());

        mTitle.setBackground(classificationTitleBean.isSelect() ? getContext().getResources().getDrawable(R.drawable.bg_tag_title_selected) :
                getContext().getResources().getDrawable(R.drawable.bg_tag_title));
        mTitle.setTextColor(classificationTitleBean.isSelect() ? getContext().getResources().getColor(R.color.tagSelected) :
                getContext().getResources().getColor(R.color.tagSelect));

    }
}
