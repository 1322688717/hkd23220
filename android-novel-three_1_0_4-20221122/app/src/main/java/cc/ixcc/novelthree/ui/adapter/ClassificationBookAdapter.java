package cc.ixcc.novelthree.ui.adapter;

import cc.ixcc.novelthree.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import cc.ixcc.novelthree.bean.ClassficationBookBean;
import cc.ixcc.novelthree.utils.NumberUtils;

public class ClassificationBookAdapter extends BaseQuickAdapter<ClassficationBookBean, BaseViewHolder> {

    public ClassificationBookAdapter() {
        super(R.layout.classification_content_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ClassficationBookBean stackRoomItemBean) {
        Glide.with(getContext()).asBitmap().load(stackRoomItemBean.getCoverpic()).placeholder(R.mipmap.book_cover_def).diskCacheStrategy(DiskCacheStrategy.ALL).into((RoundedImageView) baseViewHolder.getView(R.id.book_img));
        baseViewHolder.setText(R.id.name, stackRoomItemBean.getTitle());
        baseViewHolder.setText(R.id.tt_full_desc, stackRoomItemBean.getDesc());
        baseViewHolder.setText(R.id.author, stackRoomItemBean.getAuthor());
        baseViewHolder.setVisible(R.id.hot, stackRoomItemBean.getHots() != 0);
        baseViewHolder.setText(R.id.hot, NumberUtils.amountConversion(stackRoomItemBean.getHots()));
        baseViewHolder.setVisible(R.id.tvVip, stackRoomItemBean.getIsvip() == 1);
    }
}