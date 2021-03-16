/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.biyoex.app.common.utils;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.loader.ImageLoader;
import com.biyoex.app.R;

import java.io.File;


/**
 *
 */
public class GlideImageLoader implements ImageLoader {


    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions       .error(R.mipmap.default_image)           //设置错误图片
                .placeholder(R.mipmap.default_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))//设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                //设置占位图片
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions       .error(R.mipmap.default_image)           //设置错误图片
                .placeholder(R.mipmap.default_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))//设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                //设置占位图片
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
