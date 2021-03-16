package com.biyoex.app.my.view;

import com.biyoex.app.common.mvpbase.BaseView;
import com.biyoex.app.my.bean.TransferRecordBean;

import java.util.List;

public interface TransferRecordView extends BaseView {
    void getRecordListSuccess(List<TransferRecordBean> transferRecordBeans);
    void getRecordListFailed();
}
