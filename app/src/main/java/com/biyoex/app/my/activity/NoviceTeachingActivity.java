//package com.vbt.app.my.activity;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.flyco.tablayout.SlidingTabLayout;
//import com.vbt.app.R;
//import com.vbt.app.common.adapter.TabLayoutAdapter;
//import com.vbt.app.common.base.BaseAppCompatActivity;
//import com.vbt.app.my.fragment.MarketDynamicFragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Created by LG on 2017/6/5.
// */
//
//public class NoviceTeachingActivity extends BaseAppCompatActivity {
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//
//    @BindView(R.id.rl_menu)
//    RelativeLayout rlMenu;
//    @BindView(R.id.public_tabLayout)
//    SlidingTabLayout publicTabLayout;
//    @BindView(R.id.public_viewpager)
//    ViewPager publicViewpager;
//
//    /**
//     * 页面集合
//     */
//    private List<Fragment> mFragmentList;
//    private TabLayoutAdapter mLayoutAdapter;
//
//    private List<String> mTitlesList;
//
//    @Override
//    protected void initView() {
//        tvTitle.setText(R.string.announcement);
//    }
//
//    @Override
//    protected void initData() {
//
//
//        mFragmentList = new ArrayList<>();
//        mFragmentList.add(MarketDynamicFragment.newInstance(1, getString(R.string.announcement_detail)));
//        mFragmentList.add(MarketDynamicFragment.newInstance(4, getString(R.string.help_detail)));
//        mTitlesList = new ArrayList<>();
//        mTitlesList.add(getString(R.string.office_announcement));
//        mTitlesList.add(getString(R.string.help_center));
//
//        mLayoutAdapter = new TabLayoutAdapter(this, getSupportFragmentManager(), mFragmentList, mTitlesList);
//        publicViewpager.setAdapter(mLayoutAdapter);
//        publicTabLayout.setViewPager(publicViewpager);
//        publicViewpager.setOffscreenPageLimit(2);
////        publicViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////            @Override
////            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////
////            }
////
////            @Override
////            public void onPageSelected(int position) {
////
////                tvTitle.setText(mTitlesList.get(position));
////            }
////
////            @Override
////            public void onPageScrollStateChanged(int state) {
////
////            }
////        });
//
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.public_viewpager_title_bar;
//    }
//
//
//
//    @OnClick(R.id.iv_menu)
//    public void onClick() {
//        finish();
//    }
//}
