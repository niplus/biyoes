package com.biyoex.app.common;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by xxx on 2018/8/8.
 */

public class Constants {
        public static String BaseUrl = "https://www.vb.pub";//正式
//    public static String BaseUrl = "http://xxly.natapp1.cc";//测试
    public static String BASE_URL = BaseUrl + "/api/";
    //    public static final String REALM_NAME = "http://xxly.natapp1.cc/";
    public static String REALM_NAME = BaseUrl;
    public static String PosRule = REALM_NAME + "/pos/guize";
    public static final String ZenkenAccountKey = "HMyFTvomfQLr7wGVnauOVGduVVggA8rI";
    /**
     * b
     * websocket的base url
     */

    public static String shareId = "";
    public static String BaseWebSocketUrl = "wss://www.vb.pub";
    public static String WEBSOCKET_URL = BaseWebSocketUrl + "/socket.io/?";
    //        public static final String WEBSOCKET_URL = "ws://xxly.nat100.top/socket.io/?";
    //帮助中心
    public static final String HELP_URL = "https://biyoex.com/mobile/#/index";
    //提交工单
    public static final String POST_IDEA = "https://vbt.zendesk.com/hc/zh-sg/requests/new";
    //绑定google操作
    public static final String BIND_Google = "https://vbt.zendesk.com/hc/zh-sg/articles/360024239773";
    //微信的appId
    public static final String APP_ID = "wx9ab15729996414bc";
    public static String coinName = "VBT";//用來存儲當前是選擇那种币种/默认显示VBT
    public static String coinGroup = "USDT";
    public static boolean isBuy = true;

    /**
     * 超时连接30s
     */
    public static final int TIME_OUT = 30 * 1000;

    /**
     * 法币C2C支付方式
     */

    public static class PayType {
        public static final int ALI_PAY = 1;
        public static final int WECHAT_PAY = 2;
        public static final int BANK_PAY = 3;
    }

    //EventBus
    public static final int REFRESH_OTC_ORDER = 0x1;

    //未读消息
    public static final int UNREAD_MESSAGE_EVENT = 0x2;

    /**
     * 订单状态变更
     */
    public static final int ORDER_STATUS_CHANGED = 0x3;

    /**
     * 切换语言或者切换模式需要重启
     */
    public static final int RESTART_EVENT = 0x4;

    /**
     * 去除一下小数点
     */
    public static String numberFormat(double number, int scale) {
        BigDecimal bigDecimal = new BigDecimal(number);
        String format = NumberFormat.getInstance().format(bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue());
        return format;

    }

    public static String numberFormat(double number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        String format = NumberFormat.getInstance().format(bigDecimal.setScale(0, BigDecimal.ROUND_DOWN).doubleValue());
        return format;
    }

    /**
     * 请求网络失败原因
     */
    public static class ExceptionReason {
        /**
         * 解析数据失败C
         */
        public static final int PARSE_ERROR = 0x0;
        /**
         * 网络问题
         */
        public static final int BAD_NETWORK = 0x2;
        /**
         * 连接错误
         */
        public static final int CONNECT_ERROR = 0x3;
        /**
         * 连接超时
         */
        public static final int CONNECT_TIMEOUT = 0x4;
        /**
         * 未知错误
         */
        public static final int UNKNOWN_ERROR = 0x5;
    }
}
