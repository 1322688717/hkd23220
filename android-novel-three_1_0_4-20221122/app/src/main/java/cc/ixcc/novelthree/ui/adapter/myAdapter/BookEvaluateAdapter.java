package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import cc.ixcc.novelthree.bean.BookDetailBean;
import cc.ixcc.novelthree.common.MyAdapter;
import cc.ixcc.novelthree.utils.StarBar;

import butterknife.BindView;

public class BookEvaluateAdapter extends MyAdapter<BookDetailBean.CommentsBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;

    public BookEvaluateAdapter(@NonNull Context context) {
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
        @BindView(R.id.vipImg)
        ImageView vipImg;
        @BindView(R.id.book_name)
        TextView bookName;
        @BindView(R.id.starBar)
        StarBar starBar;
        @BindView(R.id.book_content)
        TextView bookContent;
        @BindView(R.id.pl_time)
        TextView plTime;
        @BindView(R.id.dz_num)
        TextView dzNum;
        @BindView(R.id.dz_imgNum)
        LinearLayout dzImgNum;

        ViewHolder() {
            super(R.layout.item_bookpl);
        }

        @Override
        public void onBindView(int position) {
            BookDetailBean.CommentsBean bean = getItem(position);

            String avaterStr = bean.getAvatar();
            Glide.with(getContext()).load(avaterStr).apply(RequestOptions.circleCropTransform()).into(plUserImg);
            bookName.setText(bean.getNickname());
            bookContent.setText(bean.getContent());
            plTime.setText(bean.getUpdated_at());
            dzNum.setText(bean.getLikes() + "");
            vipImg.setVisibility(TextUtils.equals(bean.getIs_vip(),"1")? View.VISIBLE:View.GONE);
            setDrawable(dzNum, bean.getIs_admire().equals(Constants.LIKE) ? getDrawable(R.mipmap.dz) : getDrawable(R.mipmap.dz_default));
            dzNum.setTextColor(bean.getIs_admire().equals(Constants.LIKE) ? getColor(R.color.dz) : getColor(R.color.textColorHint));
            //设置星星为整数评分
            starBar.setIntegerMark(true);
            //设置显示的评分等级  默认为1
            starBar.setStarMark(bean.getStar());
            dzImgNum.setOnClickListener(view -> {
                if (mClickListener != null) {
                    mClickListener.onLikeClick(bean);
                }
            });
        }
    }

    private Drawable setDrawable(TextView textView, Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        return drawable;
    }

    public interface OnClickListener {
        void onActionClick(BookDetailBean.CommentsBean bean);

        void onLikeClick(BookDetailBean.CommentsBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mClickListener = lickListener;
    }
}
