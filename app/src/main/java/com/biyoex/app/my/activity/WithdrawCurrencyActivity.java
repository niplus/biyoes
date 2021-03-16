package com.biyoex.app.my.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.SendCodeView;
import com.biyoex.app.common.activity.LoginActivity;
import com.biyoex.app.common.bean.CancelWithdrawEvent;
import com.biyoex.app.common.bean.RequestResult;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.present.SendCodePresent;
import com.biyoex.app.common.utils.CashierInputFilterUtils;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.utils.RegexUtils;
import com.biyoex.app.common.utils.TimerUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.my.adapter.ChainNameAdapter;
import com.biyoex.app.my.bean.ChainNameBean;
import com.biyoex.app.my.bean.ZGRechargeCoinBean;
import com.biyoex.app.my.bean.ZGSessionInfoBean;
import com.biyoex.app.my.view.ManyChainDialog;
import com.biyoex.app.my.view.TransferChainNameDialog;
import com.biyoex.app.qrscan.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

import static com.biyoex.app.qrscan.activity.CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN;
import static com.biyoex.app.qrscan.activity.CaptureActivity.RESULT_CODE_QR_SCAN;

/**
 * 提现页面
 */

public class WithdrawCurrencyActivity extends BaseActivity<SendCodePresent> implements SendCodeView {
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.tv_frozen)
    TextView tvFrozen;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.tv_withdrawals_number)
    TextView tvWithdrawalsNumber;
    @BindView(R.id.ed_withdrawals_number)
    EditText edWithdrawalsNumber;
    @BindView(R.id.tv_transaction_pad)
    TextView tvTransactionPad;
    @BindView(R.id.ed_transaction_pad)
    EditText edTransactionPad;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.ed_verification_code)
    EditText edVerificationCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.iv_add_adderss)
    ImageView ivAdderss;
    @BindView(R.id.tv_max)
    TextView tvMax;
    @BindView(R.id.tv_min)
    TextView tvMin;
    @BindView(R.id.ed_google_code)
    EditText edGoogleCode;
    @BindView(R.id.rl_google_code)
    LinearLayout rlGoogleCode;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.rv_remark)
    View rvMark;
    @BindView(R.id.tv_coin_shortName)
    TextView tvCoinShortName;
    @BindView(R.id.tv_coin_name)
    TextView tvCoinName;
    @BindView(R.id.tv_widthdraw_hit)
    TextView tvWidthdrawHint;
    @BindView(R.id.tv_fee_name)
    TextView tvFeeName;
    @BindView(R.id.tv_all_name)
    TextView tvAllName;
    @BindView(R.id.tv_account_tatol)
    TextView tvAccount;
    @BindView(R.id.switchover)
    TextView tvSwitchover;
    @BindView(R.id.tv_max_draw)
    TextView tvMaxDraw;
    @BindView(R.id.rl_payment)
    LinearLayout layoutPayment;
    @BindView(R.id.edit_payment)
    EditText editPayment;
    @BindView(R.id.tv_real_account)
    TextView tvRealaccount;
    @BindView(R.id.tv_coin_chainName)
    TextView tvChainName;
    @BindView(R.id.layout_coin_chainName)
    LinearLayout layout_coinChainName;

    private ChainNameAdapter adapter;
    private List<ChainNameBean> list = new ArrayList<>();
    private TransferChainNameDialog dialogs;
    private ZGRechargeCoinBean zgRechargeCoinBean;
    private ManyChainDialog chainDialog;

    private String strCurrencyName;

    private ZGSessionInfoBean sessionInfo;
    /**
     * 计时器类对象
     */
    private TimerUtils timerUtils;

    private String strSymbol;

    private String strShortName;

    private boolean isGoogleBind;

    private NumberFormat numberFormat;

    private ZGRechargeCoinBean.AddressBean addressBean;

    /**
     * 最小提币额度
     */
    private double drawMin;
    private String url;
    private double maxVolume;

    /**
     * 可用币
     */
    private double totalValue;

    private double feeAmount;
    private double feeRadio;

    /**
     * 短信验证码dialog
     */
    private Dialog dialog;

    @SuppressLint("SetTextI18n")
    @Override
    public void initData() {
        chainDialog = new ManyChainDialog(this);
        Intent itCurrencyName = getIntent();
        strCurrencyName = itCurrencyName.getStringExtra("currency_name");
        strSymbol = itCurrencyName.getStringExtra("symbol");
        url = itCurrencyName.getStringExtra("img_url");
        strShortName = itCurrencyName.getStringExtra("shortName");
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(true);
        adapter = new ChainNameAdapter(this, R.layout.item_account_name, list);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            dialogs.dismiss();
            String type = zgRechargeCoinBean.getMultiChainList().get(position).getType();
            feeAmount = zgRechargeCoinBean.getMultiChainList().get(position).getFeeAmount();
            tvChainName.setText("USDT-" + type);
            edWithdrawalsNumber.setHint(getString(R.string.single_minimum_withdrawals) +
                    numberFormat.format(zgRechargeCoinBean.getMultiChainList().get(position).getMinWithdraw()));
            String number=edWithdrawalsNumber.getText().toString().trim();
            if (!TextUtils.isEmpty(number)) {
                double value = Double.parseDouble(number);
                double fee = MoneyUtils.add(feeAmount, MoneyUtils.mul(value, feeRadio));
                tvPrompt.setText(MoneyUtils.decimal4ByUp(new BigDecimal(fee)).toPlainString());
                tvRealaccount.setText(getString(R.string.actual_amount_received) + (MoneyUtils.sub(number, tvPrompt.getText().toString())));
            } else {
                tvPrompt.setText("0");
                tvRealaccount.setText(getString(R.string.actual_amount_received));
            }
        });
        dialogs = new TransferChainNameDialog(this, adapter);
        edWithdrawalsNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!TextUtils.isEmpty(edWithdrawalsNumber.getText())) {
                    double number = Double.parseDouble(edWithdrawalsNumber.getText().toString());
                    if (number < drawMin) {
//                            ToastUtils.showToast(getString(R.string.single_minimum_withdrawals) + numberFormat.format(drawMin));
                        showToast(getString(R.string.single_minimum_withdrawals) + numberFormat.format(drawMin));
                    }
                }
            }
        });

        showLoadingDialog();
        requestCoinParams();

        edWithdrawalsNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double value;
                if (!TextUtils.isEmpty(s)) {
                    value = Double.parseDouble(s.toString());
                    double fee = MoneyUtils.add(feeAmount, MoneyUtils.mul(value, feeRadio));
                    tvPrompt.setText(MoneyUtils.decimal4ByUp(new BigDecimal(fee)).toPlainString());
                    tvRealaccount.setText(getString(R.string.actual_amount_received) + (MoneyUtils.sub(s.toString(), tvPrompt.getText().toString())));
                } else {
                    tvPrompt.setText("0");
                    tvRealaccount.setText(getString(R.string.actual_amount_received));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sessionInfo = SessionLiveData.getIns().getValue();
        if (sessionInfo != null && !sessionInfo.isAuthDeep()) {
            tvMin.setVisibility(View.VISIBLE);
        } else {
            tvMin.setVisibility(View.GONE);
        }
        EventBus.getDefault().register(this);
        tvSwitchover.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected SendCodePresent createPresent() {
        return new SendCodePresent(SendCodePresent.ACCOUNT, 4);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initComp() {
        isGoogleBind = sessionInfo.isGoogleBind();
        rlGoogleCode.setVisibility(isGoogleBind ? View.VISIBLE : View.GONE);
        tvCoinShortName.setText(strShortName);
        tvCoinName.setText("(" + strCurrencyName + ")");
        CashierInputFilterUtils cashierInputFilterUtils = new CashierInputFilterUtils();
        cashierInputFilterUtils.POINTER_LENGTH = 6;
        final InputFilter[] isPrice = {cashierInputFilterUtils};
        edWithdrawalsNumber.setFilters(isPrice);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCancelWithdraw(CancelWithdrawEvent cancelWithdrawEvent) {
        requestCoinParams();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw_currency;
    }


    @OnClick({R.id.iv_back, R.id.iv_menu, R.id.btn_confirm, R.id.tv_send_code, R.id.iv_qu_scan,
            R.id.iv_add_adderss, R.id.tv_all, R.id.iv_chain_name_introduce, R.id.tv_chainName_switchover})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_menu: //提现记录
                Intent itWithdrawAndRechargeCurrencyRecord = new Intent(this, WithdrawAndRechargeCurrencyRecordActivity.class);
                itWithdrawAndRechargeCurrencyRecord.putExtra("type", getString(R.string.withdraw));
                itWithdrawAndRechargeCurrencyRecord.putExtra("symbol", strSymbol);
                itWithdrawAndRechargeCurrencyRecord.putExtra("img_url", url);
                itWithdrawAndRechargeCurrencyRecord.putExtra("currency_name", strCurrencyName);
                startActivity(itWithdrawAndRechargeCurrencyRecord);
                break;
            case R.id.btn_confirm://提现
                if (TextUtils.isEmpty(edtAddress.getText().toString())) {
//                    ToastUtils.showToast(getString(R.string.please_input_withdraw_address));
                    showToast(getString(R.string.please_input_withdraw_address));
                } else if (TextUtils.isEmpty(edWithdrawalsNumber.getText().toString())) {
//                    ToastUtils.showToast(getString(R.string.please_input_withdraw_number));
                    showToast(getString(R.string.please_input_withdraw_number));
                } else if (TextUtils.isEmpty(edTransactionPad.getText())) {
//                    ToastUtils.showToast(R.string.input_trade_password);
                    showToast(getString(R.string.input_trade_password));
                } else if (RegexUtils.inputFilter(edTransactionPad.getText().toString())) {
//                    ToastUtils.showToast(R.string.illegal_charaters);
                    showToast(getString(R.string.illegal_charaters));
                } else if (layoutPayment.getVisibility() == View.VISIBLE && editPayment.getText().toString().isEmpty()) {
                    showToast(getString(R.string.input_payment));
                }
//                else if(TextUtils.isEmpty(edGoogleCode.getText().toString())){
//                    showToast("请输入google验证器验证码");
//                }
                else {
                    sessionInfo = SessionLiveData.getIns().getValue();
                    if (sessionInfo != null) {
                        if (sessionInfo.isSafeword()) {
                            requestWithdrawBtc();
//                            edGoogleCode.setText("");
//                            edWithdrawalsNumber.setText("");
//                            edTransactionPad.setText("");
//                            edVerificationCode.setText("");
                        } else {
                            ToastUtils.showToast(getString(R.string.need_set_trade_password));
                            Intent itPersonalInfo = new Intent(WithdrawCurrencyActivity.this, RevisePayPasswordActivity.class);
                            itPersonalInfo.putExtra("revise", getString(R.string.setting));
                            startActivity(itPersonalInfo);
                        }
                    } else {
                        Intent itLogin = new Intent(this, LoginActivity.class);
                        itLogin.putExtra("type", "info");
                        startActivity(itLogin);
                    }
                }
                break;
            case R.id.iv_add_adderss: //添加地址
                Intent itCurrencyAddressList = new Intent(WithdrawCurrencyActivity.this, CurrencyAddressListActivity.class);
                itCurrencyAddressList.putExtra("strSymbol", strSymbol);
                itCurrencyAddressList.putExtra("address", addressBean);
                itCurrencyAddressList.putExtra("isUSDT", strShortName.equals("USDT"));
                startActivityForResult(itCurrencyAddressList, 0x2);
                break;
            case R.id.tv_send_code: //短信验证码
                if (!tvSendCode.getText().toString().contains(getString(R.string.second))) {
                    mPresent.sendCode(null);
                }
                break;
            case R.id.iv_qu_scan:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 0x1);
                break;
            case R.id.tv_all:
                edWithdrawalsNumber.setText(MoneyUtils.subZeroAndDot(MoneyUtils.decimal4ByUp(new BigDecimal(Math.min(maxVolume, totalValue))).toPlainString()));
                break;
            case R.id.iv_chain_name_introduce://链名称介绍
                chainDialog.show();
                break;
            case R.id.tv_chainName_switchover://链名称切换
                dialogs.show();
                break;
        }
    }
