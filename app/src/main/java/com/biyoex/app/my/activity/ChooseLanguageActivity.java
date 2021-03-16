package com.biyoex.app.my.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.v.StartActivity;
import com.biyoex.app.R;
import com.biyoex.app.common.base.BaseAppCompatActivity;
import com.biyoex.app.common.utils.ActivityManagerUtils;
import com.biyoex.app.common.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.OnClick;
    //更换语言
public class ChooseLanguageActivity extends BaseAppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Override
    protected void initView() {
//        ivFallBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.language));
    }

    @OnClick(R.id.rl_menu)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void initData() {
        ActivityManagerUtils.getInstance().addActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_language;
    }

    @OnClick({R.id.rl_chinese, R.id.rl_kr,R.id.rl_us,R.id.rl_jp,R.id.rl_vn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_chinese:
                if (LanguageUtils.currentLanguage == 1){
                    finish();
                    return;
                }
                LanguageUtils.changeLanguage("zh", this);
                break;
            case R.id.rl_kr:
                if (LanguageUtils.currentLanguage == 2){
                    finish();
                    return;
                }
                LanguageUtils.changeLanguage("kr", this);
                break;
            case R.id.rl_jp:
                if (LanguageUtils.currentLanguage == 4){
                    finish();
                    return;
                }
                LanguageUtils.changeLanguage("jp", this);
                break;
            case R.id.rl_us:
                if (LanguageUtils.currentLanguage == 3){
                    finish();
                    return;
                }
                LanguageUtils.changeLanguage("us", this);
                break;
            case R.id.rl_vn:
                if (LanguageUtils.currentLanguage == 5){
                    finish();
                    return;
                }
                LanguageUtils.changeLanguage("vn", this);
                break;
        }
        ActivityManagerUtils.getInstance().finishAllActivity();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
