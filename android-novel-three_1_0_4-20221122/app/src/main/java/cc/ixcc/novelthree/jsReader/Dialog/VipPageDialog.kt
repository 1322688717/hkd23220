package cc.ixcc.novelthree.jsReader.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import cc.ixcc.novelthree.R
import cc.ixcc.novelthree.jsReader.page.PageLoader
import kotlinx.android.synthetic.main.dialog_vip_bottom.*
import kotlinx.android.synthetic.main.dialog_vip_bottom.view.*

class VipPageDialog(
        val activity: FragmentActivity,
        var book: cc.ixcc.novelthree.jsReader.model.bean.CollBookBean,
        var current: Int
) : Dialog(activity, R.style.PayDialogStyle) {
    private val STABLE_STATUS = View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    val ID = 101
    val buttonTexts = arrayListOf("Buttons", "Text", "Both")
    val chapter = book.bookChapters[current]
    val config = cc.ixcc.novelthree.Constants.getInstance().config
    lateinit var payListener: PayDialogListener
    var payClickable = true
    var isAuto = false
    var ownCoins = 0;

    //购买章节数
    var chapter_number = 10

    //总章节数
    var chapter_left = 0
    var pageLoader: PageLoader? = null
        set(value) {
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_vip_bottom)

        window?.attributes?.run {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            gravity = Gravity.BOTTOM
        }

        ivBack.setOnClickListener {
            pageLoader?.skipToPrePage()
            this.dismiss()
        }

    }


    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp: WindowManager.LayoutParams? = window?.attributes
            lp?.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window?.attributes = lp
            // 设置页面全屏显示
            // 设置页面全屏显示
            val decorView: View? = window?.decorView
            decorView?.let {
                it.systemUiVisibility =
                        it.systemUiVisibility or STABLE_STATUS or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            val decorView: View? = window?.decorView
            decorView?.let {
                val option = it.systemUiVisibility or STABLE_STATUS
                it.systemUiVisibility = option
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.dismiss()
        activity.finish()
    }

//    fun setBgColor(color: Int){
//        val drawable1 = GradientDrawable()
//        drawable1.colors = intArrayOf(Color.TRANSPARENT,
//            this.context.resources.getColor(color)
//        )
//        findViewById<ConstraintLayout>(R.id.content).setBackgroundDrawable(drawable1)
//    }

    //TODO
    override fun show() {
        super.show()

        ivClose.setOnClickListener {
            if (this::payListener.isInitialized) {
                payListener.dialogClose(isAuto)
            }
            dismiss()
        }
        Glide.with(context).asBitmap().load(book.coverPic).into(ivBook)

        tvBookName.text = book.title
        tvBookCurrent.text = chapter.title

        //刷新购买金币
        onRefreshCost();

        tv1.text = "Including the free and purchased chapters"

        //个人金币
        balance_box.balance_own_line.balance_own.text = ownCoins.toString() + "${config.coin_name}";

        tvByCurrent.setOnClickListener {
            if (this::payListener.isInitialized) {
                payListener.byCurrent(current, chapter_number)
            }
        }
        tvGoOpen.setOnClickListener {
            if (this::payListener.isInitialized) {
                payListener.goOpen()
            }
        }
        tvAutoBuy.setOnClickListener {
            cbAuto.isChecked = !cbAuto.isChecked
        }
        cbAuto.setOnCheckedChangeListener { buttonView, isChecked ->
            isAuto = isChecked
        }
        cbAuto.isChecked = isAuto

        //4个按钮
        div2.radioGroup1.radio1.setOnClickListener {
            if (chapter_number != 1) {
                div2.radioGroup1.radio1.isChecked = true;
                div2.radioGroup1.radio2.isChecked = false;
                div2.radioGroup1.radio3.isChecked = false;
                div2.radioGroup1.radio4.isChecked = false;

                chapter_number = 1;
                onRefreshCost();
            }
        }
        div2.radioGroup1.radio2.setOnClickListener {
            if (chapter_number != 10) {
                div2.radioGroup1.radio1.isChecked = false;
                div2.radioGroup1.radio2.isChecked = true;
                div2.radioGroup1.radio3.isChecked = false;
                div2.radioGroup1.radio4.isChecked = false;

                chapter_number = 10;
                onRefreshCost();
            }
        }
        div2.radioGroup1.radio3.setOnClickListener {
            if (chapter_number != 50) {
                div2.radioGroup1.radio1.isChecked = false;
                div2.radioGroup1.radio2.isChecked = false;
                div2.radioGroup1.radio3.isChecked = true;
                div2.radioGroup1.radio4.isChecked = false;

                chapter_number = 50;
                onRefreshCost();
            }
        }
        div2.radioGroup1.radio4.setOnClickListener {
            if (chapter_number != chapter_left) {
                div2.radioGroup1.radio1.isChecked = false;
                div2.radioGroup1.radio2.isChecked = false;
                div2.radioGroup1.radio3.isChecked = false;
                div2.radioGroup1.radio4.isChecked = true;

                chapter_number = chapter_left;
                onRefreshCost();
            }
        }

        if (chapter_left < 10) {
            div2.radioGroup1.radio2.visibility = View.GONE;
            div2.radioGroup1.radio3.visibility = View.GONE;
            onRefreshCost();
        } else if (chapter_left < 50) {
            div2.radioGroup1.radio3.visibility = View.GONE;
            onRefreshCost();
        }
    }

    fun onRefreshCost() {
        //计算购买所需金币
        var totalCost = 0;
        var totalDeduce = 0;
        if (chapter_number >= 50) {
            totalCost = (chapter_number * chapter.coin * 0.75).toInt();
            totalDeduce = (chapter_number * chapter.coin * 0.25).toInt();
        } else if (chapter_number >= 10) {
            totalCost = (chapter_number * chapter.coin * 0.9).toInt();
            totalDeduce = (chapter_number * chapter.coin * 0.1).toInt();
        } else {
            totalCost = chapter_number * chapter.coin;
        }

        if (totalCost <= ownCoins) {
            tvByCurrent.text = "${totalCost} Coins to Read this Chapter"
        } else {
            tvByCurrent.text = "Purchase 1442 coins to unlock 10 chapters"
        }

        tvCoin.text = "Actually paid:"
        if (totalDeduce != 0) {
            tvCoin2.text = "" + totalCost.toString() + "${config.coin_name}";
            tvCoin3.text = " Saved(" + totalDeduce.toString() + ")${config.coin_name}";
        } else {
            tvCoin2.text = " " + totalCost.toString() + "${config.coin_name}";
            tvCoin3.text = "";
        }
    }

    interface PayDialogListener {
        fun byCurrent(currentPos: Int, buyChapterCount: Int)
        fun dialogClose(auto: Boolean)
        fun goOpen()
    }
}