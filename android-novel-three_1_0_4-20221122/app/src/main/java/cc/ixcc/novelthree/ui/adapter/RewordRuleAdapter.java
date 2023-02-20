package cc.ixcc.novelthree.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.RewordBean;

import java.util.List;

public class RewordRuleAdapter extends RecyclerView.Adapter<RewordRuleAdapter.Vh>  {

    private Context mContext;
    private List<RewordBean> mRewordBeanList;
    private LayoutInflater mInflater;
    private ActionListener mActionListener;
    private View.OnClickListener mOnClickListener;
    private int curPosition=0;

    public RewordRuleAdapter(Context mContext, List<RewordBean> payRuleList) {
        this.mContext = mContext;
        this.mRewordBeanList=payRuleList;
        mInflater = LayoutInflater.from(mContext);
        this.notifyDataSetChanged();
        mOnClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object tag = view.getTag();
                if(tag!=null){
                    int position = (int) tag;
                    if (mActionListener != null) {
                        mActionListener.onItemClick(mRewordBeanList.get(position));
                    }
                    curPosition=position;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public void setActionListener(ActionListener listener) {
        mActionListener = listener;
    }

    @Override
    public RewordRuleAdapter.Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_reword, parent, false));
    }

    @Override
    public void onBindViewHolder(RewordRuleAdapter.Vh holder, int position) {
        holder.setData(mRewordBeanList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mRewordBeanList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView rewordTextView;
        LinearLayout ll_item;
        ImageView icon_lw;


        public Vh(View itemView) {
            super(itemView);
            rewordTextView=(TextView)itemView.findViewById(R.id.tv_reword);
            ll_item=(LinearLayout) itemView.findViewById(R.id.ll_item_reword);
            icon_lw =(ImageView) itemView.findViewById(R.id.icon_lw);
            itemView.setOnClickListener(mOnClickListener);
        }

        @SuppressLint("StringFormatMatches")
        void setData(RewordBean rewordBean, int position) {
            itemView.setTag(position);
            if(position == 0){
                icon_lw.setImageResource(R.mipmap.icon_dianzhan);
            }else if (position == 1){
                icon_lw.setImageResource(R.mipmap.icon_meigui);
            }else if (position == 2){
                icon_lw.setImageResource(R.mipmap.icon_coffee);
            }else if (position == 3){
                icon_lw.setImageResource(R.mipmap.icon_xiaoxiong);
            }else if (position == 4){
                icon_lw.setImageResource(R.mipmap.icon_huangguan);
            }else if (position == 5){
                icon_lw.setImageResource(R.mipmap.icon_bieshu);
            }
            rewordTextView.setText(rewordBean.getCoin() + "Coins");
            if(position==curPosition){
                ll_item.setBackgroundResource(R.drawable.bg_board_reword_item);
            }else {
                ll_item.setBackground(null);
            }
        }
    }


    public void refresh(List<RewordBean> newList) {
        mRewordBeanList.removeAll(mRewordBeanList);
        mRewordBeanList.addAll(newList);
        notifyDataSetChanged();
    }

    public void SetSelect(int position){
        mActionListener.onItemClick(mRewordBeanList.get(position));
    }


    public interface ActionListener {
        void onItemClick(RewordBean rewordBean);
    }
}
