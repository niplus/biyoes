package com.biyoex.app.common.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.bean.ImageItem;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.base.BaseActivity;
import com.biyoex.app.common.utils.GlideUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by LG on 2017/7/20.
 */

public class LookPictureActivity extends BaseActivity {
    @BindView(R.id.iv_fall_back)
    ImageView ivFallBack;
    @BindView(R.id.rl_fall_back)
    RelativeLayout rlFallBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.photo_view)
    PhotoView photoView;

    private String strPictureUrl;
    private String strType;

    //正面回调
    public static final int RESULT_CODE_POSITIVE=0x1455;
    //反面回调
    public static final int RESULT_CODE_REVERSESIDE=0x1511;

    //手持正面回调
    public static final int RESULT_CODE_HOLD_POSITIVE = 0x1512;

    //正面身份证图片
    private String strPositiveUrl="";
    //反面省份证图片
    private String strReverseSideUrl="";

    private ArrayList<ImageItem> mPositiveImgPathList;

    private ArrayList<ImageItem> mReverseSideImgPathList;

    private ArrayList<ImageItem> mHoldPositiveImgpathlist;
    @Override
    protected void initView() {
        tvTitle.setText(R.string.check_pic);

    }

    @Override
    protected void initData() {
    Intent itPicture=getIntent();
    strPictureUrl=itPicture.getStringExtra("picture_url");
    GlideUtils.getInstance().displaySquareImage(this, VBTApplication.appPictureUrl+ strPictureUrl,photoView);
    strType= itPicture.getStringExtra("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_look_picture;
    }

    @OnClick({R.id.rl_fall_back, R.id.rl_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_fall_back:
                finish();
                break;
            case R.id.rl_menu:
                //正面
//               if(strType.equals("Positive")){
//                   MPermissionUtils.requestPermissionsResult(LookPictureActivity.this, new MPermissionUtils.OnPermissionListener() {
//                       @Override
//                       public void onPermissionGranted() {
//                               ImagePicker.getInstance().setMultiMode(false);
//                               ImagePicker.getInstance().setShowCamera(true);
//                               Intent intent = new Intent(LookPictureActivity.this, ImageGridActivity.class);
//                               startActivityForResult(intent, RESULT_CODE_POSITIVE);
//
//                       }
//                       @Override
//                       public void onPermissionDenied() {
//                           MPermissionUtils.showTipsDialog(LookPictureActivity.this);
//                       }
//                   }, Manifest.permission.CAMERA);
//               }else if(strType.equals("ReverseSide")){
//                   //反面
//                   MPermissionUtils.requestPermissionsResult(LookPictureActivity.this, new MPermissionUtils.OnPermissionListener() {
//                       @Override
//                       public void onPermissionGranted() {
//                               ImagePicker.getInstance().setMultiMode(false);
//                               ImagePicker.getInstance().setShowCamera(true);
//                               Intent intent = new Intent(LookPictureActivity.this, ImageGridActivity.class);
//                               startActivityForResult(intent, RESULT_CODE_REVERSESIDE);
//                       }
//                       @Override
//                       public void onPermissionDenied() {
//                           MPermissionUtils.showTipsDialog(LookPictureActivity.this);
//                       }
//                   }, Manifest.permission.CAMERA);
//               }else{
//                   //手持正面
//                   MPermissionUtils.requestPermissionsResult(LookPictureActivity.this, new MPermissionUtils.OnPermissionListener() {
//                       @Override
//                       public void onPermissionGranted() {
//                           ImagePicker.getInstance().setMultiMode(false);
//                           ImagePicker.getInstance().setShowCamera(true);
//                           Intent intent = new Intent(LookPictureActivity.this, ImageGridActivity.class);
//                           startActivityForResult(intent, RESULT_CODE_HOLD_POSITIVE);
//                       }
//
//                       @Override
//                       public void onPermissionDenied() {
//                           MPermissionUtils.showTipsDialog(LookPictureActivity.this);
//                       }
//                   }, Manifest.permission.CAMERA);
//               }
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            //正面
//            case RESULT_CODE_POSITIVE:
//                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//                    mPositiveImgPathList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                    Intent itPositive=new Intent();
//                    itPositive.putExtra(ImagePicker.EXTRA_RESULT_ITEMS,mPositiveImgPathList);
//                    setResult(ImagePicker.RESULT_CODE_ITEMS,itPositive);
//                    finish();
//                }
//                break;
//            //反面
//            case RESULT_CODE_REVERSESIDE:
//                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//                mReverseSideImgPathList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                Intent itPositive=new Intent();
//                itPositive.putExtra(ImagePicker.EXTRA_RESULT_ITEMS,mReverseSideImgPathList);
//                setResult(ImagePicker.RESULT_CODE_ITEMS,itPositive);
//                finish();
//            }
//                break;
//            //手持正面
//            case RESULT_CODE_HOLD_POSITIVE:
//                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//                    mHoldPositiveImgpathlist = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                    Intent itPositive=new Intent();
//                    itPositive.putExtra(ImagePicker.EXTRA_RESULT_ITEMS,mHoldPositiveImgpathlist);
//                    setResult(ImagePicker.RESULT_CODE_ITEMS,itPositive);
//                    finish();
//                }
//                break;
//
//        }
//    }
}
