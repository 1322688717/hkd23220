package cc.ixcc.novelthree.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.VipHomeBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VipBenefitAdapter extends BaseQuickAdapter<VipHomeBean.VipPrivilegeBean, BaseViewHolder> {
    public VipBenefitAdapter(@Nullable List<VipHomeBean.VipPrivilegeBean> data){
        this(R.layout.item_benefit,data);
    }
    public VipBenefitAdapter(int layoutResId, @Nullable List<VipHomeBean.VipPrivilegeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VipHomeBean.VipPrivilegeBean vipPrivilegeBean) {
        baseViewHolder.setText(R.id.tvBenefit,vipPrivilegeBean.getTitle());
//        baseViewHolder.setText(R.id.tvBenefit,vipPrivilegeBean.getTitle());
        Glide.with(getContext()).load(vipPrivilegeBean.getImage()).into((ImageView) baseViewHolder.getView(R.id.ivBenefit));
    }
}
