package cc.ixcc.novelthree.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;

public class StackLine1View extends RelativeLayout {

    public StackLine1View(Context context, StackRoomBookBean.ColumnBean.ListBean bean) {
        this(context, null, bean);
    }

    public StackLine1View(Context context, AttributeSet attrs, StackRoomBookBean.ColumnBean.ListBean bean) {
        this(context, attrs, 0, bean);
    }

    public StackLine1View(Context context, AttributeSet attrs, int defStyleAttr, StackRoomBookBean.ColumnBean.ListBean bean) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.item_stack_week, this, false);

    }
}
