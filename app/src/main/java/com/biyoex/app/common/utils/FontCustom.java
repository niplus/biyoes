package com.biyoex.app.common.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class FontCustom {

    public void replaceSystemDefaultFont(Context context, String fontPath) {
        replaceTypefaceField("MONOSPACE", Typeface.createFromAsset(context.getAssets(), fontPath));
    }

    private void replaceTypefaceField(String fieldName, Object value) {
        try {
            Field defaultField = Typeface.class.getDeclaredField(fieldName);
            defaultField.setAccessible(true);
            defaultField.set(null, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
