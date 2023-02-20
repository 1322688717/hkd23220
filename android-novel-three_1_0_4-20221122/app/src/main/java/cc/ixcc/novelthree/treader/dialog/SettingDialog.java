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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import cc.ixcc.novelthree.treader.Config;
import cc.ixcc.novelthree.treader.util.DisplayUtils;
import cc.ixcc.novelthree.treader.view.CircleImageView;
import cc.ixcc.novelthree.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SettingDialog extends Dialog {

    @BindView(R.id.tv_dark)
    TextView tv_dark;
    @BindView(R.id.sb_brightness)
    SeekBar sb_brightness;
    @BindView(R.id.tv_bright)
    TextView tv_bright;
    @BindView(R.id.tv_xitong)
    TextView tv_xitong;
    @BindView(R.id.tv_subtract)
    RelativeLayout tv_subtract;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_add)
    RelativeLayout tv_add;
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
    @BindView(R.id.tv_simulation)
    TextView tv_simulation;
    @BindView(R.id.tv_cover)
    TextView tv_cover;
    @BindView(R.id.tv_slide)
    TextView tv_slide;
    @BindView(R.id.tv_none)
    TextView tv_none;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.background)
    TextView background;
    @BindView(R.id.page)
    TextView page;
    @BindView(R.id.a)
    TextView a;
    @BindView(R.id.a1)
    TextView a1;
    @BindView(R.id.a2)
    TextView a2;
    @BindView(R.id.a21)
    TextView a21;

    private PageModeDialog.PageModeListener pageModeListener;

    private Config config;
    private Boolean isSystem;
    private SettingListener mSettingListener;
    private int FONT_SIZE_MIN;
    private int FONT_SIZE_MAX;
    private int currentFontSize;

    private SettingDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public SettingDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public SettingDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_setting_new);
        // 初始化View注入
        ButterKnife.bind(this);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);

        FONT_SIZE_MIN = (int) getContext().getResources().getDimension(R.dimen.reading_min_text_size);
        FONT_SIZE_MAX = (int) getContext().getResources().getDimension(R.dimen.reading_max_text_size);

        config = Config.getInstance();
        initDayOrNight();

        //初始化亮度
        isSystem = config.isSystemLight();
        setTextViewSelect(tv_xitong, isSystem);
        setBrightness(config.getLight());

        //初始化字体大小
        currentFontSize = (int) config.getFontSize();
        tv_size.setText(currentFontSize + "");

        //初始化字体
        tv_default.setTypeface(config.getTypeface(Config.FONTTYPE_DEFAULT));
        tv_qihei.setTypeface(config.getTypeface(Config.FONTTYPE_QIHEI));
//        tv_fzxinghei.setTypeface(config.getTypeface(Config.FONTTYPE_FZXINGHEI));
        tv_fzkatong.setTypeface(config.getTypeface(Config.FONTTYPE_FZKATONG));
        tv_bysong.setTypeface(config.getTypeface(Config.FONTTYPE_BYSONG));
