package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.CommentBean;
import cc.ixcc.novelthree.common.MyAdapter;
import cc.ixcc.novelthree.utils.StarBar;

import butterknife.BindView;

public class BookPlListAdapter extends MyAdapter<CommentBean.ListBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;

    public BookPlListAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {
        @BindView(R.id.pl_userImg)
        ImageView plUserImg;
        @BindView(R.id.book_name)
        TextView bookName;
        @BindView(R.id.starBar)
        StarBar starBar;
        @BindView(R.id.book_content)
        TextView bookContent;
        @BindView(R.id.pl_time)
        TextView plTime;
        @BindView(R.id.un_dz)
        ImageView unDz;
        @BindView(R.id.vip_img)
        ImageView vipImg;
        @BindView(R.id.book_leaner)
        LinearLayout bookLeaner;
        @BindView(R.id.dz_num)
        TextView dzNum;
        int dzNumber;
        @BindView(R.id.pl_status)
        TextView plStatus;
        @BindView(R.id.dz_imgNum)
        LinearLayout dzImgNum;

        ViewHolder() {
            super(R.layout.item_bookpllist);
        }

        @Override
        public void onBindView(int position) {
            CommentBean.ListBean bean = getItem(position);
            Glide.with(getContext()).load(bean.getAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .into(plUserImg);
            if (TextUtils.equals(bean.getIs_vip(),"1")) {
                vipImg.setVisibility(View.VISIBLE);
                vipImg.setImageResource(R.mipmap.mine_vip_sign);
            } else {
                vipImg.setVisibility(View.GONE);
            }

            bookName.setText(bean.getNickname());
            bookContent.setText(bean.getContent());
            plTime.setText(bean.getUpdated_at());
            dzNum.setText(bean.getLikes() + "");
            //根据状态值来判断点赞的图片
            if (bean.getIs_admire().equals(Constants.LIKE)) {
                unDz.setImageResource(R.mipmap.dz);
                dzNum.setTextColor(getColor(R.color.dz));
            } else {
                unDz.setImageResource(R.mipmap.dz_default);
                dzNum.setTextColor(getColor(R.color.textColorHint));
            }
            bookLeaner.setOnClickListener(view -> {
                if (mClickListener != null) mClickListener.onActionClick(bean);
            });
            dzImgNum.setOnClickListener(view -> {
                if (mClickListener != null) mClickListener.onLikeClick(bean);
            });
            starBar.setStarMark(bean.getStar());
            getScore();
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!DoubleClickHelper.isOnDoubleClick()) {
                        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                        intent.putExtra("fansBean", bean);
                        intent.putExtra("type", 2);//团队订单
                        startActivity(intent);
                    }
                }
            });*/
        }

        private void getScore() {
            int starBarNum = (int) starBar.getStarMark();
            if (starBarNum == 1) {
                plStatus.setText("2.0");
            } else if (starBarNum == 2) {
                plStatus.setText("4.0");
            } else if (starBarNum == 3) {
                plStatus.setText("6.0");
            } else if (starBarNum == 4) {
                plStatus.setText("8.0");
            } else if (starBarNum == 5) {
                plStatus.setText("10.0");
            }
        }
    }

    public interface OnClickListener {
        void onActionClick(CommentBean.ListBean bean);

        void onLikeClick(CommentBean.ListBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mClickListener = lickListener;
    }
}
