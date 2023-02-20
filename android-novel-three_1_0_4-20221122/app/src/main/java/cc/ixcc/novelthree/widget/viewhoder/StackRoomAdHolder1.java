package cc.ixcc.novelthree.widget.viewhoder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;

public class StackRoomAdHolder1 extends AbsViewHolder {
    protected Context mContext;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.mode1_ad_title)
    TextView mode1AdTitle;
    @BindView(R.id.mode1_ad_content)
    TextView mode1AdContent;
    @BindView(R.id.mode1_ad_img)
    RoundedImageView mode1AdImg;
    @BindView(R.id.mode1_ad_type)
    TextView mode1AdType;
    @BindView(R.id.mode1_ad_download)
    TextView mode1AdDownload;
    @BindView(R.id.mode1_ad_delete)
    LinearLayout mode1AdDelete;
    StackRoomBookBean.ColumnBean mBean;

    public StackRoomAdHolder1(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mBean = bean;
        mContext = context;
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_ad1;
    }

    @Override
    public void init() {
    }

    public void init2() {
        layout.setVisibility(View.VISIBLE);
        mode1AdTitle.setText(mBean.getTitle());
        mode1AdContent.setText(mBean.getDescribe());
        Glide.with(mContext)
                .load(mBean.getThumb())
                .load(R.mipmap.book_cover_def)
                .into(mode1AdImg);
        layout.setOnClickListener(v -> {
            //Toast.makeText(mContext, "加载链接", Toast.LENGTH_SHORT).show();
        });
        mode1AdDelete.setOnClickListener(v -> {
            //Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
            layout.setVisibility(View.GONE);
        });
    }
}
