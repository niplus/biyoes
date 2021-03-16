package com.biyoex.app.my.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.base.BaseActivity;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.utils.GsonUtil;
import com.biyoex.app.my.bean.InviteUserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class InviteCountActivity extends BaseActivity {

    @BindView(R.id.rv_invite_list)
    RecyclerView rvInvite;
    @BindView(R.id.tv_invite_count)
    TextView tvInviteCount;
    @BindView(R.id.tv_realName_count)
    TextView tvRealNameCount;
    @BindView(R.id.tv_reward)
    TextView tvReward;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_coin_name)
    TextView tvCoinName;
    @BindView(R.id.ll_toolbar)
    View llToolBar;

    private BaseQuickAdapter inviteListAdapter;
    private List<InviteUserBean.Intros> inviteList;

    private int page = 1;

    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void initView() {
        tvTitle.setText(R.string.extension_statistics);

        llToolBar.setBackgroundColor(getResources().getColor(R.color.blue_to_weak));
    }

    @Override
    @OnClick(R.id.iv_fall_back)
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvInvite.setLayoutManager(linearLayoutManager);

        inviteList = new ArrayList();
        inviteListAdapter = new BaseQuickAdapter<InviteUserBean.Intros, BaseViewHolder>(R.layout.item_invite_record, inviteList) {
            @Override
            protected void convert(BaseViewHolder helper, InviteUserBean.Intros item) {
                helper.setText(R.id.tv_user, item.getLoginName());
//                helper.setText(R.id.tv_reward, item.getNum() + "");
                helper.setText(R.id.tv_invite_time, simpleDateFormat.format(new Date(item.getCreateTime())));
            }
        };
        inviteListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                requestInviteInfo();
            }
        }, rvInvite);
        rvInvite.setAdapter(inviteListAdapter);
        inviteListAdapter.setEnableLoadMore(true);
        simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

        requestInviteInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void requestInviteInfo()
    {
        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/spread/introLog")
                .addParams("page", page + "")
                .addParams("pageNum", "20")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        int totalCount = jsonObject.getInt("totalCount");
                        if (code == 200)
                        {
                            InviteUserBean inviteUserBean = GsonUtil.GsonToBean(jsonObject.getString("data"), InviteUserBean.class);

                            tvInviteCount.setText(totalCount + getString(R.string.man)+"\n"+ getString(R.string.invite_human_number));
                            tvRealNameCount.setText(inviteUserBean.getHasRealValidateCount() + getString(R.string.man)+"\n" + getString(R.string.real_name_number));
                            if (inviteUserBean.getNum().size() != 0) {
                                tvReward.setText(inviteUserBean.getNum().get(0).getSum());
                                tvCoinName.setText(getString(R.string.invite_reward)+"(" + inviteUserBean.getNum().get(0).getCoinName() + ")");
                            }

                            if (inviteUserBean.getIntros().size() != 0){

                                inviteList.addAll(inviteUserBean.getIntros());
                            }

                            if (totalCount == inviteList.size()){
                                inviteListAdapter.setEnableLoadMore(false);
                            }

                            inviteListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void requestCoinName()
    {
        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/spread/getInfo")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String coinName = data.getString("introCoinName");
                            tvReward.setText(getString(R.string.invite_reward)+"(" + coinName + ")");
                        }
                    }
                });
    }
}
