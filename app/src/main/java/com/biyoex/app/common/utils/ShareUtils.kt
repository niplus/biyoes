package com.biyoex.app.common.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils.getScreenHeight
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils.getScreenWidth

object SharedUtils {
    val WX = "WX"
    val QQ = "QQ"

    private fun checkInstallation(context: Context, packageName: String): Boolean {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }



    fun captureWithStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val width = getScreenWidth()
        val height = getScreenHeight()
        val bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
        view.destroyDrawingCache()
        return bp
    }
}