package com.biyoex.app.my.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.biyoex.app.R;
import com.biyoex.app.common.adapter.TabLayoutAdapter;
import com.biyoex.app.common.base.BaseAppCompatActivity;
import com.biyoex.app.my.fragment.CurrencyManageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LG on 2017/8/22.
 */
//委托管理页面
public class NewsEntrustedManageActivity extends BaseAppCompatActivity {

    @BindView(R.id.vp_entrusted)
    ViewPager vpEntrusted;
    /**
     * 页面集合
     */
    private List<Fragment> mFragmentList;

    private CurrencyManageFragment mCurrencyManageFragment;

    private TabLayoutAdapter mLayoutAdapter;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
        String id = getIntent().getStringExtra("id");
        mCurrencyManageFragment = new CurrencyManageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        mCurrencyManageFragment.setArguments(bundle);
        mFragmentList.add(mCurrencyManageFragment);
        mLayoutAdapter = new TabLayoutAdapter(this, getSupportFragmentManager(), mFragmentList, null);
        vpEntrusted.setAdapter(mLayoutAdapter);
        vpEntrusted.setOffscreenPageLimit(2);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_entrusted_manage;
    }
//    @OnClick({ R.id.rl_menu})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_menu:
//                finish();
//                break;
//            case R.id.rl_menu:
//                if(vpEntrusted!=null){
//                 switch (vpEntrusted.getCurrentItem()){
//                     case 0:
//                        mCurrencyManageFragment.refreshRollTop();
//                         break;
//                 }
//
//                }
//                break;
//        }
//    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        immersionBar.transparentNavigationBar();
        immersionBar.statusBarDarkFont(true);
    }
}
