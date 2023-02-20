package cc.ixcc.novelthree.widget.viewhoder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.BookMoreActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomBookAdapter;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import cc.ixcc.novelthree.utils.WordUtil;

import static cc.ixcc.novelthree.Constants.MORE_VISIBLE;
import static cc.ixcc.novelthree.utils.WordUtil.getString;

public class StackRoomModeHolder2 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;
    StackRoomBookAdapter mHotAdapter;
    @BindView(R.id.mode2_title)
    TextView mode2Title;
    @BindView(R.id.mode2_more)
    TextView mode2More;
    @BindView(R.id.mode2_img)
    RoundedImageView mode2Img;
    @BindView(R.id.mode2_name)
    TextView mode2Name;
    @BindView(R.id.mode2_content)
    TextView mode2Content;
    @BindView(R.id.mode2_pingfen)
    TextView mode2Pingfen;
    @BindView(R.id.mode2_author)
    TextView mode2Author;
    @BindView(R.id.mode2_type)
    TextView mode2Type;
    @BindView(R.id.mode2_lianzai)
    TextView mode2Lianzai;
    @BindView(R.id.mode2_week)
    WrapRecyclerView mode2Week;
    StackRoomBookBean.ColumnBean mBean;
    @BindView(R.id.book)
    LinearLayout book;
    @BindView(R.id.fen)
    TextView fen;

    public StackRoomModeHolder2(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode2;
    }

    @Override
    public void init() {
    }

    public void init2() {
        if (mList.size() > 0) {
            StackRoomBookBean.ColumnBean.ListBean bean = mList.get(0);
            Glide.with(mContext)
                    .load(bean.getCoverpic())
//                    .load(R.mipmap.book_img1)
                    .into(mode2Img);
            mode2Name.setText(bean.getTitle());
            mode2Type.setText(bean.getClassify().name);
            mode2Type.setVisibility(TextUtils.isEmpty(bean.getClassify().name) ? View.GONE : View.VISIBLE);
            mode2Pingfen.setText(bean.getAverage_score() + "");
            mode2Pingfen.setVisibility(bean.getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            fen.setVisibility(bean.getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            mode2Author.setText(bean.getAuthor());
            mode2Content.setText(bean.getDesc());
            mode2Lianzai.setText(bean.getIswz() == Constants.lianzai ? WordUtil.getString(R.string.lianzai) : WordUtil.getString(R.string.wanjie));
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.start(mContext, bean.getId());
                }
            });
        }
        initHotRv();
        mode2More.getPaint().setFakeBoldText(true);
        mode2Title.setText(mTitleStr);
        mode2More.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mode2More.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }

    private void initHotRv() {
        if (mList.size() == 0 || mList.size() == 1) {
            return;
        }
        if (mList.size() > 4) {
            mList = mList.subList(1, 5);
        } else {
            mList = mList.subList(1, mList.size());
        }
        mode2Week.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        mHotAdapter = new StackRoomBookAdapter(mContext, 1);
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext, mList.get(position).getAnid());
            }
        });
        mode2Week.setAdapter(mHotAdapter);
        mode2Week.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mList);
    }
}
