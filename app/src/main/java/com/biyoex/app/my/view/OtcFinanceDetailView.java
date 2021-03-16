package com.biyoex.app.my.view;

import com.biyoex.app.my.bean.OtcFinanceListBean;

public interface OtcFinanceDetailView extends TransferRecordView {
    void getFinanceDetailSuccess(OtcFinanceListBean otcFinanceListBean);
    void getFinanceDetailFailed();
}
