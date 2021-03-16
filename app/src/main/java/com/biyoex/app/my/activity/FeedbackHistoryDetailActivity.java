package com.biyoex.app.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.activity.LookPictureActivity;
import com.biyoex.app.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedbackHistoryDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_question_title)
    TextView tvQuestionTitle;
    @BindView(R.id.tv_question_content)
    TextView tvQuestionContent;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_answer)
    TextView tvAnswer;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;

    @BindView(R.id.ll_answer)
    View llAnswer;

    private String path;

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.history_list));
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        tvQuestionTitle.setText(getString(R.string.title)+":"+intent.getStringExtra("title"));
        tvQuestionContent.setText(intent.getStringExtra("content"));
        tvCreateTime.setText(intent.getStringExtra("create_time"));

        if (intent.getIntExtra("status", 0) == 1){
            tvAnswer.setText(intent.getStringExtra("answer"));
            tvUpdateTime.setText(intent.getStringExtra("update_time"));
        }else {
            llAnswer.setVisibility(View.GONE);
        }

        path = intent.getStringExtra("path");
        String type = "";
        if (path != null){
            type = path.substring(path.lastIndexOf(".")+1);
        }
        //当文件是jpg或者png的时候才显示
        if (path != null &&
                (type.equals("jpg") || type.equals("png"))){
            Glide.with(this)
                    .load(VBTApplication.appPictureUrl+path)
                    .into(ivImage);
        }else {
            ivImage.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback_history_detail;
    }

    @OnClick(R.id.iv_image)
    public void onClick(View view){
        Intent itLookPicture = new Intent(this, LookPictureActivity.class);
        itLookPicture.putExtra("type", "normal");
        itLookPicture.putExtra("picture_url", path);
        startActivity(itLookPicture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.rl_fall_back)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
