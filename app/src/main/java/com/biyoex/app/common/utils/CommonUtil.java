package com.biyoex.app.common.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import com.google.android.material.tabs.TabLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CommonUtil {


    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "/vbt/" + System.currentTimeMillis();

    //0 = -  1 = +
    public static String getEditADDandSub(int type, String editString, int decimal) {
        if (type == 0) {
            String baseNumberAdd = getDoubleByDecimals(decimal);
            if (!TextUtils.isEmpty(editString)) {
                double currentPrice = Double.valueOf(editString);
//                Log.i(TAG, "getEditADDandSub: ");
                double result = MoneyUtils.sub(currentPrice, new BigDecimal(baseNumberAdd).doubleValue());
                if (result < 0) {
                    result = 0;
                }
                return MoneyUtils.decimalByUp(decimal, new BigDecimal(result)).toPlainString();
            }
            return "0";
        } else {
            String baseNumberAdd = getDoubleByDecimals(decimal);
            if (!TextUtils.isEmpty(editString)) {
                double currentPrice = new BigDecimal(editString).doubleValue();
                Log.i("????????????", currentPrice + "getEditADDandSub: " + new BigDecimal(baseNumberAdd).doubleValue());
//                double result = ;
                return MoneyUtils.add(new BigDecimal(editString).doubleValue(), new BigDecimal(baseNumberAdd).doubleValue()) + "";
            } else {
                return MoneyUtils.decimalByUp(decimal, new BigDecimal(baseNumberAdd)).toPlainString();
            }
        }
    }

    private static String getDoubleByDecimals(int decimals) {
        if (decimals == 0) {
            return "1";
        }
//        android.util.Log.i(TAG, "getDoubleByDecimals: " + decimals);
        StringBuilder stringBuilder = new StringBuilder("0.");
        for (int i = 0; i < decimals - 1; i++) {
            stringBuilder.append("0");
        }
        stringBuilder.append("1");
//        android.util.Log.i(TAG, "getDoubleByDecimals: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    //??????????????????tablayout???????????????
    public static void setTabWidth(final TabLayout tabLayout) {
        try {
            //??????tabLayout???mTabStrip??????
            LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                View tabView = mTabStrip.getChildAt(i);
                //??????tabView???mTextView??????  tab????????????????????????????????????mTextView
                Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                mTextViewField.setAccessible(true);
                TextView mTextView = (TextView) mTextViewField.get(tabView);
                tabView.setPadding(0, 0, 0, 0);
                //???????????????????????????   ????????????????????????????????????mTextView?????????
                int width = 0;
                width = mTextView.getWidth();
                if (width == 0) {
                    mTextView.measure(0, 0);
                    width = mTextView.getMeasuredWidth();
                }
                width = 50;
                //??????tab???????????? ????????????????????????Padding ???????????????????????????????????? tabView?????????????????????
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
//                        Log.i("tablayout", "run: " + width + "   " + tabView.getWidth() + "     " + (params.width - width) / 2);
                params.width = width;
                params.leftMargin = (tabView.getWidth() - width) / 2;
                params.rightMargin = (tabView.getWidth() - width) / 2;
                tabView.setLayoutParams(params);
                tabView.invalidate();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void saveBitmap2file(Bitmap bmp, Context context) {
        String savePath;
        String fileName = generateFileName() + ".JPEG";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show();
            return;
        }

        File filePic = new File(savePath + fileName);
        try {
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "????????????,??????:" + filePic.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath + fileName)));
    }

    public static String saveBItmapFile(Bitmap bmp, Context context) {
        String savePath;
        String fileName = generateFileName() + ".JPEG";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
//            Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show();
            return "";
        }

        File filePic = new File(savePath + fileName);
        try {
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
//            Toast.makeText(context, "????????????,??????:" + filePic.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath + fileName)));
        return filePic.getAbsolutePath();
    }
    public static File getFileDir(Context context, String desFileName) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/carefree/");
            if (!dir.exists()) {
                dir.createNewFile();
            }
            return dir;
        } catch (Exception e) {
            e.printStackTrace();
            return new File(context.getFilesDir() + desFileName);
        }

    }

    public static Bitmap createAsciiPic(final String path, Context context) {
        final String base = "#8XOHLTI)i=+;:,.";// ???????????????????????????
//        final String base = "#,.0123456789:;@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";// ???????????????????????????
        StringBuilder text = new StringBuilder();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Bitmap image = BitmapFactory.decodeFile(path);  //????????????
        int width0 = image.getWidth();
        int height0 = image.getHeight();
        int width1, height1;
        int scale = 7;
        if (width0 <= width / scale) {
            width1 = width0;
            height1 = height0;
        } else {
            width1 = width / scale;
            height1 = width1 * height0 / width0;
        }
        image = scale(path, width1, height1);  //????????????
        //????????????????????????
        for (int y = 0; y < image.getHeight(); y += 2) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int pixel = image.getPixel(x, y);
                final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                final int index = Math.round(gray * (base.length() + 1) / 255);
                String s = index >= base.length() ? " " : String.valueOf(base.charAt(index));
                text.append(s);
            }
            text.append("\n");
        }
        return textAsBitmap(text, context);
