package cc.ixcc.novelthree.treader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.treader.Config;
import cc.ixcc.novelthree.treader.util.DisplayUtils;
import cc.ixcc.novelthree.treader.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class LightSettingDialog extends Dialog {

    @BindView(R.id.tv_dark)
    TextView tv_dark;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.sb_brightness)
    SeekBar sb_brightness;
    @BindView(R.id.tv_bright)
    TextView tv_bright;
    @BindView(R.id.system)
    TextView system;
    @BindView(R.id.sw_system)
    Switch swsystem;
    @BindView(R.id.sw_huyan)
    Switch swhuyan;
    @BindView(R.id.huyan)
    TextView huyan;
    @BindView(R.id.tv_xitong)
    TextView tv_xitong;
    @BindView(R.id.tv_subtract)
    TextView tv_subtract;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_add)
    TextView tv_add;
    @BindView(R.id.tv_qihei)
    TextView tv_qihei;
    @BindView(R.id.tv_default)
    TextView tv_default;
    @BindView(R.id.iv_bg_default)
    CircleImageView iv_bg_default;
    @BindView(R.id.iv_bg_1)
    CircleImageView iv_bg1;
    @BindView(R.id.iv_bg_2)
    CircleImageView iv_bg2;
    @BindView(R.id.iv_bg_3)
    CircleImageView iv_bg3;
    @BindView(R.id.iv_bg_4)
    CircleImageView iv_bg4;
    @BindView(R.id.tv_size_default)
    TextView tv_size_default;
    @BindView(R.id.tv_fzxinghei)
    TextView tv_fzxinghei;
    @BindView(R.id.tv_fzkatong)
    TextView tv_fzkatong;
    @BindView(R.id.tv_bysong)
    TextView tv_bysong;

    private Config config;
    private Boolean isSystem;
    private SettingListener mSettingListener;
    private int FONT_SIZE_MIN;
    private int FONT_SIZE_MAX;
    private int currentFontSize;

    private LightSettingDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public LightSettingDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public LightSettingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_setting_light);
        // ?????????View??????
        ButterKnife.bind(this);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);

        FONT_SIZE_MIN = (int) getContext().getResources().getDimension(R.dimen.reading_min_text_size);
        FONT_SIZE_MAX = (int) getContext().getResources().getDimension(R.dimen.reading_max_text_size);

        config = Config.getInstance();

        //???????????????
        isSystem = config.isSystemLight();
        setTextViewSelect(tv_xitong, isSystem);
        setBrightness(config.getLight());
        swsystem.setChecked(config.isSystemLight());
        swhuyan.setChecked(config.isHuyan());
        //?????????????????????
        currentFontSize = (int) config.getFontSize();
        tv_size.setText(currentFontSize + "");
        initDayOrNight();
        //???????????????
        tv_default.setTypeface(config.getTypeface(Config.FONTTYPE_DEFAULT));
        tv_qihei.setTypeface(config.getTypeface(Config.FONTTYPE_QIHEI));
//        tv_fzxinghei.setTypeface(config.getTypeface(Config.FONTTYPE_FZXINGHEI));
        tv_fzkatong.setTypeface(config.getTypeface(Config.FONTTYPE_FZKATONG));
        tv_bysong.setTypeface(config.getTypeface(Config.FONTTYPE_BYSONG));
