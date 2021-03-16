package com.biyoex.app.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.biyoex.app.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 图片加载工具类
 * Created by LG on 2017/3/6.
 */

public class GlideUtils {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";
    private static  RequestOptions requestOptions;
    private static class ImageLoaderHolder {
        private static final GlideUtils INSTANCE = new GlideUtils();

    }

    private GlideUtils() {
        requestOptions = new RequestOptions();
    }

    public static final GlideUtils getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }

//    /**
//     * 验证码图片加载
//     *
//     * @param context
//     * @param
//     * @param imageView
//     */
//    public void displayVerificationCodeImage(Context context, Bitmap bitmap, ImageView imageView) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        byte[] bytes = outputStream.toByteArray();
//            RequestOptions requestOptions = new RequestOptions();
//        Glide
//                .with(context)
//                .load(bytes)
//
//                .error(R.mipmap.yzm_no)
//                .placeholder(R.mipmap.yzm_no)
//                .into(imageView);
//    }

    /**
     * 有长方形站位图，并且加载网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
//    public void displayRectangleImage(Context context, String url, ImageView imageView) {
//        Glide
//                .with(context)
//                .load(url)
//                .crossFade()
//                .into(imageView);
//    }

    /**
     * 有正方形站位图，并且加载网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void displaySquareImage(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 加载币的占位图，并且加载网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void displayCurrencyImage(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions   .error(R.mipmap.jb)
                .placeholder(R.mipmap.jb);
        Glide
                .with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }


    //直接本地图片
    public void displayImageThumbnail(Context context, File file, ImageView imageView) {
        Glide
                .with(context)
                .load(file)
                .thumbnail(0.5f)
                .into(imageView);
    }


    //直接加载网络图片
    public void displayImage(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .into(imageView);
    }

    //直接加载网络图片
    public void displayImageNoCenterCrop(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .into(imageView);
    }

//    //加载SD卡图片
//    public void displayImage(Context context, File file, ImageView imageView) {
//        Glide
//                .with(context)
//                .load(file)
//                .centerCrop()
//                .into(imageView);
//
//    }
//
//    //加载SD卡图片并设置大小
//    public void displayImage(Context context, File file, ImageView imageView, int width, int height) {
//        Glide
//                .with(context)
//                .load(file)
//                .override(width, height)
//                .centerCrop()
//                .into(imageView);
//
//    }
//
//    //加载网络图片并设置大小
//    public void displayImage(Context context, String url, ImageView imageView, int width, int height) {
//        Glide
//                .with(context)
//                .load(url)
//                .centerCrop()
//                .override(width, height)
//                .crossFade()
//                .into(imageView);
//    }
//
//    //加载drawable图片
//    public void displayImage(Context context, int resId, ImageView imageView) {
//        Glide.with(context)
//                .load(resourceIdToUri(context, resId))
//                .crossFade()
//                .into(imageView);
//    }
//
//    //加载drawable图片显示为圆形图片
//    public void displayCricleImage(Context context, int resId, ImageView imageView) {
//        Glide.with(context)
//                .load(resourceIdToUri(context, resId))
//                .crossFade()
//                .transform(new GlideCircleTransform(context))
//                .into(imageView);
//    }

    //加载网络图片显示为圆形图片
    public void displayCricleImage(Context context, String url, ImageView imageView) {
        requestOptions       .error(R.mipmap.tx)
                .placeholder(R.mipmap.tx).circleCrop();
        Glide
                .with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    //加载网络图片显示为圆形带边线98图片
    public void displayCricleImageWithBorder(Context context, String url, ImageView imageView) {
        requestOptions.circleCrop();
        Glide
                .with(context)
                .load(url)
                //.centerCrop()//网友反馈，设置此属性可能不起作用,在有些设备上可能会不能显示为圆形。
                .apply(requestOptions)
                .into(imageView);
    }

    //加载网络图片显示为圆形带边线98图片
    public void displayCricleImageWithBorder(Context context, byte[] bytes, ImageView imageView) {
        RequestOptions requestOptions  = new RequestOptions();
        Glide
                .with(context)
                .load(bytes)
                .apply(requestOptions.circleCrop())
                //.centerCrop()//网友反馈，设置此属性可能不起作用,在有些设备上可能会不能显示为圆形。
                .into(imageView);
    }

    //加载SD卡图片显示为圆形图片
    public void displayCricleImage(Context context, File file, ImageView imageView) {
        Glide
                .with(context)
                .load(file)
                //.centerCrop()
                .apply(requestOptions.circleCrop())
                .into(imageView);

    }

    //将资源ID转为Uri
    public Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getString(R.string.packageName) + FOREWARD_SLASH + resourceId);
    }

    //加载bitmap对象，并且生成圆头像
    public void displayBitmapImage(Context context, Bitmap bit, ImageView imageView) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        this.displayCricleImageWithBorder(context, bytes, imageView);
    }


    /**
     * 加载本地图片生成为圆角图片
     *
     * @param context
     * @param
     * @param imageView
     */
    public void displayFilletImage(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .thumbnail(0.5f)
                .apply(requestOptions.circleCrop())
                .into(imageView);
    }
}
