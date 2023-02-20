package cc.ixcc.novelthree.widget.viewhoder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.BookMoreActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomHotAdapter;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;

import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.MORE_VISIBLE;

public class StackRoomModeHolder1 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;
    StackRoomHotAdapter mHotAdapter;
    @BindView(R.id.mode1_title)
    TextView mTitle;
    @BindView(R.id.mode1_more)
    TextView mMore;
    @BindView(R.id.rv_mode1)
    WrapRecyclerView mModeRv;
    @BindView(R.id.mode1_line)
    View mode1Line;
    StackRoomBookBean.ColumnBean mBean;

    public StackRoomModeHolder1(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode1;
    }

    @Override
    public void init() {
    }

    public void init2() {
        initHotRv();
        mMore.getPaint().setFakeBoldText(true);
        mTitle.setText(mTitleStr);
        mMore.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mMore.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }

    private void initHotRv() {
        if (mList.size() == 0) {
            return;
        }
        if (mList.size() > 5) {
            mList = mList.subList(0, 6);
        }
        mHotAdapter = new StackRoomHotAdapter(mContext);
        mModeRv.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,mList.get(position).getId());
            }
        });
        mModeRv.setAdapter(mHotAdapter);
        mModeRv.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mList);
    }
}
