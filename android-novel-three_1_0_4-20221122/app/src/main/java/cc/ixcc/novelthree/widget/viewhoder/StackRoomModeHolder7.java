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
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomSuggestAdapter;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomBookAdapter;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;

import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.MORE_VISIBLE;

public class StackRoomModeHolder7 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    StackRoomSuggestAdapter mHotAdapter;
    StackRoomBookAdapter mHotAdapter2;
    @BindView(R.id.mode7_title)
    TextView mode7Title;
    @BindView(R.id.mode7_more)
    TextView mode7More;
    @BindView(R.id.rv_mode7_belong1)
    WrapRecyclerView rvMode7Belong1;
    @BindView(R.id.rv_mode7_belong2)
    WrapRecyclerView rvMode7Belong2;
    StackRoomBookBean.ColumnBean mBean;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;
    List<StackRoomBookBean.ColumnBean.ListBean> mNewList;
    boolean hasother = true;

    public StackRoomModeHolder7(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode7;
    }

    @Override
    public void init() {
    }

    public void init2() {
        initHotRv();
        initHotRv2();
        mode7More.getPaint().setFakeBoldText(true);
        mode7Title.setText(mTitleStr);
        mode7More.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mode7More.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }

    private void initHotRv() {
//        if (!hasother) return;
        if (mList.size() == 0) {
            hasother = false;
            return;
        }
        if (mList.size() > 1) {
            mNewList = mList.subList(0, 2);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(0, mList.size());
        }
        rvMode7Belong1.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mHotAdapter = new StackRoomSuggestAdapter(mContext);
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,mHotAdapter.getData().get(position).getId());
            }
        });
        rvMode7Belong1.setAdapter(mHotAdapter);
        rvMode7Belong1.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mNewList);
    }

    private void initHotRv2() {
        if (!hasother) return;
        if (mList.size() > 5) {
            mNewList = mList.subList(2, 6);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(2, mList.size());
        }
        rvMode7Belong2.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        mHotAdapter2 = new StackRoomBookAdapter(mContext,1);
        mHotAdapter2.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,mHotAdapter2.getData().get(position).getId());
            }
        });
        rvMode7Belong2.setAdapter(mHotAdapter2);
        rvMode7Belong2.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter2.setData(mNewList);
    }
}
