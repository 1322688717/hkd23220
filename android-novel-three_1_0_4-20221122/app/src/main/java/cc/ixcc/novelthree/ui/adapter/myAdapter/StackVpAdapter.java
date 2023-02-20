package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackVp;

import java.util.ArrayList;
import java.util.List;

/**
 * Author  : MinKin.
 * Date    : 2019/10/31
 * Version : 1.0
 * Desc    : 右测适配器
 */
public class StackVpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * Item类型
     */
    public static final int TITLE = 1;
    public static final int CONTENT = 2;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<StackVp> mDatas;

    public StackVpAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void addData(List<StackVp> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<StackVp> getDatas() {
        return mDatas;
    }

    @Override
    public int getItemViewType(int position) {
        StackVp rightVo = mDatas.get(position);
        return rightVo.getItemType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
           /* case StackVpAdapter.TITLE:
                return new TitleViewHolder(mInflater.inflate(R.layout.item_right_title, parent, false));
            case StackVpAdapter.CONTENT:
                return new ContentViewHolder(mInflater.inflate(R.layout.item_stack, parent, false));*/
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StackVp rightVo = mDatas.get(position);
        switch (getItemViewType(position)) {
            case StackVpAdapter.TITLE:
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                titleViewHolder.tvRightTitle.setText(rightVo.getTitle());
                titleViewHolder.tvRightMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
            case StackVpAdapter.CONTENT:
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.tvRightContent.setText(rightVo.getTitle());
                contentViewHolder.ivRightContent.setText(rightVo.getTitle());
               /* Glide.with(mContext)
                        .load(rightVo.getImage())
                        .circleCrop()
                        .into(contentViewHolder.ivRightContent);*/
                contentViewHolder.ivRightContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Intent intent = new Intent(mContext, ShopActivity.class);
                        intent.putExtra("title",rightVo.getTitle());
                        intent.putExtra("itemId",rightVo.getId()+"");
                        mContext.startActivity(intent);*/
                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        return mDatas.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (mDatas.get(position).getItemType()) {
                        case StackVpAdapter.TITLE:
                            return 3;                //Title占3份
                        case StackVpAdapter.CONTENT:
                            return 1;                //Content占1份
                    }
                    return 1;
                }
            });
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView tvRightTitle,tvRightMore;

        TitleViewHolder(@NonNull View itemView) {
            super(itemView);
          /*  tvRightTitle = itemView.findViewById(R.id.tv_right_title);
            tvRightMore = itemView.findViewById(R.id.tv_right_more);*/
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView tvRightContent;
        TextView ivRightContent;

        ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRightContent = itemView.findViewById(R.id.book_name);
            ivRightContent = itemView.findViewById(R.id.book_status);
        }
    }


}