//        tv_xinshou.setTypeface(config.getTypeface(Config.FONTTYPE_XINSHOU));
//        tv_wawa.setTypeface(config.getTypeface(Config.FONTTYPE_WAWA));
        selectTypeface(config.getTypefacePath());

        selectBg(config.getBookBgType());
        selectPageMode(config.getPageMode());

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
    }

    //设置翻页
    public void setPageMode(int pageMode) {
        config.setPageMode(pageMode);
        if (pageModeListener != null) {
            pageModeListener.changePageMode(pageMode);
        }
    }

    //选择怕翻页
    private void selectPageMode(int pageMode) {
        if (pageMode == Config.PAGE_MODE_SIMULATION) {
            setTextViewSelect(tv_simulation, true);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_COVER) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, true);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_SLIDE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, true);
            setTextViewSelect(tv_none, false);
        } else if (pageMode == Config.PAGE_MODE_NONE) {
            setTextViewSelect(tv_simulation, false);
            setTextViewSelect(tv_cover, false);
            setTextViewSelect(tv_slide, false);
            setTextViewSelect(tv_none, true);
        }
    }

    public void setPageModeListener(PageModeDialog.PageModeListener pageModeListener) {
        this.pageModeListener = pageModeListener;
    }

    public interface PageModeListener {
        void changePageMode(int pageMode);
    }

    private void inithuyan() {
        if (config.isHuyan()) {
            iv_bg_default.setImageResource(R.color.read_bg_default_huyan);
            iv_bg1.setImageResource(R.color.read_bg_1_huyan);
            iv_bg2.setImageResource(R.color.read_bg_2_huyan);
            iv_bg3.setImageResource(R.color.read_bg_3_huyan);
            iv_bg4.setImageResource(R.color.read_bg_4_huyan);
        }
        else {
            iv_bg_default.setImageResource(R.color.read_bg_default);
            iv_bg1.setImageResource(R.color.read_bg_1);
            iv_bg2.setImageResource(R.color.read_bg_2);
            iv_bg3.setImageResource(R.color.read_bg_3);
            iv_bg4.setImageResource(R.color.read_bg_4);
        }
    }

    //选择背景
    private void selectBg(int type) {
        if (config.getDayOrNight()) {
            iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
            iv_bg_default.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
            iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
            iv_bg1.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
            iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
            iv_bg2.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
            iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
            iv_bg3.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
            iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
            iv_bg4.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
        } else {
            iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0.5f));
            iv_bg_default.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
            iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0.5f));
            iv_bg1.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
            iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0.5f));
            iv_bg2.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
            iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0.5f));
            iv_bg3.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
            iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0.5f));
            iv_bg4.setBorderColor(getContext().getResources().getColor(R.color.board_bg));
        }
        switch (type) {
            case Config.BOOK_BG_DEFAULT:
                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 1));
                iv_bg_default.setBorderColor(getContext().getResources().getColor(config.getDayOrNight() ? R.color.read_font_night : R.color.board_bg_s));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 1));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 1));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 1));
                break;
            case Config.BOOK_BG_1:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 1));
                iv_bg1.setBorderColor(getContext().getResources().getColor(config.getDayOrNight() ? R.color.read_font_night : R.color.board_bg_s));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_2:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 1));
                iv_bg2.setBorderColor(getContext().getResources().getColor(config.getDayOrNight() ? R.color.read_font_night : R.color.board_bg_s));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_3:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 1));
                iv_bg3.setBorderColor(getContext().getResources().getColor(config.getDayOrNight() ? R.color.read_font_night : R.color.board_bg_s));
//                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                break;
            case Config.BOOK_BG_4:
