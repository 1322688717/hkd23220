package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.Read;

import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.MyViewHolder> {

    private Context context;
    private List<Read.HobbyBean> list;
    private View inflater;


    //先声明一个int成员变量
    private int thisPosition;
    //再定义一个int类型的返回值方法
    public int getthisPosition() {
        return thisPosition;
    }
    public void setThisPosition(int thisPosition) {
        this.thisPosition = thisPosition;
    }

    private onCallBackListener callBackListener;

    public void setCallBackListener(onCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public interface onCallBackListener {
        void callBack(int position);
    }


    public DialogAdapter(Context context, List<Read.HobbyBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.item_dialog, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //将数据和控件绑定
        // Glide.with(context).load(list.get(position)).into(holder.img);
        holder.userPhone.setText(list.get(position).getTitle().name);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackListener != null) {
                    callBackListener.callBack(position);
                }
            }
        });
        if (list.get(position).getIsselect().equals("1")) {
            holder.userPhone.setTextColor(Color.parseColor("#FFFFFF")); //还是利用Color类；
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_super));
        } else {
            holder.userPhone.setTextColor(Color.parseColor("#999999")); //还是利用Color类；
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_conner_green2));
        }
    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout youzhihaowu;
        //构造方法，传入数据

        RelativeLayout layout;
        TextView userPhone;

        public MyViewHolder(View itemView) {
            super(itemView);
//            order_linear =  itemView.findViewById(R.id.order_linear);
            layout = itemView.findViewById(R.id.layout);
            userPhone = itemView.findViewById(R.id.type);
        }
    }
}

