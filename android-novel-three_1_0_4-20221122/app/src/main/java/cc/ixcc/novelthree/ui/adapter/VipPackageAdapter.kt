package cc.ixcc.novelthree.ui.adapter

import android.graphics.Paint
import android.widget.TextView
import cc.ixcc.novelthree.ui.activity.my.vip.OpenVipActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import cc.ixcc.novelthree.R

class VipPackageAdapter(packageList: MutableList<cc.ixcc.novelthree.bean.VipHomeBean.PackageBean>) : BaseQuickAdapter<cc.ixcc.novelthree.bean.VipHomeBean.PackageBean, BaseViewHolder>(R.layout.item_vip_charge,packageList) {
    override fun convert(holder: BaseViewHolder, item: cc.ixcc.novelthree.bean.VipHomeBean.PackageBean) {
//        @+id/layout
//        @+id/ivBg
//        @+id/day
//        @+id/money
//        @+id/old_money
//        @+id/song
        holder.setText(R.id.day, "${item.day} days");
        holder.setText(R.id.money, OpenVipActivity.money_symbol+item.price);
        holder.setVisible(R.id.old_money, item.original_price.toFloat() > 0)
        holder.getView<TextView>(R.id.old_money).paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        holder.setText(R.id.old_money,  OpenVipActivity.money_symbol+item.original_price)
        holder.setVisible(R.id.song, item.sday > 0)
        holder.setText(R.id.song, "+ ${item.sday} days")
        holder.setImageResource(R.id.ivBg, if (item.isSelect) R.drawable.bg_pakage_select else R.drawable.bg_pakage_white)
    }
}