package cc.ixcc.novelthree.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import cc.ixcc.novelthree.R
import kotlinx.android.synthetic.main.dialog_pay.*

class PayBottomDialog(context: Context, var wxVisi: Boolean,var zfbVisi: Boolean) : Dialog(context,R.style.PayDialogStyle) {
    lateinit var payClickListener: OnPayClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pay_bottom)
        window?.attributes?.run {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.BOTTOM
        }
        wx.setOnClickListener {
            if (this::payClickListener.isInitialized) {
                payClickListener.wxClick()
                dismiss()
            }
        }
        zfb.setOnClickListener {
            if (this::payClickListener.isInitialized) {
                payClickListener.zfbClick()
                dismiss()
            }
        }
        wx.visibility = if(wxVisi) View.VISIBLE else View.GONE
        zfb.visibility = if(zfbVisi) View.VISIBLE else View.GONE
    }

    override fun show() {
        super.show()
    }

    interface OnPayClickListener {
        fun wxClick()
        fun zfbClick()
    }
}