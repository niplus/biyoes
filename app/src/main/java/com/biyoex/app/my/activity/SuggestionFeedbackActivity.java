package com.biyoex.app.my.activity;

import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.base.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 意见反馈
 * Created by LG on 2017/6/5.
 */

public class SuggestionFeedbackActivity extends BaseAppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
//
//    @BindView(R.id.tab_feedback)
//    TabLayout tabFeedback;
//    @BindView(R.id.vp_feedback)
//    ViewPager vpFeedback;

//    private TabLayoutAdapter adapter;
//    private List<Fragment> fragmentList;
//    private List<String> tabList;

    @Override
    protected void initView() {
        tvTitle.setText("联系客服");
    }

    @Override
    protected void initData() {
//        fragmentList = new ArrayList<>(3);
//        tabList = new ArrayList<>(3);
//
////        fragmentList.add(new FeedbackFragment());
//        fragmentList.add(new BlankFeedbackFragment());
//        fragmentList.add(new FeedbackHistoryFragment());
//        fragmentList.add(new ContactUsFragment());
//        tabList.add(getString(R.string.submit_list));
//        tabList.add(getString(R.string.history_list));
//        tabList.add(getString(R.string.contact_us));
//        adapter = new TabLayoutAdapter(this, getSupportFragmentManager(), fragmentList, tabList);
//        tabFeedback.setupWithViewPager(vpFeedback);
//        vpFeedback.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_suggestion_feedback;
    }

    @Override
    @OnClick(R.id.iv_menu)
    public void onBackPressed() {
        super.onBackPressed();
    }
}
