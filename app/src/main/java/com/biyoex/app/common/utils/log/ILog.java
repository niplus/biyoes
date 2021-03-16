package com.biyoex.app.common.utils.log;

/**
 * 使用接口定义，到时候便于替换实现类
 */
public interface ILog {
    void init();
    void i(String msg);
    void d(String msg);
    void e(String msg);
    void e(Throwable e, String msg);
    void json(String json);
}