//        tv_xinshou.setTypeface(config.getTypeface(Config.FONTTYPE_XINSHOU));
//        tv_wawa.setTypeface(config.getTypeface(Config.FONTTYPE_WAWA));
        selectTypeface(config.getTypefacePath());

        selectBg(config.getBookBgType());

        sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 10) {
                    changeBright(false, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        swsystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSystem = isChecked;
//                isSystem = !isSystem;
                changeBright(isSystem, sb_brightness.getProgress());
            }
        });
        swhuyan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeHuyan(isChecked);
            }
        });
    }

    public void refresh() {
        initDayOrNight();
    }

    private void initDayOrNight() {
        int color = 0;
        int textbg = 0;
        int switchthumb = 0;
        int switchtrack = 0;
        int progressbtn = 0;
        if (config.getDayOrNight()) {
            color = R.color.read_font_night;
            textbg = R.drawable.bg_conner_alpha_night;
            setBgColor(Config.BOOK_BG_NIGHT);
            selectBg(Config.BOOK_BG_DEFAULT);
            switchthumb = R.drawable.thumb_selector_night;
            switchtrack = R.drawable.track_selector_night;
            progressbtn = R.drawable.seek_button_light_heiye;
        } else {
            color = R.color.read_font_default;
            textbg = R.drawable.bg_conner_alpha;
            setBgColor(config.getBookBgType());
            selectBg(config.getBookBgType());
            switchthumb = R.drawable.thumb_selector;
            switchtrack = R.drawable.track_selector;
            progressbtn = R.drawable.seek_button_light;
        }
        system.setTextColor(getContext().getResources().getColor(color));
        huyan.setTextColor(getContext().getResources().getColor(color));
        swsystem.setThumbDrawable(getContext().getResources().getDrawable(switchthumb));
        swsystem.setTrackDrawable(getContext().getResources().getDrawable(switchtrack));
        swhuyan.setThumbDrawable(getContext().getResources().getDrawable(switchthumb));
        swhuyan.setTrackDrawable(getContext().getResources().getDrawable(switchtrack));
        sb_brightness.setThumb(getContext().getResources().getDrawable(progressbtn));
    }

    //?????????????????????
    public void setBgColor(int type) {
        int color = 0;
        switch (type) {
            case Config.BOOK_BG_NIGHT:
                color = config.isHuyan() ? R.color.day_night_huyan : R.color.day_night;
                break;
            case Config.BOOK_BG_DEFAULT:
                color = config.isHuyan() ? R.color.read_bg_default_huyan : R.color.read_bg_default;
                break;
            case Config.BOOK_BG_1:
                color = config.isHuyan() ? R.color.read_bg_1_huyan : R.color.read_bg_1;
                break;
            case Config.BOOK_BG_2:
                color = config.isHuyan() ? R.color.read_bg_2_huyan : R.color.read_bg_2;
                break;
            case Config.BOOK_BG_3:
                color = config.isHuyan() ? R.color.read_bg_3_huyan : R.color.read_bg_3;
                break;
            case Config.BOOK_BG_4:
                color = config.isHuyan() ? R.color.read_bg_4_huyan : R.color.read_bg_4;
                break;
        }
        layout.setBackground(getContext().getResources().getDrawable(color));
    }

    //????????????
    private void selectBg(int type) {
        switch (type) {
            case Config.BOOK_BG_DEFAULT:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_1:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_2:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_3:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_4:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 2));
                break;
        }
    }

    //????????????
    public void setBookBg(int type) {
        config.setBookBg(type);
        if (mSettingListener != null) {
            mSettingListener.changeBookBg(type);
        }
    }

    //????????????
    private void selectTypeface(String typeface) {
        if (typeface.equals(Config.FONTTYPE_DEFAULT)) {
            setTextViewSelect(tv_default, true);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, false);
        } else if (typeface.equals(Config.FONTTYPE_QIHEI)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, true);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, false);
        } else if (typeface.equals(Config.FONTTYPE_FZXINGHEI)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, true);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, true);
