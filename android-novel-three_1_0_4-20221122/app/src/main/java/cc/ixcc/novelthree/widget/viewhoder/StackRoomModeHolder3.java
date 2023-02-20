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

public class StackRoomModeHolder3 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    StackRoomBookAdapter mHotAdapter;
    @BindView(R.id.mode3_title)
    TextView mode3Title;
    @BindView(R.id.mode3_more)
    TextView mode3More;
    @BindView(R.id.rv_mode3)
    WrapRecyclerView rvMode3;
    StackRoomBookBean.ColumnBean mBean;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;

    public StackRoomModeHolder3(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode3;
    }

    @Override
    public void init() {
    }

    public void init2() {
        initHotRv();
        mode3More.getPaint().setFakeBoldText(true);
        mode3Title.setText(mTitleStr);
        mode3More.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mode3More.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }

    private void initHotRv() {
        if (mList.size() == 0) {
            return;
        }
        if (mList.size() > 5) {
            mList = mList.subList(0, 6);
        }
        rvMode3.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mHotAdapter = new StackRoomBookAdapter(mContext, 2);
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,mList.get(position).getId());
            }
        });
        rvMode3.setAdapter(mHotAdapter);
        rvMode3.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mList);
    }
}
