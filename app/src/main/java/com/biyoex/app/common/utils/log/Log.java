package com.biyoex.app.common.utils.log;

public class Log {
    private static ILog iLog;

    static {
        // TODO: 2019/3/29 获取ILog的实例
        iLog = new XLogImpl();
    }

    public static void init(){
        iLog.init();
    }

    public static void i(String msg){
        iLog.i(msg);
    }

    public static void d(String msg){
        iLog.d(msg);
    }

    public static void e(Throwable e, String msg){
        iLog.e(e, msg);
    }

    public static void e(String msg){
        iLog.e(msg);
    }

    public static void json(String json){
        iLog.json(json);
    }
}
