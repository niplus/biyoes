package com.biyoex.app.my.activity;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.GestureIconView;
import com.biyoex.app.common.widget.GestureLock;
import com.biyoex.app.common.widget.GestureLockView;
import com.biyoex.app.common.widget.MyStyleLockView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//设置
public class GestureSettingActivity extends BaseActivity {

    @BindView(R.id.gesture_lock)
    GestureLock gestureLock;
    @BindView(R.id.gestureIconView)
    GestureIconView gestureIconView;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private List<Integer> gestureCode;

    @Override
    public void initData() {
        gestureCode = new ArrayList<>(9);
    }

    @Override
    protected BasePresent createPresent() {
        return null;
    }

    @Override
    @OnClick(R.id.iv_menu)
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @OnClick(R.id.rl_menu)
//    public void onClick(){
//        gestureLock.clear();
//        gestureCode.clear();
//        tvHint.setTextColor(getResources().getColor(R.color.text_black_to_middle));
//        tvHint.setText("设置手势密码，保护账号安全");
//        gestureLock.setMode(GestureLock.MODE_EDIT);
//    }

    @Override
    public void initComp() {
        tvTitle.setText(getString(R.string.gesture_password_setting));
//        tvMenu.setText("重绘");
        tvHint.setText(R.string.set_gesture_account_security);
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
                return new int[0];
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
                return new MyStyleLockView(GestureSettingActivity.this);
            }
        });
        gestureLock.setOnGestureEventListener(new GestureLock.OnGestureEventListener() {
            @Override
            public void onBlockSelected(int position) {
                if (position == -1) {
                    gestureIconView.clear();
                } else {
                    if (gestureLock.getMode() == GestureLock.MODE_EDIT)
                        gestureCode.add(position);
                    //绘制缩略图
                    gestureIconView.setSelected(position);
                }
            }

            @Override
            public void onGestureEvent(boolean matched) {
                Log.i("onGestureEvent : " + matched);
                if (matched) {
                    //保存手势密码
                    try {
                        SharedPreferencesUtils.getInstance().setUserGesture(listToString());
                        SharedPreferencesUtils.getInstance().setUserGestureSwitch(true);
                        ToastUtils.showToast(R.string.Gesture_password_enabled);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    tvHint.setText(R.string.Inconsistent_password);
                    tvHint.setTextColor(getResources().getColor(R.color.price_red));
                    tvHint.startAnimation(translateAnimation);
                }

                gestureLock.clear();
            }

            @Override
            public void onUnmatchedExceedBoundary(int times) {
                Log.i("onUnmatchedExceedBoundary");
            }

            @Override
            public void onGestureSetFinished() {
                Log.i("onGestureSetFinished : " + gestureCode.size());
                if (gestureCode.size() < 5) {
                    tvHint.setTextColor(getResources().getColor(R.color.price_red));
                    tvHint.setText(R.string.Contain_at_least_5_dots);
                    tvHint.startAnimation(translateAnimation);
                    gestureCode.clear();
                } else {
                    gestureLock.setGestureCode(listToArray());
                    gestureLock.setMode(GestureLock.MODE_NORMAL);
                    tvHint.setTextColor(getResources().getColor(R.color.my_theme));
                    tvHint.setText(R.string.please_enter_again);
                }
                gestureLock.clear();
            }
        });
        gestureLock.setMode(GestureLock.MODE_EDIT);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_setting;
    }

    private int[] listToArray() {
        int[] array = new int[gestureCode.size()];

        for (int i = 0; i < gestureCode.size(); i++) {
            array[i] = gestureCode.get(i);
        }

        return array;
    }

    private String listToString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < gestureCode.size(); i++) {
            builder.append(gestureCode.get(i));
        }

        return builder.toString();
    }
}
