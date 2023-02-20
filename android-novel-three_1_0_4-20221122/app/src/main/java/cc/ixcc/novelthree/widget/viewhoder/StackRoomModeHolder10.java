package cc.ixcc.novelthree.widget.viewhoder;

import static cc.ixcc.novelthree.Constants.MORE_VISIBLE;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cc.ixcc.novelthree.R;

import java.util.List;

import butterknife.BindView;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.ui.activity.my.BookMoreActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomBookAdapter;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomWeekAdapter;

public class StackRoomModeHolder10 extends AbsViewHolder {

    protected Context mContext;
    String mTitleStr;
    StackRoomWeekAdapter mHotAdapter;
    StackRoomBookAdapter mHotAdapter2;
    StackRoomWeekAdapter mHotAdapter3;
    StackRoomBookAdapter mHotAdapter4;
    @BindView(R.id.mode10_title)
    TextView mode10Title;
    @BindView(R.id.mode10_more)
    TextView mode10More;

    StackRoomBookBean.ColumnBean mBean;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;
    List<StackRoomBookBean.ColumnBean.ListBean> mNewList;
    boolean hasother = true;
    public StackRoomModeHolder10(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode10;
    }

    @Override
    public void init() {

    }
    public void init2() {

        mode10More.getPaint().setFakeBoldText(true);
        mode10Title.setText(mTitleStr);
        mode10More.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mode10More.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));
    }





}
