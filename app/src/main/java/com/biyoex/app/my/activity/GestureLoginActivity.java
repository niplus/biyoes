package com.biyoex.app.my.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.activity.LoginActivity;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.GestureLock;
import com.biyoex.app.common.widget.GestureLockView;
import com.biyoex.app.common.widget.MyStyleLockView;

import butterknife.BindView;
import butterknife.OnClick;

public class GestureLoginActivity extends BaseActivity {

    @BindView(R.id.gestureLock)
    GestureLock gestureLock;
    @BindView(R.id.tv_login_num)
    TextView tvLoginNum;
    @BindView(R.id.tv_hint)
    TextView tvHint;

    private int[] gestureCode;

    @Override
    public void initData() {
        String gestureString = SharedPreferencesUtils.getInstance().getUserGesture();

        if (!TextUtils.isEmpty(gestureString)) {
            gestureCode = string2Array(gestureString);
        }
    }

    @Override
    protected BasePresent createPresent() {
        return null;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.tv_login:
                setResult(1);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void initComp() {
        String loginName = SharedPreferencesUtils.getInstance().getString("loginName", "");
        tvLoginNum.setText(starNum(loginName));
        final TranslateAnimation translateAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0.05f,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setRepeatCount(2);
        translateAnimation.setDuration(50);
        gestureLock.setAdapter(new GestureLock.GestureLockAdapter() {
            @Override
            public int getDepth() {
                return 3;
            }

            @Override
            public int[] getCorrectGestures() {

                if (gestureCode != null) {
                    return gestureCode;
                } else {
                    return new int[0];
                }
            }
            @Override
            public int getUnmatchedBoundary() {
                return 5;
            }

            @Override
            public int getBlockGapSize() {
                return ScreenUtils.dp2px(50);
            }

            @Override
            public GestureLockView getGestureLockViewInstance(Context context, int position) {
                return new MyStyleLockView(GestureLoginActivity.this);
            }
        });
        gestureLock.setOnGestureEventListener(new GestureLock.OnGestureEventListener() {
            @Override
            public void onBlockSelected(int position) {

            }
            @Override
            public void onGestureEvent(boolean matched) {
                if (matched) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    gestureLock.clear();
                }
            }

            @SuppressLint("StringFormatInvalid")
            @Override
            public void onUnmatchedExceedBoundary(int times) {
                Log.i("onUnmatchedExceedBoundary : " + times);
                if (times == 0) {
                    //超过5次需要重新去登陆
                    SharedPreferencesUtils.getInstance().saveString("accessWord", "");
                    setResult(1);
                    finish();
                } else {
                    tvHint.setText(String.format(getString(R.string.password_error), times));
                    tvHint.setTextColor(getResources().getColor(R.color.price_red));
                    tvHint.startAnimation(translateAnimation);
                }
            }

            @Override
            public void onGestureSetFinished() {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_login;
    }

    private int[] string2Array(String src) {
        if (TextUtils.isEmpty(src)) {
            return new int[0];
        }

        int[] dest = new int[src.length()];

        for (int i = 0; i < src.length(); i++) {
            dest[i] = Integer.parseInt(src.charAt(i) + "");
        }

        return dest;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoginActivity.count = 0;
        }

    private String starNum(String num) {
        if (num.contains("@")) {
            return num.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        } else {
            return num.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
    }
}
