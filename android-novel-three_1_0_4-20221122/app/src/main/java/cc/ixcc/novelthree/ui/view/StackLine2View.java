package cc.ixcc.novelthree.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import cc.ixcc.novelthree.bean.StackRoomBookBean;

public class StackLine2View extends RelativeLayout {

    public StackLine2View(Context context, StackRoomBookBean.ColumnBean.ListBean bean) {
        this(context, null, bean);
    }

    public StackLine2View(Context context, AttributeSet attrs, StackRoomBookBean.ColumnBean.ListBean bean) {
        this(context, attrs, 0, bean);
    }

    public StackLine2View(Context context, AttributeSet attrs, int defStyleAttr, StackRoomBookBean.ColumnBean.ListBean bean) {
        super(context, attrs, defStyleAttr);
    }


}