//            setTextViewSelect(tv_wawa, false);
        } else if (typeface.equals(Config.FONTTYPE_FZKATONG)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, true);
            setTextViewSelect(tv_bysong, false);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, true);
        } else if (typeface.equals(Config.FONTTYPE_BYSONG)) {
            setTextViewSelect(tv_default, false);
            setTextViewSelect(tv_qihei, false);
            setTextViewSelect(tv_fzxinghei, false);
            setTextViewSelect(tv_fzkatong, false);
            setTextViewSelect(tv_bysong, true);
//            setTextViewSelect(tv_xinshou, false);
//            setTextViewSelect(tv_wawa, true);
        }
    }

    //????????????
    public void setTypeface(String typeface) {
        config.setTypeface(typeface);
        Typeface tface = config.getTypeface(typeface);
        if (mSettingListener != null) {
            mSettingListener.changeTypeFace(tface);
        }
    }

    //????????????
    public void setBrightness(float brightness) {
        sb_brightness.setProgress((int) (brightness * 100));
    }

    //???????????????????????????
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_select_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.read_dialog_button_select));
        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.button_bg));
            textView.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//???????????????
    }

    public Boolean isShow() {
        return isShowing();
    }


    @OnClick({R.id.tv_dark, R.id.tv_bright, R.id.tv_xitong, R.id.tv_subtract, R.id.tv_add, R.id.tv_size_default, R.id.tv_qihei, R.id.tv_fzxinghei, R.id.tv_fzkatong, R.id.tv_bysong,
            R.id.tv_default, R.id.iv_bg_default, R.id.iv_bg_1, R.id.iv_bg_2, R.id.iv_bg_3, R.id.iv_bg_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dark:
                break;
            case R.id.tv_bright:
                break;
            case R.id.tv_xitong:
                isSystem = !isSystem;
                changeBright(isSystem, sb_brightness.getProgress());
                break;
            case R.id.tv_subtract:
                subtractFontSize();
                break;
            case R.id.tv_add:
                addFontSize();
                break;
            case R.id.tv_size_default:
                defaultFontSize();
                break;
            case R.id.tv_qihei:
                selectTypeface(Config.FONTTYPE_QIHEI);
                setTypeface(Config.FONTTYPE_QIHEI);
                break;
            case R.id.tv_fzxinghei:
                selectTypeface(Config.FONTTYPE_FZXINGHEI);
                setTypeface(Config.FONTTYPE_FZXINGHEI);
                break;
            case R.id.tv_fzkatong:
                selectTypeface(Config.FONTTYPE_FZKATONG);
                setTypeface(Config.FONTTYPE_FZKATONG);
                break;
            case R.id.tv_bysong:
                selectTypeface(Config.FONTTYPE_BYSONG);
                setTypeface(Config.FONTTYPE_BYSONG);
                break;
//            case R.id.tv_xinshou:
//                selectTypeface(Config.FONTTYPE_XINSHOU);
//                setTypeface(Config.FONTTYPE_XINSHOU);
//                break;
//            case R.id.tv_wawa:
//                selectTypeface(Config.FONTTYPE_WAWA);
//                setTypeface(Config.FONTTYPE_WAWA);
//                break;
            case R.id.tv_default:
                selectTypeface(Config.FONTTYPE_DEFAULT);
                setTypeface(Config.FONTTYPE_DEFAULT);
                break;
            case R.id.iv_bg_default:
                setBookBg(Config.BOOK_BG_DEFAULT);
                selectBg(Config.BOOK_BG_DEFAULT);
                break;
            case R.id.iv_bg_1:
                setBookBg(Config.BOOK_BG_1);
                selectBg(Config.BOOK_BG_1);
                break;
            case R.id.iv_bg_2:
                setBookBg(Config.BOOK_BG_2);
                selectBg(Config.BOOK_BG_2);
                break;
            case R.id.iv_bg_3:
                setBookBg(Config.BOOK_BG_3);
                selectBg(Config.BOOK_BG_3);
                break;
            case R.id.iv_bg_4:
                setBookBg(Config.BOOK_BG_4);
                selectBg(Config.BOOK_BG_4);
                break;
        }
    }

    //??????????????????
    private void addFontSize() {
        if (currentFontSize < FONT_SIZE_MAX) {
            currentFontSize += 1;
            tv_size.setText(currentFontSize + "");
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    private void defaultFontSize() {
        currentFontSize = (int) getContext().getResources().getDimension(R.dimen.reading_default_text_size);
        tv_size.setText(currentFontSize + "");
        config.setFontSize(currentFontSize);
        if (mSettingListener != null) {
            mSettingListener.changeFontSize(currentFontSize);
        }
    }

    //??????????????????
    private void subtractFontSize() {
        if (currentFontSize > FONT_SIZE_MIN) {
            currentFontSize -= 1;
            tv_size.setText(currentFontSize + "");
            config.setFontSize(currentFontSize);
            if (mSettingListener != null) {
                mSettingListener.changeFontSize(currentFontSize);
            }
        }
    }

    //????????????
    public void changeBright(Boolean isSystem, int brightness) {
        float light = (float) (brightness / 100.0);
        setTextViewSelect(tv_xitong, isSystem);
        config.setSystemLight(isSystem);
        config.setLight(light);
        if (mSettingListener != null) {
            mSettingListener.changeSystemBright(isSystem, light);
        }
    }

    //????????????
    public void changeHuyan(Boolean isHuyan) {
        config.sethuyan(isHuyan);
        initDayOrNight();
        if (mSettingListener != null) {
            mSettingListener.changehuyan(isHuyan);
        }
    }

    public void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    public interface SettingListener {
        void changeSystemBright(Boolean isSystem, float brightness);

        void changehuyan(Boolean isHuyan);

        void changeFontSize(int fontSize);

        void changeTypeFace(Typeface typeface);

        void changeBookBg(int type);
    }

}