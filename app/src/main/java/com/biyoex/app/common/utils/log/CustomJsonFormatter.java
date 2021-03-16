package com.biyoex.app.common.utils.log;

import com.elvishew.xlog.formatter.FormatException;
import com.elvishew.xlog.formatter.message.json.JsonFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CustomJsonFormatter implements JsonFormatter {
    @Override
    public String format(String data) {
        if (data == null || data.trim().length() == 0) {
            throw new FormatException("JSON empty.");
        }
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String json = gson.toJson(jsonObject);
            return json;
        }catch (Exception e){
            throw new FormatException("Parse JSON error. JSON string:" + data, e);
        }

    }
}