//
//    private void showBottomDialog() {
//        dialog = new Dialog(getContext(), R.style.BottomDialog);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_draw, null);
//        TextView textView = view.findViewById(R.id.tv_money);
//        final CodeInputView inputView = view.findViewById(R.id.code_input);
//        String s = edWithdrawalsNumber.getText().toString();
//        Double now_money = Double.valueOf(s) - Double.valueOf(tvPrompt.getText().toString());
//        textView.setText("实际到账数量" + new BigDecimal(now_money).setScale(4, BigDecimal.ROUND_HALF_UP));
//        dialog.setContentView(view);
//        dialog.setCanceledOnTouchOutside(true);
//        Window dialogWindow = dialog.getWindow();
//        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams attr = dialogWindow.getAttributes();
////        inputView.setOnCodeCompleteListener(code -> );
//        if (attr != null) {
//            attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            attr.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            attr.gravity = Gravity.BOTTOM;
//            dialogWindow.setAttributes(attr);
//        }
//        dialog.show();//显示对话框
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0x1 && resultCode == RESULT_CODE_QR_SCAN) {
            String address = data.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
            edtAddress.setText(address);
            edtAddress.setSelection(edtAddress.getText().length());
        }
        if (requestCode == 0x2) {
            if (resultCode == RESULT_OK) {
                addressBean = (ZGRechargeCoinBean.AddressBean) data.getSerializableExtra("address");
                edtAddress.setText(addressBean.getAddress());
                edtAddress.setSelection(edtAddress.getText().length());
                editPayment.setText(addressBean.getRemark());
                if (addressBean.isIsremark() && !TextUtils.isEmpty(addressBean.getRemark())) {
                    rvMark.setVisibility(View.VISIBLE);
                    tvRemark.setText(addressBean.getRemark());
                } else {
                    rvMark.setVisibility(View.GONE);
                }
            } else if (resultCode == 2) {
                edtAddress.setText("");
                rvMark.setVisibility(View.GONE);
            }
        }
    }

    public void requestCoinParams() {
        RetrofitHelper.getIns().getZgtopApi()
                .getUserParamsByCoin(strSymbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RequestResult<ZGRechargeCoinBean>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void success(RequestResult<ZGRechargeCoinBean> zgRechargeCoinBeanRequestResult) {
                        zgRechargeCoinBean = zgRechargeCoinBeanRequestResult.getData();
                        totalValue = Double.valueOf(zgRechargeCoinBean.getTotal());
                        tvAvailable.setText(Html.fromHtml(MoneyUtils.decimal4ByUp(new BigDecimal(totalValue)) + " " + strShortName));
                        tvFrozen.setText(Html.fromHtml(MoneyUtils.decimal4ByUp(new BigDecimal(zgRechargeCoinBean.getFrozen())) + " " + strShortName));
                        maxVolume = zgRechargeCoinBean.getMax();
                        tvMax.setText(numberFormat.format(maxVolume) + " " + strShortName);
                        drawMin = zgRechargeCoinBean.getMin();
                        tvFeeName.setText(zgRechargeCoinBean.getFeeName());
                        tvAllName.setText(zgRechargeCoinBean.getFeeName());
                        feeRadio = zgRechargeCoinBean.getFeeRatio();
                        tvPrompt.setText("0");
                        tvAccount.setText("可用:" + zgRechargeCoinBean.getTotal());
                        tvWidthdrawHint.setText(getString(R.string.user_max_draw) + NumberFormat.getInstance().format(zgRechargeCoinBean.getMax()) + "\n" + getString(R.string.draw_info));
//                            boolean isremark = jsonObject.optBoolean("isremark");
//                            Log.i("isremark", "onResponse: "+isremark);
                        layoutPayment.setVisibility(zgRechargeCoinBean.getIsremark() ? View.VISIBLE : View.GONE);
                        hideLoadingDialog();
                        if (list.size() > 0) {
                            list.clear();
                        }
                        String multiChain = zgRechargeCoinBean.getMultiChain();
                        if ("true".equals(multiChain)) {
                            layout_coinChainName.setVisibility(View.VISIBLE);
                            for (int i = 0; i < zgRechargeCoinBean.getMultiChainList().size(); i++) {
                                ChainNameBean bean = new ChainNameBean();
                                String Default = zgRechargeCoinBean.getMultiChainList().get(i).getDefaultFlag();
                                String type = zgRechargeCoinBean.getMultiChainList().get(i).getType();
                                if ("true".equals(Default)) {
                                    feeAmount = zgRechargeCoinBean.getMultiChainList().get(i).getFeeAmount();
                                    tvChainName.setText("USDT-" + type);
                                    edWithdrawalsNumber.setHint(getString(R.string.single_minimum_withdrawals) +
                                            numberFormat.format(zgRechargeCoinBean.getMultiChainList().get(i).getMinWithdraw()));
                                }
                                bean.setType(type);
                                list.add(bean);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            layout_coinChainName.setVisibility(View.GONE);
                            feeAmount = zgRechargeCoinBean.getFeeAmount();
                            edWithdrawalsNumber.setHint(getString(R.string.single_minimum_withdrawals) + numberFormat.format(zgRechargeCoinBean.getMin()));
                        }
                    }

                    @Override
                    protected void failed(int code, String data, String msg) {
                        super.failed(code, data, msg);
                        hideLoadingDialog();
                    }
                });
    }

    /**
     * 申请提现虚拟币
     */
    public void requestWithdrawBtc() {
        try {
            Map<String, String> params = new HashMap<>();
            //google验证码
            params.put("googleCode", edGoogleCode.getText().toString());
            //提现地址
            params.put("address", edtAddress.getText().toString());
            //提现数量
            params.put("amount", edWithdrawalsNumber.getText().toString());
            //交易密码
            params.put("safeWord", edTransactionPad.getText().toString());
            //验证码
            params.put("code", edVerificationCode.getText().toString());
            params.put("id", strSymbol);
            if (layoutPayment.getVisibility() == View.VISIBLE) {
                params.put("remark", editPayment.getText().toString());
            }

            OkHttpUtils
                    .post()
                    .addHeader("X-Requested-With", "XMLHttpReques")
                    .url(Constants.BASE_URL + "v1/account/withdrawCoin")
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                            hideLoadingDialog();
                            showToast(getString(R.string.net_error));
                        }

                        @Override
                        public void onResponse(String response, int id) throws JSONException {
                            JSONObject dataJson = new JSONObject(response);
                            hideLoadingDialog();
                            if (dataJson.has("code")) {
                                String code = dataJson.getString("code");
                                if (code.equals("200")) {
//                                ToastUtils.showToast(strCurrencyName + getString(R.string.withdraw_success));
                                    String string = getString(R.string.withdraw_success);
                                    showToast(strCurrencyName + string + "");
//                                    //清空填写数据
//                                    edGoogleCode.setText("");
//                                    edWithdrawalsNumber.setText("0");
//                                    edTransactionPad.setText("");
//                                    edVerificationCode.setText("");
                                    Intent itWithdrawAndRechargeCurrencyRecord = new Intent(WithdrawCurrencyActivity.this, WithdrawAndRechargeCurrencyRecordActivity.class);
                                    itWithdrawAndRechargeCurrencyRecord.putExtra("type", getString(R.string.withdraw));
                                    itWithdrawAndRechargeCurrencyRecord.putExtra("symbol", strSymbol);
                                    itWithdrawAndRechargeCurrencyRecord.putExtra("img_url", url);
                                    itWithdrawAndRechargeCurrencyRecord.putExtra("currency_name", strCurrencyName);
                                    startActivity(itWithdrawAndRechargeCurrencyRecord);
                                    requestCoinParams();
                                    //从财务详情进入提现，提现成功需要刷新
                                    EventBus.getDefault().post(new CancelWithdrawEvent(strShortName, Integer.valueOf(strSymbol)));
                                } else if (code.equals("3")) {
                                    if (sessionInfo.isAuthDeep()) {
                                        ToastUtils.showToast(getString(R.string.single_largest_withdraw) + dataJson.getString("data"));
                                    } else {
                                        ToastUtils.showToast(getString(R.string.please_complete_certification));
                                    }
                                } else if (code.equals("13")) {
                                    showToast("请先开启谷歌二次验证");
                                } else if (code.equals("-1")) {
                                    showToast(getString(R.string.error_address_info));
                                } else if (code.equals("-2")) {
                                    showToast(getString(R.string.error_payment));
                                } else {
                                    String data = dataJson.getString("msg");
//                                ToastUtils.showToast(withdrawBtcMessage(code, data));
                                    showToast(withdrawBtcMessage(code, data));
                                }
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SessionLiveData.getIns().getValue() != null) {
            sessionInfo = SessionLiveData.getIns().getValue();
        }
    }

    /**
     * 提现币种提示语
     *
     * @param resultCode
     * @return
     */
    public String withdrawBtcMessage(String resultCode, String data) {
        String messag = "";
        switch (resultCode) {
            case "2":
                messag = getString(R.string.min_withdraw_volume) + data;
                break;
            case "3":
                messag = getString(R.string.please_complete_certification);
                break;
            case "5":
                messag = getString(R.string.message_not_enough_balance);
                break;
            case "6":
                messag = getString(R.string.not_enough_fee);
                break;
            case "7":
                messag = getString(R.string.max_withdraw_time);
                break;
            case "106":

                messag = getString(R.string.code_error_too_much);
                break;
            case "107":

                messag = getString(R.string.code_error_remains);
                break;
            case "4":

                messag = getString(R.string.coin_not_support_withdraw);
                break;
            case "1":
                messag = getString(R.string.need_photo_certify);
                break;
            case "9":

                messag = getString(R.string.trade_password_error);
                break;
            case "10":

                messag = getString(R.string.google_code_error);
                break;
            case "100":
                messag = getString(R.string.code_not_send);
                break;
            case "101":
                messag = getString(R.string.opera_too_much);
                break;
            case "102":

                messag = getString(R.string.code_error_remains_time) + data;
                break;
        }
        if (messag.equals("")) {
            messag = getString(R.string.withdraw_failed);
        }
        return messag;
    }

    @Override
    public void sendCodeSuccess() {
        timerUtils = TimerUtils.getInitialise();
        timerUtils.resetTimer();
        if (timerUtils.getCanOnClick()) {
            timerUtils.startTimer(tvSendCode, 61);
        }
//        showBottomDialog();
    }

}
