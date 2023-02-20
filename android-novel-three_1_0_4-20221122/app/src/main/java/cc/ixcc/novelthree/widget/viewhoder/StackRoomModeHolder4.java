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
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;

import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.MORE_VISIBLE;

public class StackRoomModeHolder4 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    StackRoomWeekAdapter mHotAdapter;
    @BindView(R.id.mode4_title)
    TextView mode4Title;
    @BindView(R.id.mode4_more)
    TextView mode4More;
    @BindView(R.id.rv_mode4)
    WrapRecyclerView rvMode4;
    StackRoomBookBean.ColumnBean mBean;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;

    public StackRoomModeHolder4(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode4;
    }

    @Override
    public void init() {
    }

    public void init2() {
        initHotRv();
        mode4More.getPaint().setFakeBoldText(true);
        mode4Title.setText(mTitleStr);
        mode4More.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mode4More.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }

    private void initHotRv() {
        if (mList.size() == 0) {
            return;
        }
        if (mList.size() > 2) {
            mList = mList.subList(0, 3);
        }
        rvMode4.setLayoutManager(new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false));
        mHotAdapter = new StackRoomWeekAdapter(mContext);
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,mList.get(position).getId());
            }
        });
        rvMode4.setAdapter(mHotAdapter);
        rvMode4.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mList);
    }
}
