package cc.ixcc.novelthree.ui.dialog;

import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiusen.base.BaseDialog;
import com.jiusen.base.action.AnimAction;
import cc.ixcc.novelthree.R;

/**

 *    time   : 2020/10/19
 *    desc   : 合成对话框
 */
public final class SynthesizerDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        private final TextView tv_synthesizer;
        private final SeekBar seek_synthesizer;

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.dialog_update);
            setAnimStyle(AnimAction.BOTTOM);
            setCancelable(false);

            tv_synthesizer = findViewById(R.id.tv_synthesizer);
            seek_synthesizer = findViewById(R.id.seek_synthesizer);
        }

        public void SetProgress(int progress){
            seek_synthesizer.setProgress(progress);
            tv_synthesizer.setText("Synthesis progress："+progress+"%");
        }

    }
}