//        return creatCodeBitmap(text,context,colors);
//        return image;
    }

    public static void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize) {
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try {
                    //???????????? mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //???????????????????????????Tab
                        View view = mTabContainer.getChildAt(j);
                        //?????????Tab????????????????????????
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));

                        //?????????Tab??????????????????
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //?????????????????????DP???????????????setTextSize??????????????????????????????????????????????????????????????????????????????????????????????????????????????????*??????2?????????????????????????????????DP???????????????????????????
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0, 0, 0, dip2px(20 - textSize - space / 2));

                        //?????????Tab??????????????????
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        iconView.setScaleType(ImageView.ScaleType.CENTER);
                        //??????????????????????????????MethodUtils.dip2px()?????????dp???
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0, 0, 0, space / 2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int dip2px(float dpValue) {
        final float scale = VBTApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static Bitmap createAsciiPicColor(final String path, Context context) {
        final String base = "#8XOHLTI)i=+;:,.";// ???????????????????????????
//        final String base = "#,.0123456789:;@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";// ???????????????????????????
        StringBuilder text = new StringBuilder();
        List<Integer> colors = new ArrayList<>();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Bitmap image = BitmapFactory.decodeFile(path);  //????????????
        int width0 = image.getWidth();
        int height0 = image.getHeight();
        int width1, height1;
        int scale = 7;
        if (width0 <= width / scale) {
            width1 = width0;
            height1 = height0;
        } else {
            width1 = width / scale;
            height1 = width1 * height0 / width0;
        }
        image = scale(path, width1, height1);  //????????????
        //????????????????????????
        for (int y = 0; y < image.getHeight(); y += 2) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int pixel = image.getPixel(x, y);
                final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                final int index = Math.round(gray * (base.length() + 1) / 255);
                String s = index >= base.length() ? " " : String.valueOf(base.charAt(index));
                colors.add(pixel);
                text.append(s);
            }
            text.append("\n");
            colors.add(0);
        }
        return textAsBitmapColor(text, colors, context);
