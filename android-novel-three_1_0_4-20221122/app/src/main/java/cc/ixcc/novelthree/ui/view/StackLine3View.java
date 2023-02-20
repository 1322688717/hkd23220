package cc.ixcc.novelthree.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import cc.ixcc.novelthree.bean.StackRoomBookBean;

public class StackLine3View extends RelativeLayout {

    public StackLine3View(Context context, StackRoomBookBean.ColumnBean.ListBean bean) {
        this(context, null, bean);
    }

    public StackLine3View(Context context, AttributeSet attrs, StackRoomBookBean.ColumnBean.ListBean bean) {
        this(context, attrs, 0, bean);
    }

    public StackLine3View(Context context, AttributeSet attrs, int defStyleAttr, StackRoomBookBean.ColumnBean.ListBean bean) {
        super(context, attrs, defStyleAttr);
    }


}
