package com.youth.banner.loader;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.RoundImageView;


public abstract class ImageLoader implements ImageLoaderInterface<RoundImageView> {
    @Override
    public RoundImageView createImageView(Context context) {
        RoundImageView imageView = new RoundImageView(context);
        return imageView;
    }

}