//        return creatCodeBitmap(text,context,colors);
//        return image;
    }

    public static Bitmap creatCodeBitmap(StringBuilder contents, Context context, List<Integer> colors) {
//        contents = new StringBuilder().append("")
        float scale = context.getResources().getDisplayMetrics().scaledDensity;

        TextView tv = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(contents);
        ForegroundColorSpan colorSpan;
        for (int i = 0; i < colors.size(); i++) {
            colorSpan = new ForegroundColorSpan(colors.get(i));
            spannableString.setSpan(colorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(spannableString);
        tv.setTextSize(scale * 2);
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setDrawingCacheEnabled(true);
        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());


        tv.setBackgroundColor(Color.WHITE);

        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }

    public static Bitmap textAsBitmap(StringBuilder text, Context context) {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.GRAY);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(12);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         //

        StaticLayout layout = new StaticLayout(text, textPaint, width,

                Layout.Alignment.ALIGN_CENTER, 1f, 0.0f, true);

        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,

                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        canvas.translate(10, 10);

        canvas.drawColor(Color.WHITE);

//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//???????????????

        layout.draw(canvas);

        return bitmap;

    }

    public static Bitmap textAsBitmapColor(StringBuilder text, List<Integer> colors, Context context) {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.TRANSPARENT);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setTextSize(12);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         //
        SpannableStringBuilder spannableString = new SpannableStringBuilder(text);
        ForegroundColorSpan colorSpan;
        for (int i = 0; i < colors.size(); i++) {
            colorSpan = new ForegroundColorSpan(colors.get(i));
            spannableString.setSpan(colorSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        StaticLayout layout = new StaticLayout(spannableString, textPaint, width,

                Layout.Alignment.ALIGN_CENTER, 1f, 0.0f, true);

        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,

                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        canvas.translate(10, 10);

        canvas.drawColor(Color.WHITE);

//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//???????????????

        layout.draw(canvas);

        return bitmap;

    }

    public static Bitmap scale(String src, int newWidth, int newHeight) {
        Bitmap ret = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(src), newWidth, newHeight, true);
        return ret;
    }

    /**
     * ??????????????????????????????
     */
    private static final String FILES_NAME = "/MyPhoto";
    /**
     * ?????????????????????
     */
    public static final String TIME_STYLE = "yyyyMMddHHmmss";
    /**
     * ????????????
     */
    public static final String IMAGE_TYPE = ".png";


    /**
     * ???????????????????????????
     *
     * @param context ?????????
     * @return ?????????????????????
     */
    private static String getPhoneRootPath(Context context) {
        // ?????????SD???
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || !Environment.isExternalStorageRemovable()) {
            // ??????SD????????????
            return context.getExternalCacheDir().getPath();
        } else {
            // ??????apk?????????????????????
            return context.getCacheDir().getPath();
        }
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @return ??????????????????+????????????
     */
    public static String getPhotoFileName(Context context) {
        File file = new File(getPhoneRootPath(context) + FILES_NAME);
        // ???????????????????????????????????????????????????
        if (!file.exists()) {
            file.mkdirs();
        }
        // ????????????????????????
        SimpleDateFormat format = new SimpleDateFormat(TIME_STYLE, Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        String photoName = "/" + time + IMAGE_TYPE;
        return file + photoName;
    }

    /**
     * ??????Bitmap?????????SD??????
     * ????????????SD?????????????????????
     *
     * @param mbitmap ???????????????Bitmap??????
     * @return ??????????????????????????????????????????????????????null
     */
    public static String savePhotoToSD(Bitmap mbitmap, Context context) {
        FileOutputStream outStream = null;
        String fileName = getPhotoFileName(context);
        try {
            outStream = new FileOutputStream(fileName);
            // ????????????????????????100???????????????
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // ?????????????????????
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ????????????1/10???????????????
     *
     * @param path ???????????????
     * @return ??????????????????
     */
    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 10; // ?????????????????????????????????????????????
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        options = null;
        return bmp;
    }

    /**
     * ????????????????????????
     *
     * @param originpath ????????????
     * @param context    ?????????
     * @return ????????????????????????????????????
     */
    public static String amendRotatePhoto(String originpath, Context context) {

        // ????????????????????????
        int angle = readPictureDegree(originpath);
        // ????????????????????????Bitmap??????
        if (angle != 0) {
            Bitmap bmp = getCompressPhoto(originpath);
            Bitmap bitmap = rotaingImageView(angle, bmp);
            return savePhotoToSD(bitmap, context);
        } else {
            return originpath;
        }

    }

    /**
     * ????????????????????????
     *
     * @param path ????????????
     * @return ??????
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * ????????????
     *
     * @param angle  ???????????????
     * @param bitmap ????????????
     * @return ??????????????????
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // ???????????????????????????????????????
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // ?????????????????????????????????????????????????????????????????????
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    public static Bitmap sizeCompres(String path, int rqsW, int rqsH) {
        // ???option???????????????bitmap???????????????????????????
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// ???????????????Bitmap???????????????????????????
        BitmapFactory.decodeFile(path, options);// ?????????????????????????????????option??????
        final int height = options.outHeight;//?????????????????????option??????outHeight?????????
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) {
            options.inSampleSize = 1;
        } else if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
        }
        return BitmapFactory.decodeFile(path, options);// ????????????option??????inSampleSize?????????????????????????????????
    }
    /**
     * ???????????????
     *
     * @param text ??????????????????????????????????????????
     * @param size ????????????????????????????????????
     * @return bitmap
     */
    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
