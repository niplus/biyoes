package com.biyoex.app.my.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.base.BaseAppCompatActivity;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.widget.SharePostersDialog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
    //邀请界面
public class InviteActivity extends BaseAppCompatActivity {

    @BindView(R.id.qr_code_img)
    ImageView qrCodeImg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.save_zxing)
    TextView tv_save;
    @BindView(R.id.create_http)
    TextView tv_create;
    @BindView(R.id.my_shareid)
    TextView tv_shareId;
    @BindView(R.id.iv_copy)
    ImageView ivCopy;
    private long uid;
    private Bitmap zxing;

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.invite));
//        ivBg.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();

//        View decorView = getWindow().getDecorView();
//        int uiOptions =
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        decorView.setSystemUiVisibility(uiOptions);
    }

    @OnClick(R.id.iv_menu)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void initData() {
        if (SessionLiveData.getIns().getValue() != null) {
            uid = SessionLiveData.getIns().getValue().getId();
            createEnglishQRCodeWithLogo(Constants.REALM_NAME + "/user/register?intro=" + uid);
        }
        tv_save.setOnClickListener(v -> {
            new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            new SharePostersDialog(this).show();
                        } else {
                            showToast(getString(R.string.not_permissmon));
                        }
                    });
        });
        tv_shareId.setText(Constants.shareId);
        ivCopy.setOnClickListener(v -> {
            ClipData clipData1 = ClipData.newPlainText(null, tv_shareId.getText());
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(clipData1);
            showToast(getString(R.string.copy_success));
        });
        tv_create.setOnClickListener(v -> {
            ClipData clipData1 = ClipData.newPlainText(null, Constants.REALM_NAME + "/user/register?intro=" + uid);
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(clipData1);
            Toast.makeText(this, R.string.copy_success, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newinvite;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createEnglishQRCodeWithLogo(final String path) {
        Observable.create((ObservableOnSubscribe<Bitmap>) e -> {
            e.onNext(QRCodeEncoder.syncEncodeQRCode(path, BGAQRCodeUtil.dp2px(InviteActivity.this, 130)));
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Bitmap bitmap) {
                if (bitmap != null) {
                    qrCodeImg.setImageBitmap(bitmap);
                    zxing = bitmap;
                } else {
//                    ToastUtils.showToast(getString(R.string.create_qr_failed));
                    showToast(getString(R.string.create_qr_failed));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
//        immersionBar.transparentStatusBar();
        immersionBar.statusBarDarkFont(true);
    }
}
