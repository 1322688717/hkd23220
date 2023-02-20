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

public class StackRoomModeHolder8 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    StackRoomWeekAdapter mHotAdapter;
    StackRoomBookAdapter mHotAdapter2;
    StackRoomWeekAdapter mHotAdapter3;
    StackRoomBookAdapter mHotAdapter4;
    @BindView(R.id.mode8_title)
    TextView mode8Title;
    @BindView(R.id.mode8_more)
    TextView mode8More;
    @BindView(R.id.rv_mode8_1)
    WrapRecyclerView rvMode81;
    @BindView(R.id.rv_mode8_2)
    WrapRecyclerView rvMode82;
    @BindView(R.id.rv_mode8_3)
    WrapRecyclerView rvMode83;
    @BindView(R.id.rv_mode8_4)
    WrapRecyclerView rvMode84;
    StackRoomBookBean.ColumnBean mBean;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;
    List<StackRoomBookBean.ColumnBean.ListBean> mNewList;
    boolean hasother = true;

    public StackRoomModeHolder8(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode8;
    }

    @Override
    public void init() {
    }

    public void init2() {
        initHotRv();
        initHotRv2();
        initHotRv3();
        initHotRv4();
        mode8More.getPaint().setFakeBoldText(true);
        mode8Title.setText(mTitleStr);
        mode8More.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mode8More.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }

    private void initHotRv() {
        if (mList.size() == 0) {
            hasother = false;
            return;
        }
        if (mList.size() > 0) {
            mNewList = mList.subList(0, 1);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(0, mNewList.size());
        }
        rvMode81.setLayoutManager(new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false));
        mHotAdapter = new StackRoomWeekAdapter(mContext);
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext, mHotAdapter.getData().get(position).getId());
            }
        });
        rvMode81.setAdapter(mHotAdapter);
        rvMode81.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mNewList);
    }

    private void initHotRv2() {
        if (!hasother) return;
        if (mList.size() > 4) {
            mNewList = mList.subList(1, 5);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(1, mList.size());
        }
        rvMode82.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        mHotAdapter2 = new StackRoomBookAdapter(mContext, 1);
        mHotAdapter2.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext, mHotAdapter2.getData().get(position).getId());
            }
        });
        rvMode82.setAdapter(mHotAdapter2);
        rvMode82.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter2.setData(mNewList);
    }

    private void initHotRv3() {
        if (!hasother) return;
        if (mList.size() > 5) {
            mNewList = mList.subList(5, 6);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(5, mList.size());
        }
        rvMode83.setLayoutManager(new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false));
        mHotAdapter3 = new StackRoomWeekAdapter(mContext);
        mHotAdapter3.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext, mHotAdapter3.getData().get(position).getId());
            }
        });
        rvMode83.setAdapter(mHotAdapter3);
        rvMode83.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter3.setData(mNewList);
    }

    private void initHotRv4() {
        if (!hasother) return;
        if (mList.size() > 9) {
            mNewList = mList.subList(6, 10);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(6, mList.size());
        }
        rvMode84.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        mHotAdapter4 = new StackRoomBookAdapter(mContext, 1);
        mHotAdapter4.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext, mHotAdapter4.getData().get(position).getId());
            }
        });
        rvMode84.setAdapter(mHotAdapter4);
        rvMode84.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter4.setData(mNewList);
    }
}
