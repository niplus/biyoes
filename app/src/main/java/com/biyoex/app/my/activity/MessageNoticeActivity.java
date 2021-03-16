package com.biyoex.app.my.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.ResponseCallBack;
import com.biyoex.app.common.okhttp.callback.ResultModelCallback;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.my.adapter.MessageNoticeAdapter;
import com.biyoex.app.my.bean.MessagesBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by LG on 2017/6/5.
 */

public class MessageNoticeActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.p_recyclerview)
    RecyclerView pRecyclerview;
    @BindView(R.id.sr_SwipeRefreshLayout)
    SwipeRefreshLayout srSwipeRefreshLayout;

    private List<MessagesBean.DataBean> mMessageNoticeList;

    private int intCurrentPage = 1;

    private MessageNoticeAdapter mMessageNoticeAdapter;

    private int intPageCount;

    private String params="";

    private boolean isLoadRefresh=true;

    @Override
    public void initData() {

        mMessageNoticeList = new ArrayList<>();

        LinearLayoutManager messageNoticeLayout = new LinearLayoutManager(this);
        messageNoticeLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageNoticeAdapter = new MessageNoticeAdapter(this, R.layout.item_message_notice, mMessageNoticeList);
        mMessageNoticeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String id = mMessageNoticeList.get(position).getId();
                Intent messageDetailIntent = new Intent(MessageNoticeActivity.this, MessageDetailActivity.class);
                messageDetailIntent.putExtra("id", id);
                startActivity(messageDetailIntent);
                mMessageNoticeList.get(position).setStatus(2);
                mMessageNoticeAdapter.notifyDataSetChanged();
            }
        });

        mMessageNoticeAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                new AlertDialog.Builder(MessageNoticeActivity.this)
                        .setTitle(R.string.hint_message)
                        .setMessage(R.string.confirm_delete_message)
                        .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                (MessageNoticeActivity.this).showLoadingDialog();
                                requestDeletemessage(mMessageNoticeList.get(position).getId(),position);
                            }
                        })
                        .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                return true;
            }
        });

        pRecyclerview.setLayoutManager(messageNoticeLayout);
        pRecyclerview.setAdapter(mMessageNoticeAdapter);
        srSwipeRefreshLayout.setOnRefreshListener(this);
        srSwipeRefreshLayout.setColorSchemeColors(Color.rgb(44, 183, 227));

        showLoadingDialog();
        isLoadRefresh=true;
        requestMessages();
    }

    public void requestDeletemessage(String id,final int position){
        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL+"v1/account/delMessage")
                .addParams("ids",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                        ToastUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        int code = new JSONObject(response).getInt("code");
                        if(200 == code){
                            ToastUtils.showToast(getString(R.string.delete_success));
                            mMessageNoticeList.remove(position);
                            mMessageNoticeAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtils.showToast(getString(R.string.delete_failed));

                        }
                        hideLoadingDialog();
                    }
                });
    }

    @Override
    protected BasePresent createPresent() {
        return null;
    }

    @Override
    public void initComp() {
        tvTitle.setText("我的消息");
    }

    @Override
    public int getLayoutId() {
        return R.layout.public_title_bar_refresh_recyclerview;
    }


    @OnClick(R.id.iv_menu)
    public void onClick() {
        finish();
    }


    public void requestMessages() {
        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/account/messages")
                .addParams("pageSize", "20")
                .addParams("page", intCurrentPage + "")
                .build()
                .execute(new ResultModelCallback(this, new ResponseCallBack<MessagesBean>() {
                    @Override
                    public void onError(String e) {
                        if(e.equals("8989")){
                            requestMessages();
                        }else{
                            if (isLoadRefresh) {
                                //关闭刷新控件
                                srSwipeRefreshLayout.setRefreshing(false);
                                //把底部刷新控件打开
                                mMessageNoticeAdapter.setEnableLoadMore(true);
                            } else {
                                //设置为刷新失败
                                mMessageNoticeAdapter.loadMoreFail();
                                srSwipeRefreshLayout.setEnabled(true);
                            }
                            if(mMessageNoticeList.size()==0) {
                                mMessageNoticeAdapter.setEmptyView(getEmptyView(1));
                            }
                            hideLoadingDialog();
                        }

                    }

                    @Override
                    public void onResponse(MessagesBean response) throws JSONException {
                        intPageCount=response.getTotalCount();

                        if (isLoadRefresh) {
                                    if (mMessageNoticeList.size() > 0) {
                                        mMessageNoticeList.clear();
                                        ToastUtils.showToast(getString(R.string.data_refresh_success));
                                    } else {
                                        hideLoadingDialog();
                                    }
                                    mMessageNoticeList.addAll(response.getData());
                                    mMessageNoticeAdapter.notifyDataSetChanged();
                                    //关闭刷新控件
                                    srSwipeRefreshLayout.setRefreshing(false);
                                    //把底部刷新控件打开
                                    mMessageNoticeAdapter.setEnableLoadMore(true);
                                    //数据刷新成功
                                    mMessageNoticeAdapter.loadMoreComplete();
                                } else {
                                    mMessageNoticeList.addAll(response.getData());
                                    mMessageNoticeAdapter.notifyDataSetChanged();
                                    intCurrentPage += 1;
                                    //数据刷新成功
                                    mMessageNoticeAdapter.loadMoreComplete();
                                    if(mMessageNoticeList.size()>19){
                                        //加入底部刷新
                                        mMessageNoticeAdapter.setOnLoadMoreListener(MessageNoticeActivity.this, pRecyclerview);
                                        //去掉第一次进入页面进行底部刷新
                                        mMessageNoticeAdapter.disableLoadMoreIfNotFullPage(pRecyclerview);

                                        if (intCurrentPage>intPageCount) {
                                            mMessageNoticeAdapter.loadMoreEnd(false);
                                        }
                                    }

                                  srSwipeRefreshLayout.setEnabled(true);

                            }

                        if(mMessageNoticeList.size()==0){
                            mMessageNoticeAdapter.setEmptyView(R.layout.layout_no_data,(ViewGroup) pRecyclerview.getParent());
                            TextView tvMessage = mMessageNoticeAdapter.getEmptyView().findViewById(R.id.tv_message);
                            tvMessage.setText(R.string.no_message_record);
                        }

                         for (int i=0;i<mMessageNoticeList.size();i++){
                             params+=mMessageNoticeList.get(i).getId()+",";
                         }
//
//                        if(!params.equals("")){
//                            requestRemarkmessageremarkmessage(params.substring(0,params.length()-1));
//                        }


                    }

                }));
    }


    public void requestRemarkmessageremarkmessage(String params){
        OkHttpUtils
                .post()
                .addHeader(VBTApplication.RequestedWith, VBTApplication.XmlHttpRequest)
                .url(Constants.BASE_URL+"v1/account/remarkmessage.html")
                .addParams("params",params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                        ToastUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        if(response.equals("success")){
                        }
                    }
                });
    }



    @Override
    public void onRefresh() {
        //把底部刷新设为不可用
        mMessageNoticeAdapter.setEnableLoadMore(false);
        intCurrentPage=1;
        isLoadRefresh=true;
        requestMessages();
    }

    @Override
    public void onLoadMoreRequested() {
        //把顶部刷新设置为不可用
        if(intCurrentPage<=intPageCount){
            srSwipeRefreshLayout.setEnabled(false);
            isLoadRefresh=false;
            requestMessages();
        }

    }


}
