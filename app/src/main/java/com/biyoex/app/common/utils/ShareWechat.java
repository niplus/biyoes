package com.biyoex.app.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.biyoex.app.R;
import com.biyoex.app.common.Constants;

public class ShareWechat {
    /**
     * 微信7.0版本号，兼容处理微信7.0版本分享到朋友圈不支持多图片的问题
     */
    private static final int VERSION_CODE_FOR_WEI_XIN_VER7 = 1380;
    /**
     * 微信包名
     */
    public static final String PACKAGE_NAME_WEI_XIN = "com.tencent.mm";

 /**
  * 分享URl到微信朋友圈
  *
  * */

    public static void ShareUrlToWechat(String url,String title,String message,Context mContext,boolean isShareToline){
        IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, false);
        //初始化 WXImageObject 和 WXMediaMessage 对象
//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.send_img);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl =url;
        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title =title;
        msg.messageExt = message;
        msg.description =message;
        Bitmap thumbBmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_logo);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumbBmp, 200, 200, true);
        msg.thumbData =Util.bmpToByteArray(scaledBitmap, true);
//      构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = msg;
        req.scene = isShareToline?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
//        req.userOpenId = getOpenId();
        wxapi.sendReq(req);
    }
    /**
     * 分享图片到微信朋友圈
     *
     * @param bmp     分享的图片的Bitmap对象
     * @param content 分享内容
     */
    public static void shareImageToWechat(Bitmap bmp, String content, Context mContext,boolean isSharetoline) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, false);
        //初始化 WXImageObject 和 WXMediaMessage 对象
//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.send_img);
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 200,200, true);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img";
        req.message = msg;
        req.scene = isSharetoline?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
//        req.userOpenId = getOpenId();
        wxapi.sendReq(req);
//调用api接口，发送数据到微信

//        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
//        String fileName = "share";
//        File appDir = new File(file, fileName);
//        if (!appDir.exists()) {
//            appDir.mkdirs();
//        }
//        fileName = System.currentTimeMillis() + ".jpg";
//        File currentFile = new File(appDir, fileName);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(currentFile);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        ArrayList<Uri> uris = new ArrayList<>();
//        Uri uri = null;
//        try {
//            ApplicationInfo applicationInfo = mContext.getApplicationInfo();
//            int targetSDK = applicationInfo.targetSdkVersion;
//            if (targetSDK >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(mContext.getContentResolver(), currentFile.getAbsolutePath(), fileName, null));
//            } else {
//                uri = Uri.fromFile(new File(currentFile.getPath()));
//            }
//            uris.add(uri);
//        } catch (Exception ex) {
//
//        }
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        ComponentName comp = new ComponentName(PACKAGE_NAME_WEI_XIN, isSharetoline?"com.tencent.mm.ui.tools.ShareToTimeLineUI":"com.tencent.mm.ui.tools.ShareImgUI");
//        intent.setComponent(comp);
//        intent.setType("image/*");
//        intent.putExtra("Kdescription", content);
//        if (VersionUtil.getVersionCode(mContext, PACKAGE_NAME_WEI_XIN) < VERSION_CODE_FOR_WEI_XIN_VER7) {
//            // 微信7.0以下版本
//            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//        } else {
//            // 微信7.0及以上版本
//            intent.setAction(Intent.ACTION_SEND);
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
//        }
//        mContext.startActivity(intent);
    }


    public static class VersionUtil {


        /**
         * 获取制定包名应用的版本的versionCode
         *
         * @param context
         * @param
         * @return
         */
        public static int getVersionCode(Context context, String packageName) {
            try {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(packageName, 0);
                int version = info.versionCode;
                return version;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }


    }
}
