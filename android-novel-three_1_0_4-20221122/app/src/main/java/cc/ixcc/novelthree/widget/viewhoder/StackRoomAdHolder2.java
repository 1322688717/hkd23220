package cc.ixcc.novelthree.widget.viewhoder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;

public class StackRoomAdHolder2 extends AbsViewHolder {
    protected Context mContext;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.mode2_title)
    TextView mode2Title;
    @BindView(R.id.mode2_img)
    RoundedImageView mode2Img;
    @BindView(R.id.mode2_type)
    TextView mode2Type;
    @BindView(R.id.mode2_download)
    TextView mode2Download;
    @BindView(R.id.mode2_delete)
    LinearLayout mode2Delete;
    StackRoomBookBean.ColumnBean mBean;

    public StackRoomAdHolder2(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mBean = bean;
        mContext = context;
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_ad2;
    }

    @Override
    public void init() {
    }

    public void init2() {
        mode2Title.setText(mBean.getTitle());
        Glide.with(mContext)
                    .load(mBean.getThumb())
//                .load(R.mipmap.book_img1)
                .into(mode2Img);
        layout.setOnClickListener(v -> Toast.makeText(mContext, "加载链接", Toast.LENGTH_SHORT).show());
        mode2Delete.setOnClickListener(v -> Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show());
    }
}
