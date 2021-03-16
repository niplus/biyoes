package com.biyoex.simplebanner.loader;

import android.content.Context;
import android.view.View;

public interface ImageLoaderInterface<T extends View> {

    void displayImage(Context context, Object path, T imageView);

    T createImageView(Context context);
}
