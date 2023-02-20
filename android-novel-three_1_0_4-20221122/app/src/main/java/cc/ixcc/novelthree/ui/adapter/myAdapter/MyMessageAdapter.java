package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.MyMessagebean;
import cc.ixcc.novelthree.ui.adapter.RefreshAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.ixcc.novelthree.Constants;

public final class MyMessageAdapter extends RefreshAdapter<MyMessagebean> {
    private OnClickListener mClickListener;
    private String type;

    public MyMessageAdapter(String type, Context context) {
        super(context);
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (type) {
            case Constants.TYPE_NOTICE:
                return new Notice(mInflater.inflate(R.layout.item_system_notice, parent, false));
            case Constants.TYPE_LIKE:
                return new Like(mInflater.inflate(R.layout.item_message_like, parent, false));
            case Constants.TYPE_COMMENT:
                return new Comment(mInflater.inflate(R.layout.item_message_comment, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        switch (type) {
            case Constants.TYPE_NOTICE:
                ((Notice) vh).setData(mList.get(position), position);
                break;
            case Constants.TYPE_LIKE:
                ((Like) vh).setData(mList.get(position), position);
                break;
            case Constants.TYPE_COMMENT:
                ((Comment) vh).setData(mList.get(position), position);
                break;
        }
    }

    final class Comment extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.vipImg)
        ImageView vipImg;
        @BindView(R.id.userImg)
        ImageView userImg;
        @BindView(R.id.top)
        RelativeLayout top;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.book)
        TextView book;
        @BindView(R.id.like)
        TextView like;
        @BindView(R.id.layout)
        RelativeLayout layout;

        public Comment(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(MyMessagebean bean, int position) {
            itemView.setTag(position);
            Glide.with(mContext)
                    .load(bean.getSend_avatar())
//                    .load(R.mipmap.book_img1)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImg);
            vipImg.setVisibility(TextUtils.equals(bean.getIs_vip(),"1")?View.VISIBLE:View.GONE);
            name.setText(bean.getSend_nickname());
            content.setText(bean.getSummary());
            time.setText(bean.getTime_detail());
            book.setText("from《" + bean.getRel_title() + "》");
            like.setText(bean.getContent().getLikes() + "");
            layout.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.ActionClick(type, position, bean);
            });
        }
    }

    final class Like extends RecyclerView.ViewHolder {

        @BindView(R.id.userImg)
        ImageView userImg;
        @BindView(R.id.vipImg)
        ImageView vipImg;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.info)
        TextView info;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.layout)
        RelativeLayout layout;

        public Like(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(MyMessagebean bean, int position) {
            itemView.setTag(position);
            Glide.with(mContext)
                    .load(bean.getSend_avatar())
//                    .load(R.mipmap.book_img1)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImg);
            vipImg.setVisibility(TextUtils.equals(bean.getIs_vip(),"1")?View.VISIBLE:View.GONE);
            name.setText(bean.getSend_nickname());
            info.setText(bean.getAt_alt());
            content.setText(bean.getContent().getLike_comment_content());
            time.setText(bean.getTime_detail());
            layout.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.ActionClick(type, position, bean);
            });
        }
    }

    final class Notice extends RecyclerView.ViewHolder {
        @BindView(R.id.red_point)
        ImageView redPoint;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.info)
        TextView info;
        @BindView(R.id.back)
        ImageView back;
        @BindView(R.id.layout)
        RelativeLayout layout;

        public Notice(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(MyMessagebean bean, int position) {
            itemView.setTag(position);
            title.setText(bean.getTitle());
            content.setText(bean.getSummary());
            time.setText(bean.getTime_detail());
            redPoint.setVisibility(bean.getRead_status().equals("0") ? View.VISIBLE : View.GONE);
            layout.setOnClickListener(v -> {
                if (mClickListener != null) mClickListener.ActionClick(type, position, bean);
            });
        }
    }

    public interface OnClickListener {
        void ActionClick(String type, int position, MyMessagebean bean);
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}