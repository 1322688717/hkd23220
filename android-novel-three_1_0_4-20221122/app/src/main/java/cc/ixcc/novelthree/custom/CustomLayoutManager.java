package cc.ixcc.novelthree.custom;

import android.content.Context;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

public class CustomLayoutManager extends FlexboxLayoutManager {
    public CustomLayoutManager(Context context) {
        this(context, FlexDirection.ROW, FlexWrap.WRAP);
    }

    public CustomLayoutManager(Context context, int flexDirection) {
        this(context,  flexDirection, FlexWrap.WRAP);
    }

    public CustomLayoutManager(Context context, int flexDirection, int flexWrap) {
        super(context, flexDirection, flexWrap);
    }
}
