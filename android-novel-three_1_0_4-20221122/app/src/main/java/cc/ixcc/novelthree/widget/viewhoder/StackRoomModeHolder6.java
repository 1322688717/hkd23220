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
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomWeekAdapter;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomBookAdapter;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;

import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.MORE_VISIBLE;

public class StackRoomModeHolder6 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    StackRoomBookAdapter mHotAdapter;
    StackRoomWeekAdapter mHotAdapter2;
    @BindView(R.id.mode6_title)
    TextView mode6Title;
    @BindView(R.id.mode6_more)
    TextView mode6More;
    @BindView(R.id.rv_mode6_ancient1)
    WrapRecyclerView rvMode6Ancient1;
    @BindView(R.id.rv_mode6_ancient2)
    WrapRecyclerView rvMode6Ancient2;
    StackRoomBookBean.ColumnBean mBean;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;
    List<StackRoomBookBean.ColumnBean.ListBean> mNewList;
    boolean hasother = true;

    public StackRoomModeHolder6(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode6;
    }

    @Override
    public void init() {
    }

    public void init2() {
        initHotRv();
        initHotRv2();
        mode6More.getPaint().setFakeBoldText(true);
        mode6Title.setText(mTitleStr);
        mode6More.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mode6More.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }

    private void initHotRv() {
        if (mList.size() == 0) {
            hasother = false;
            return;
        }
        if (mList.size() > 2) {
            mNewList = mList.subList(0, 3);
            hasother = true;
        }
        else {
            mNewList = mList.subList(0, mList.size());
            hasother = false;
        }
        rvMode6Ancient1.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mHotAdapter = new StackRoomBookAdapter(mContext, 2);
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,mHotAdapter.getData().get(position).getId());
            }
        });
        rvMode6Ancient1.setAdapter(mHotAdapter);
        rvMode6Ancient1.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mNewList);
    }

    private void initHotRv2() {
        if (!hasother) return;
        if (mList.size() > 5) {
            mNewList = mList.subList(3, 6);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(3, mList.size());
        }
        rvMode6Ancient2.setLayoutManager(new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false));
        mHotAdapter2 = new StackRoomWeekAdapter(mContext);
        mHotAdapter2.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,mHotAdapter2.getData().get(position).getId());
            }
        });
        rvMode6Ancient2.setAdapter(mHotAdapter2);
        rvMode6Ancient2.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter2.setData(mNewList);
    }
}