//                iv_bg_default.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg1.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg2.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
//                iv_bg3.setBorderWidth(DisplayUtils.dp2px(getContext(), 0));
                iv_bg4.setBorderWidth(DisplayUtils.dp2px(getContext(), 1));
                iv_bg4.setBorderColor(getContext().getResources().getColor(config.getDayOrNight() ? R.color.read_font_night : R.color.board_bg_s));
                break;
        }
    }

    //设置字体
    public void setBookBg(int type) {
        config.setBookBg(type);
        if (mSettingListener != null) {
            mSettingListener.changeBookBg(type);
            initDayOrNight();
        }
    }

    //选择字体
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

    //设置字体
    public void setTypeface(String typeface) {
        config.setTypeface(typeface);
        Typeface tface = config.getTypeface(typeface);
        if (mSettingListener != null) {
            mSettingListener.changeTypeFace(tface);
        }
    }

    //设置亮度
    public void setBrightness(float brightness) {
        sb_brightness.setProgress((int) (brightness * 100));
    }

    private void initDayOrNight() {
        inithuyan();
        int color = 0;
        int textbg = 0;
        if (config.getDayOrNight()) {
            color = R.color.read_font_night;
            textbg = R.drawable.bg_conner_alpha_night;
            setBgColor(Config.BOOK_BG_NIGHT);
            selectBg(Config.BOOK_BG_DEFAULT);
        } else {
            color = R.color.read_font_default;
            textbg = R.drawable.bg_conner_alpha;
            setBgColor(config.getBookBgType());
            selectBg(config.getBookBgType());
        }
        a.setTextColor(getContext().getResources().getColor(color));
        a1.setTextColor(getContext().getResources().getColor(color));
        a2.setTextColor(getContext().getResources().getColor(color));
        a21.setTextColor(getContext().getResources().getColor(color));
        text.setTextColor(getContext().getResources().getColor(color));
        background.setTextColor(getContext().getResources().getColor(color));
        page.setTextColor(getContext().getResources().getColor(color));
        tv_simulation.setTextColor(getContext().getResources().getColor(color));
        tv_cover.setTextColor(getContext().getResources().getColor(color));
        tv_slide.setTextColor(getContext().getResources().getColor(color));
        tv_none.setTextColor(getContext().getResources().getColor(color));
        tv_subtract.setBackground(getContext().getResources().getDrawable(textbg));
        tv_add.setBackground(getContext().getResources().getDrawable(textbg));
    }

    //设置页面的背景
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

    //设置按钮选择的背景
    private void setTextViewSelect(TextView textView, Boolean isSelect) {
        if (isSelect) {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(config.getDayOrNight() ? R.drawable.bg_conner_page_night_s : R.drawable.bg_conner_page_s));
//            textView.setTextColor(getContext().getResources().getColor(R.color.read_dialog_button_select));
        } else {
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(config.getDayOrNight() ? R.drawable.bg_conner_page_night : R.drawable.bg_conner_page));
//            textView.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    private void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    public Boolean isShow() {
        return isShowing();
    }


    @OnClick({R.id.tv_dark, R.id.tv_bright, R.id.tv_xitong, R.id.tv_subtract, R.id.tv_add, R.id.tv_size_default, R.id.tv_qihei, R.id.tv_fzxinghei, R.id.tv_fzkatong, R.id.tv_bysong,
            R.id.tv_default, R.id.iv_bg_default, R.id.iv_bg_1, R.id.iv_bg_2, R.id.iv_bg_3, R.id.iv_bg_4, R.id.tv_simulation, R.id.tv_cover, R.id.tv_slide, R.id.tv_none})
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
            case R.id.tv_simulation:
                selectPageMode(Config.PAGE_MODE_SIMULATION);
                setPageMode(Config.PAGE_MODE_SIMULATION);
                break;
            case R.id.tv_cover:
                selectPageMode(Config.PAGE_MODE_COVER);
                setPageMode(Config.PAGE_MODE_COVER);
                break;
            case R.id.tv_slide:
                selectPageMode(Config.PAGE_MODE_SLIDE);
                setPageMode(Config.PAGE_MODE_SLIDE);
                break;
            case R.id.tv_none:
                selectPageMode(Config.PAGE_MODE_NONE);
                setPageMode(Config.PAGE_MODE_NONE);
                break;
        }
    }

    //变大书本字体
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

    //变小书本字体
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

    public void refresh() {
        initDayOrNight();
    }

    //改变亮度
    public void changeBright(Boolean isSystem, int brightness) {
        float light = (float) (brightness / 100.0);
        setTextViewSelect(tv_xitong, isSystem);
        config.setSystemLight(isSystem);
        config.setLight(light);
        if (mSettingListener != null) {
            mSettingListener.changeSystemBright(isSystem, light);
        }
    }

    public void setSettingListener(SettingListener settingListener) {
        this.mSettingListener = settingListener;
    }

    public interface SettingListener {
        void changeSystemBright(Boolean isSystem, float brightness);

        void changeFontSize(int fontSize);

        void changeTypeFace(Typeface typeface);

        void changeBookBg(int type);
    }

}