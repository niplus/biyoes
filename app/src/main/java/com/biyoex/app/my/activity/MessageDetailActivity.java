package com.biyoex.app.my.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.base.BaseActivity;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_message_title)
    TextView tvMessageTitle;
    @BindView(R.id.tv_message_content)
    TextView tvMessageContent;

    private String messageId;

    @Override
    protected void initView() {
        tvTitle.setText(R.string.message_detail);
    }

    @Override
    protected void initData() {
        messageId = getIntent().getStringExtra("id");
        showLoadingDialog();
        requestMessageDetail();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void requestMessageDetail()
    {
        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/account/messageDetail")
                .addParams("id", messageId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (200 == code)
                        {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String title = data.getString("title");
                            String content = data.getString("content");

                            tvMessageTitle.setText(title);
                            tvMessageContent.setText(content);
                        }
                        hideLoadingDialog();
                    }
                });
    }

    @Override
    @OnClick(R.id.iv_menu)
    public void onBackPressed() {
        super.onBackPressed();
    }
}
