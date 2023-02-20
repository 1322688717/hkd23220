package cc.ixcc.novelthree.zuoyou.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import cc.ixcc.novelthree.R

//　　请你务必审慎阅读、充分理解“用户协议”和“隐私政策”各条款，包括但不限于：为了更好的向你提供服务，我们需要收集你的设备标识、操作日志等信息用于分析、优化应用性能。\n　你可阅读《用户协议》和《隐私政策》了解详细信息。如果你同意，请点击下面按钮开始接受我们的服务
class FirstAgreeDialog(context: Context) :AlertDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_agree)

    }



}