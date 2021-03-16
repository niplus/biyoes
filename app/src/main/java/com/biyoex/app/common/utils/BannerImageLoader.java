package com.biyoex.app.common.utils;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.biyoex.app.R;
import com.youth.banner.RoundImageView;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by xxx on 2018/7/11.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, RoundImageView imageView) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        try {
            Glide.with(context)                             //配置上下文
                    .load(path)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                    .error(R.mipmap.img_error)
//                    .placeholder(R.mipmap.img_error)
                    .apply(  new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )//缓存全尺寸)
                    .into(imageView);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
