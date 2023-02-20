package cc.ixcc.novelthree.custom;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import cc.ixcc.novelthree.bean.ActiviyNoticeInfo;


import com.youth.banner.loader.ImageLoader;

public class ShelveBannerLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(6);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        //Glide.with(mContext).load(list.get(position).getThumb()).apply(options).into(holder.shopImg);
//            Glide.with(context).load((String) path).apply(options).into(imageView);
//            Glide.with(context).load(((StackRoomBookBean.AdBean) path).getImage()).apply(options).into(imageView);
        Glide.with(context).load(((ActiviyNoticeInfo) path).getUrl()).into(imageView);

    }
}