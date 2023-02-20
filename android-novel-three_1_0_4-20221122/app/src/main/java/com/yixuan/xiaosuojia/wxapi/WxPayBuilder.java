package com.yixuan.xiaosuojia.wxapi;

import android.content.Context;

/**
 * Created by cxf on 2017/9/22.
 */

public class WxPayBuilder {

    private Context mContext;
    String appId;
    String partnerId;
    String prepayId;
    String packageValue;
    String nonceStr;
    String sign;
    String timeStamp;

    public WxPayBuilder(Context context, String appId, String partnerId, String prepayId, String packageValue, String nonceStr, String sign, String timeStamp) {
        mContext = context;
        this.appId = appId;
        this.partnerId = partnerId;
        this.prepayId = prepayId;
        this.packageValue = packageValue;
        this.nonceStr = nonceStr;
        this.sign = sign;
        this.timeStamp = timeStamp;
    }

    public String getOrderInfo() {
        return createWxOrder();
    }

    /**
     * 根据订单号和商品信息生成支付宝格式的订单信息
     *
     * @return
     */
    private String createWxOrder() {
        String orderInfo = "{partnerId:" + "\"" + partnerId + "\"";

        orderInfo += ",prepayId:" + "\"" + prepayId + "\"";

        orderInfo += ",packageValue:" + "\"" + packageValue + "\"";

        orderInfo += ",nonceStr:" + "\"" + nonceStr + "\"";

        orderInfo += ",timeStamp:" + "\"" + timeStamp + "\"";

        orderInfo += ",sign:" + "\"" + sign + "\"}";
        return orderInfo;
    }
}
