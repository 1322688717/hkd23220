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
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomBookAdapter;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;

import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.MORE_VISIBLE;

public class StackRoomModeHolder5 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    StackRoomBookAdapter mHotAdapter;
    @BindView(R.id.current_title)
    TextView currentTitle;
    @BindView(R.id.current_more)
    TextView currentMore;
    @BindView(R.id.rv_current)
    WrapRecyclerView rvCurrent;
    StackRoomBookBean.ColumnBean mBean;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;

    public StackRoomModeHolder5(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode5;
    }

    @Override
    public void init() {
    }

    public void init2() {
        initHotRv();
        currentMore.getPaint().setFakeBoldText(true);
        currentTitle.setText(mTitleStr);
        currentMore.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        currentMore.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }

    private void initHotRv() {
        if (mList.size() == 0) {
            return;
        }
        if (mList.size() > 3) {
            mList = mList.subList(0, 4);
        }
        rvCurrent.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        mHotAdapter = new StackRoomBookAdapter(mContext, 1);
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,mList.get(position).getId());
            }
        });
        rvCurrent.setAdapter(mHotAdapter);
        rvCurrent.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mList);
    }
}
