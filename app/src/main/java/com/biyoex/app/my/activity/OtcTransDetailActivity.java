package com.biyoex.app.my.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.utils.GlideUtils;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.my.bean.TransferRecordBean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;

//划转详情
public class OtcTransDetailActivity extends BaseActivity {

    @BindView(R.id.iv_coin_icon)
    ImageView ivCoinIcon;
    @BindView(R.id.tv_coin_shortName)
    TextView tvCoinShortName;
    @BindView(R.id.tv_coin_name)
    TextView tvCoinName;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private TransferRecordBean transferRecord;

    @Override
    public void initData() {
        transferRecord = (TransferRecordBean) getIntent().getSerializableExtra("record");
    }
    @Override
    protected BasePresent createPresent() {
        return null;
    }

    @OnClick(R.id.rl_fall_back)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void initComp() {
        tvTitle.setText("划转详情");
        GlideUtils.getInstance().displayCurrencyImage(this, VBTApplication.appPictureUrl + transferRecord.getUrl(), ivCoinIcon);
        tvCoinShortName.setText(transferRecord.getCoinName());
        tvCoinName.setText(transferRecord.getAllName());

        //转入
        if (transferRecord.getType() == 1) {
            tvType.setText("币币转入法币");
            tvVolume.setText("+" + MoneyUtils.decimal4ByUp(new BigDecimal(transferRecord.getCount())));
            switch (transferRecord.getStatus()) {
                case 1:
                    tvStatus.setTextColor(getResources().getColor(R.color.commonBlue));
                    tvStatus.setText("待审核");
                    break;
                case 2:
                    tvStatus.setTextColor(getResources().getColor(R.color.price_green));
                    tvStatus.setText("转入成功");
                    break;
                case 3:
                    tvStatus.setTextColor(getResources().getColor(R.color.price_red));
                    tvStatus.setText("转入失败");
                    break;
            }
        } else {
            tvType.setText("法币转出币币");
            tvVolume.setText("-" + MoneyUtils.decimal4ByUp(new BigDecimal(transferRecord.getCount())));
            switch (transferRecord.getStatus()) {
                case 1:
                    tvStatus.setTextColor(getResources().getColor(R.color.commonBlue));
                    tvStatus.setText("待审核");
                    break;
                case 2:
                    tvStatus.setTextColor(getResources().getColor(R.color.price_green));
                    tvStatus.setText("转出成功");
                    break;
                case 3:
                    tvStatus.setTextColor(getResources().getColor(R.color.price_red));
                    tvStatus.setText("转出失败");
                    break;
            }
        }
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        tvTime.setText(sp.format(transferRecord.getCreateTime()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc_trans_detail;
    }
}
