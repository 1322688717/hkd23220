package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.BookPl;
import cc.ixcc.novelthree.common.MyAdapter;
import cc.ixcc.novelthree.utils.StarBar;

import butterknife.BindView;

public class BookDetailAdapter extends MyAdapter<BookPl, RecyclerView.ViewHolder> {
    public BookDetailAdapter(@NonNull Context context) {
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
        @BindView(R.id.book_leaner)
        LinearLayout bookLeaner;
        @BindView(R.id.dz_num)
        TextView dzNum;
        int dzNumber;

        ViewHolder() {
            super(R.layout.item_bookpl);
        }

        @Override
        public void onBindView(int position) {
            BookPl bean = getItem(position);
            //Glide.with(getContext()).load(bean.getImg()).into(iv_img);
            bookName.setText(bean.getName());
            bookContent.setText(bean.getContent());
            plTime.setText(bean.getCreateTime());
            dzNumber = Integer.parseInt(dzNum.getText().toString());
            //根据状态值来判断点赞的图片
//            if (bean.isDz()) {
////                unDz.setImageResource(R.mipmap.dz);
////                dzNumber++;
////                dzNum.setText(dzNumber + "");
////            } else {
////                unDz.setImageResource(R.mipmap.undz);
////                dzNumber--;
////                dzNum.setText(dzNumber + "");
////            }



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
    }
}
