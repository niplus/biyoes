package com.biyoex.app.common.widget;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.common.service.UpdateApkService;
import com.biyoex.app.common.utils.MPermissionUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils;
import com.biyoex.app.home.bean.UploadApkBean;

import java.util.Arrays;
import java.util.List;

public class UpdateDialog extends Dialog {

    public UpdateDialog(@NonNull Context context, boolean isForcedUpdate, UploadApkBean uploadApkBean) {
        super(context, R.style.Dialog);

        setContentView(R.layout.dialog_update);

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ScreenUtils.dp2px(250);
        window.setAttributes(attributes);
        setCanceledOnTouchOutside(false);
        if (isForcedUpdate) {
            setCancelable(false);
        } else {
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }
        initView(context, isForcedUpdate, uploadApkBean);
    }

    private void initView(final Context context, boolean isForcedUpdate, final UploadApkBean uploadApkBean) {
        RecyclerView rvUpdateInfo = findViewById(R.id.rv_update_info);

        List<String> data;
        String description = uploadApkBean.getDescription();
        data = Arrays.asList(description.split("\\n"));
        BaseQuickAdapter<String, BaseViewHolder> adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_update_info, data) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_content, item);
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvUpdateInfo.setLayoutManager(linearLayoutManager);
        rvUpdateInfo.setAdapter(adapter);
        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText(uploadApkBean.getVersion());

        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(v -> MPermissionUtils.requestPermissionsResult((Activity) context, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                dismiss();
                //启动服务并且进行下载
                Intent intent = new Intent(context, UpdateApkService.class);
                intent.putExtra(UpdateApkService.UPDATEURL, uploadApkBean.getUrl());

                intent.putExtra(UpdateApkService.IS_FORCE_UPDATE, true);
                context.startService(intent);
                ToastUtils.showToast(context.getString(R.string.downloading));
            }

            @Override
            public void onPermissionDenied() {
                MPermissionUtils.showTipsDialog(context);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE));

        TextView tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setVisibility(View.GONE);
//        if (isForcedUpdate) {
//            tvCancel.setVisibility(View.GONE);
//        } else {
//            tvCancel.setOnClickListener(v -> dismiss());
//        }
    }
}
