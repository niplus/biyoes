package com.biyoex.app.common.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 登录，注册，忘记密码等页面跳转到验证码界面用到
 * Created by xxx on 2018/10/10.
 */

public class SendCodeTimeUtils {

    public boolean needSend = true;

    private Timer timer;

    public void start() {
        needSend = false;
        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                needSend = true;
            }
        }, 60 * 1000);
    }

    public void reset() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
