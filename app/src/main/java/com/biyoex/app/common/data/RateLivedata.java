package com.biyoex.app.common.data;

import androidx.lifecycle.LiveData;

import com.biyoex.app.common.Constants;
import com.biyoex.app.common.bean.RateBean;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.utils.GsonUtil;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.utils.log.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

public class RateLivedata extends LiveData<Map<String, RateBean.RateInfo>> {

    private static RateLivedata ins;
    private Timer timer;

    private RateLivedata() {

    }

    /**
     * 初始化计时器，每隔1分钟请求一次rate
     */
    public void init() {
        Log.i("RateLivedata init");

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestRate();
            }
        }, 0, 1000 * 60 * 1);
    }

    public static RateLivedata getIns() {
        if (ins == null) {
            ins = new RateLivedata();
        }

        return ins;
    }

    public void setRateValue(Map<String, RateBean.RateInfo> rateInfoMap) {
        setValue(rateInfoMap);
    }

    private void requestRate() {
        Log.d("requestRate");
        OkHttpUtils
                .get()
                .url(Constants.BASE_URL + "v2/market/getUsdtPrice")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(final String response, int id) throws JSONException {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("code") == 200) {
                            RateBean rateBean = GsonUtil.GsonToBean(response, RateBean.class);
                            Map<String, String> map = rateBean.getData();
                            Map<String, RateBean.RateInfo> rateInfoMap = new HashMap<>();

                            //把获取到的币转rmb和转usdt的放入map中
                            for (String key : map.keySet()) {
                                RateBean.RateInfo rateInfo = new RateBean.RateInfo();
                                if (map.get(key) == null) {
                                    rateInfo.setRmbPrice(0);
                                    rateInfo.setUsdtPrice(0);
                                } else {
                                    //usdt是对人民币的汇率，其他是对usdt的汇率
                                    if (key.equals("usdt")) {
                                        rateInfo.setUsdtPrice(1);
                                        rateInfo.setRmbPrice(Double.valueOf(map.get(key)));
                                    } else {
                                        rateInfo.setUsdtPrice(Double.valueOf(map.get(key)));
                                        rateInfo.setRmbPrice(MoneyUtils.mul(Double.valueOf(map.get(key)), Double.valueOf(map.get("usdt"))));
                                    }
                                }
                                rateInfoMap.put(key.toUpperCase(), rateInfo);
//                                rateInfoMap.put("USDT", rateInfo);
                            }
//                            android.util.Log.i("汇率", "onResponse: "+rateInfoMap.toString());
                            setValue(rateInfoMap);

                        }

                    }
                });
    }
}
