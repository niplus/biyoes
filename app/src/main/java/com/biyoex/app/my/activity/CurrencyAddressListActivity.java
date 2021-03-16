package com.biyoex.app.my.activity;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;

import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.utils.ListSpaceItemDecoration;
import com.biyoex.app.common.widget.IconMessageDialog;
import com.biyoex.app.my.bean.ZGRechargeCoinBean;
import com.biyoex.app.my.presenter.CurrencyAddressListPresenter;
import com.biyoex.app.my.view.CurrencyAddressListView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2017/8/10.
 */

public class CurrencyAddressListActivity extends BaseActivity<CurrencyAddressListPresenter> implements CurrencyAddressListView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.rv_address_list)
    RecyclerView rvAddressList;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private BaseQuickAdapter mAddressAdapter;

    private List<ZGRechargeCoinBean.AddressBean> mAddressList;
    private String strSymbol;
    private ZGRechargeCoinBean.AddressBean address;

    @Override
    public void initData() {
        immersionBar.statusBarDarkFont(true);
        immersionBar.transparentStatusBar();
        tvTitle.setText(R.string.address_list);
        strSymbol = getIntent().getStringExtra("strSymbol");
        address = (ZGRechargeCoinBean.AddressBean) getIntent().getSerializableExtra("address");
        mAddressList = new ArrayList<>();
        rvAddressList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ListSpaceItemDecoration listSpaceItemDecoration = new ListSpaceItemDecoration(20);
        listSpaceItemDecoration.setType(1);
        rvAddressList.addItemDecoration(listSpaceItemDecoration);
        mAddressAdapter = new BaseQuickAdapter<ZGRechargeCoinBean.AddressBean, BaseViewHolder>(R.layout.item_address_info, mAddressList) {
            @Override
            protected void convert(BaseViewHolder holder, ZGRechargeCoinBean.AddressBean addressesBean) {
                if (addressesBean.getFlag() != null) {
                    holder.setText(R.id.tv_address, addressesBean.getAddress())
                            .setText(R.id.tv_name, addressesBean.getFlag());
                }
                holder.setOnClickListener(R.id.iv_menu, v -> {
//                    Intent itSetingCurrency = new Intent(CurrencyAddressListActivity.this, SettingAddressActivity.class);
//                    itSetingCurrency.putExtra("address", addressesBean);
//                    startActivityForResult(itSetingCurrency, 0x1)
                    new IconMessageDialog.Builder(getContext())
                            .setTitle(R.string.hint_message)
                            .setContent(R.string.confirm_delete)
                            .setNegativeMessage(R.string.cancel)
                            .setPositiveMessage(R.string.confirm)
                            .setNegativeButtonListener((dialog, which) -> {
                                dialog.dismiss();
                            })
                            .setPositiveButtonListener((dialog, which) -> {
                                mPresent.deleteAddress(addressesBean.getId());
                                dialog.dismiss();
                            }).build().show();
                });
                holder.setOnClickListener(R.id.select_address, v -> {
                    address = addressesBean;
                    Intent intent = new Intent();
                    intent.putExtra("address", addressesBean);
                    setResult(RESULT_OK, intent);
                    finish();
                });
            }
        };
        rvAddressList.setAdapter(mAddressAdapter);
        View inflate = View.inflate(this, R.layout.view_empty, null);
        mAddressAdapter.setEmptyView(inflate);
        withdrawAddresses();
    }


    @Override
    protected void onResume() {
        super.onResume();
        withdrawAddresses();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();

    }

    public void withdrawAddresses() {
        mPresent.getCoinAddress(strSymbol);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    protected CurrencyAddressListPresenter createPresent() {
        return new CurrencyAddressListPresenter();
    }

    @Override
    public void initComp() {

    }

    @OnClick({R.id.iv_menu, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
            case R.id.btn_confirm:
                Intent itSetingCurrency = new Intent(CurrencyAddressListActivity.this, SettingAddressActivity.class);
                itSetingCurrency.putExtra("strSymbol", strSymbol);
                itSetingCurrency.putExtra("isUSDT", getIntent().getBooleanExtra("isUSDT", false));
                startActivityForResult(itSetingCurrency, 0x1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            withdrawAddresses();
        }
    }

    @Override
    public void getCurrencyAddressSuccess(@NotNull List<ZGRechargeCoinBean.AddressBean> mutableList) {
        mAddressList = mutableList;
        mAddressAdapter.setNewData(mAddressList);
    }

    @Override
    public void deleteAddressSuccess() {
        showToast(getString(R.string.delete_success));
        withdrawAddresses();
    }
}
