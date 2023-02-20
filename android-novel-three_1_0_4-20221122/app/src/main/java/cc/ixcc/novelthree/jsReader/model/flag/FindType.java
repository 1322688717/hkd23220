package cc.ixcc.novelthree.jsReader.model.flag;


import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.treader.AppContext;

/**
 * Created by newbiechen on 17-4-25.
 */

public enum FindType {
    TOP(R.string.nb_fragment_find_top,R.drawable.ic_section_top),
    TOPIC(R.string.nb_fragment_find_topic,R.drawable.ic_section_topic),
    SORT(R.string.nb_fragment_find_sort,R.drawable.ic_section_sort),
    LISTEN(R.string.nb_fragment_find_listen,R.drawable.ic_section_listen);
    ;
    private String typeName;
    private int iconId;

    private FindType(@StringRes int typeNameId, @DrawableRes int iconId){
        this.typeName = AppContext.sInstance.getResources().getString(typeNameId);
        this.iconId = iconId;
    }

    public String getTypeName(){
        return typeName;
    }

    public int getIconId(){
        return iconId;
    }
}
