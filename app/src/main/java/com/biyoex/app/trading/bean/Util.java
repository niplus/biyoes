package com.biyoex.app.trading.bean;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guoziwei.klinelib.model.HisData;
import com.biyoex.app.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 数据解析的示例，数据来自于R.raw.his_data的json
 * Created by guoziwei on 2017/11/23.
 */

public class Util {

    private static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
    private static SimpleDateFormat sFormat1 = new SimpleDateFormat("HHmm", Locale.getDefault());
    private static SimpleDateFormat sFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static SimpleDateFormat sFormat3 = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());


    public static List<HisData> getK(Context context, int day) {
        int res = R.raw.day_k;
        if (day == 7) {
            res = R.raw.week_k;
        } else if (day == 30) {
            res = R.raw.month_k;
        }
        InputStream is = context.getResources().openRawResource(res);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String json = writer.toString();
        final List<KModel> list = new Gson().fromJson(json, new TypeToken<List<KModel>>() {
        }.getType());
        List<HisData> hisData = new ArrayList<>(100);
        for (int i = 0; i < list.size(); i++) {
            KModel m = list.get(i);
            HisData data = new HisData();
            data.setClose(m.getPrice_c());
            data.setOpen(m.getPrice_o());
            data.setHigh(m.getPrice_h());
            data.setLow(m.getPrice_l());
            data.setVol(m.getVolume());
            try {
                data.setDate(sFormat2.parse(m.getTime()).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hisData.add(data);
        }
        return hisData;
    }
}
