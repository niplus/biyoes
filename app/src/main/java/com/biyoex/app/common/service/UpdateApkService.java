package com.biyoex.app.common.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import android.provider.Settings;
import android.widget.RemoteViews;

import com.biyoex.app.R;
import com.biyoex.app.common.utils.FilePathUtils;
import com.biyoex.app.common.utils.MobilePhoneDeviceInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateApkService extends Service {
    public static final String UPDATEURL = "UPDATE_URL";
    public static final String IS_FORCE_UPDATE = "is_force_update";

    public static final String UPDATE_APK = "android.intent.action.updateapk";
    public static final String UPDATE_APK_FILE = "Updateapk";

    private int UpdateNotifId = 1011;

    private String fileName = "RingSetting";

    private long startTime = 0;
    private long currTime = 0;
    private boolean isForceUpdate = false;

    private NotificationManager notifiManager;
    Notification.Builder builder = null;

    private String name = "zgtop";
    private String id = "channel_001";

    @Override
    public void onCreate() {
        super.onCreate();
        //fileName = this.getPackageName();
        fileName = this.getString(R.string.packageName);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent != null) {
            isForceUpdate = intent.getBooleanExtra(IS_FORCE_UPDATE, false);
            final String updateurl = intent.getStringExtra(UPDATEURL);
            if (updateurl != null && !updateurl.equals("")) {
                Thread th = new Thread(() -> downLoadApk(UpdateApkService.this, updateurl));
                th.start();
            }
        }
    }

    private void downLoadApk(Context context, String url) {
        InputStream is = null;
        FileOutputStream fos = null;
        File myTempFile = null;
        try {
            NotifBar(0);
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            conn.connect();
            is = conn.getInputStream();
            if (is == null) {
                throw new Exception("stream is null");
            }

            myTempFile = new File(FilePathUtils.getFileName(FilePathUtils.accessPath(context, "Updateapk"), "zgtop_" + MobilePhoneDeviceInfo.getVersionName(this) + ".apk"));
            fos = new FileOutputStream(myTempFile);
            int total = conn.getContentLength();
            //Log.d("update", "download total" + total);
            int curr = 0;
            startTime = System.currentTimeMillis();
            byte buf[] = new byte[102400];
            do {
                currTime = System.currentTimeMillis();
                int numread = is.read(buf);
                curr = curr + numread;
                int num = curr * 100 / total;
                if ((currTime - startTime > 1000)) {
                    startTime = currTime;
                    NotifBar(num);
                }
                if (numread <= 0) {
                    break;
                }
                fos.write(buf, 0, numread);
            } while (true);
            NotifBar(context,myTempFile);
        } catch (Exception e) {
            e.printStackTrace();

            NotifBar();
            //Log.d("update", "update error");
        }
    }

    private void NotifBar(int num) {
        showNotification(this.getString(R.string.isdownLoding) + num + "%", null, this.getString(R.string.isdownLoding));
//        final Notification.Builder build = new Notification.Builder(this);
//        build.setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker(this.getString(R.string.isdownLoding))
//                .setOngoing(true).setContentTitle(this.getString(R.string.app_name))
//                .setContentText(this.getString(R.string.isdownLoding) + num + "%");
//        build.setProgress(100, num, false);
//        notifiManager.notify(UpdateNotifId, build.getNotification());
    }

    private void showNotification(String content, PendingIntent pendingIntent, String tickter) {
        if (notifiManager == null) {
            notifiManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        }
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notifiManager.createNotificationChannel(mChannel);
            Notification.Builder builder = new Notification.Builder(this, id)
                    .setChannelId(id)
                    .setContentTitle(this.getString(R.string.app_name))
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(tickter);
            if (pendingIntent != null) {
                builder.setContentIntent(pendingIntent);
            }
            notification = builder.build();
        } else {
            Notification.Builder notificationBuilder = new Notification.Builder(this);
            notificationBuilder.setContentTitle(this.getString(R.string.app_name))
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(tickter)
                    .setOngoing(true);
            if (pendingIntent != null) {
                notificationBuilder.setContentIntent(pendingIntent);
            }
            notification = notificationBuilder.getNotification();
        }

        notifiManager.notify(UpdateNotifId, notification);
    }

    private void NotifBar(Context context,File myTempFile) {
        Intent intent = new Intent();
//        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //intent.setDataAndType(Uri.fromFile(myTempFile), "application/vnd.android.package-archive");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", myTempFile);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Uri packageURI = Uri.parse("package:"+context.getPackageName());
//                boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
//                if (!hasInstallPermission) {
//                    Intent intent2 = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
//                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    return;
//                }
//            }
//        } else {
//            intent.setDataAndType(Uri.fromFile(myTempFile), "application/vnd.android.package-archive");
//        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        showNotification(this.getString(R.string.downloadDone) + "100%", pendingIntent, this.getString(R.string.downloadDone));
//        context. startActivity(intent);
        sendBroadcast(myTempFile);
    }
    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        //注意这个是8.0新API
        Uri packageURI = Uri.parse("package:"+getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void NotifBar() {
        if (notifiManager == null) {
            notifiManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        }
        notifiManager.cancel(UpdateNotifId);
        if (builder != null) {
            builder = null;
        }


//        build = new Notification.Builder(this);
//        build.setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker(this.getString(R.string.downloadfail))
//                .setOngoing(false).setContentTitle(this.getString(R.string.app_name))
//                .setContentText(this.getString(R.string.downloadfail));

        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

//        build.setContentIntent(pendingIntent);
//        Notification notification = build.getNotification();
//        notifiManager.notify(UpdateNotifId, notification);
        showNotification(this.getString(R.string.downloadfail), pendingIntent, this.getString(R.string.downloadfail));
        File myTempFile = null;
        sendBroadcast(myTempFile);
//        sendBroadcast(myTempFile);
    }
    private void sendBroadcast(File myTempFile) {
        Intent intentUpdateBroadcast = new Intent(UPDATE_APK);
        // intentUpdateBroadcast.setAction(UPDATE_APK);
        if (myTempFile != null) {
            intentUpdateBroadcast.putExtra(UPDATE_APK_FILE, myTempFile.getPath());
        }
        else {
            intentUpdateBroadcast.putExtra(UPDATE_APK_FILE,"");
        }
        //Log.d("updateAPK", "have send broadcast!");
        this.sendBroadcast(intentUpdateBroadcast);
    }

    // ####################################MM 下载通知
    private static NotificationManager MMnotifiManager = null;
    private static Notification MMnotif = null;
    static int MMUpdateNotifId = 1;

    public static void NotifDownMMBar(Context context, int num) {

        if (MMnotifiManager == null) {
            MMnotifiManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
        if (MMnotif == null) {
            MMnotif = new Notification(R.mipmap.ic_launcher, context.getString(R.string.isdownLoding),
                    System.currentTimeMillis());
            RemoteViews contentView = new RemoteViews(context.getString(R.string.packageName), R.layout.download_zgw_apk);
            contentView.setProgressBar(R.id.down_pb, 100, 0, false);
            contentView.setTextViewText(R.id.down_tx,
                    context.getString(R.string.isdownLoding) + "0%");
            MMnotif.contentView = contentView;
            MMnotif.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            MMnotif.contentIntent = pendingIntent;
        } else ;
        MMnotif.contentView.setProgressBar(R.id.down_pb, 100, num, false);
        MMnotif.contentView.setTextViewText(R.id.down_tx,
                context.getString(R.string.isdownLoding) + num + "%");
        MMnotifiManager.notify(MMUpdateNotifId, MMnotif);
    }

    public static void RemoveNotifMMBar() {
        if (MMnotifiManager != null)
            MMnotifiManager.cancel(MMUpdateNotifId);
    }
}
