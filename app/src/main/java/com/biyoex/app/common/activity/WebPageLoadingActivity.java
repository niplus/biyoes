package com.biyoex.app.common.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.base.BaseActivity;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.utils.GsonUtil;
import com.biyoex.app.common.utils.LollipopFixedWebView;
import com.biyoex.app.common.utils.MPermissionUtils;
import com.biyoex.app.common.utils.ShareWechatDialog;
import com.biyoex.app.home.bean.GuideListBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by LG on 2017/3/18.
 */

public class WebPageLoadingActivity extends BaseActivity{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.pb_details)
    ProgressBar pbDetails;
    @BindView(R.id.wv_details)
    LollipopFixedWebView wvDetails;
    @BindView(R.id.iv_right)
    ImageView IvRight;
    private String strTitle;

    private boolean isShare = false;

    private String strUrl;

    private final String styleDay = "<style type=\"text/css\">\n" +
            "   p {\n" +
            "      line-height: 30px !important;\n" +
            "      text-indent: 0em !important;\n" +
            "    }\n" +
            "\n" +
            "img {"+
            "max-width: 100% !important;"+
            "height: auto !important;"+
            "}"+
            "    p strong {\n" +
            "       padding:20px 0"+
            "    }\n" +
            "   body,p,div,span,img,a{\n"+
            "    color: #333333 !important;\n"+
            "    background: #ffffff!important;\n"+
//            "   border:0;margin:0;padding:0;"+
            "   }\n"+
            "  </style>" ;

    @Override
    protected void initView() {
        tvTitle.setText(strTitle);
        pbDetails.setMax(100);
        pbDetails.setProgress(0);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initData() {
        Intent itWeb = getIntent();
        strTitle = itWeb.getStringExtra("title");
        strUrl = itWeb.getStringExtra("url");
        String type = itWeb.getStringExtra("type");
//        String share = itWeb.getStringExtra("shareType");
        boolean zoom = itWeb.getBooleanExtra("zoom", false);
        IvRight.setVisibility(itWeb.getBooleanExtra("isShare",false)?View.VISIBLE:View.GONE);
        IvRight.setImageResource(R.mipmap.icon_share);
        wvDetails.getSettings().setLoadWithOverviewMode(true);
        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.getSettings().setBlockNetworkImage(false);//解决图片不显示

        wvDetails.getSettings().setLoadsImagesAutomatically(true);
        wvDetails.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wvDetails.getSettings().setUseWideViewPort(true);//适应分辨率
        //有些链接加载不出，需加上这个配置
        wvDetails.getSettings().setDomStorageEnabled(true);
        if (zoom) {
            wvDetails.getSettings().setSupportZoom(true);
            wvDetails.getSettings().setBuiltInZoomControls(true);
        }

        //有些链接加载不出，需加上这个配置
        wvDetails.getSettings().setDomStorageEnabled(true);

        wvDetails.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(strTitle==null){
                    strTitle = view.getTitle();
                    tvTitle.setText(view.getTitle());
                }
//                if(url.contains("broadcast")){
//
//                }
//                else{
//
//                }
                wvDetails.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wvDetails.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        assert type != null;
        if (type.equals("html")) {
            OkHttpUtils
                    .get()
                    .url(Constants.BASE_URL + "v1/articleDetail")
                    .addParams("id", strUrl)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }
                        @Override
                        public void onResponse(String response, int id) throws JSONException {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String content = data.getString("content");
                            GuideListBean.Content contentBean = GsonUtil.GsonToBean(content, GuideListBean.Content.class);

                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String format = sf.format(contentBean.getCreateTime());
                            String htlml = "<!DOCTYPE HTML>\n" +
                                    "<html>\n" +
                                    "<head>\n" +
                                    styleDay +
                                    "<title>"+getString(R.string.announcement_detail)+"</title>\n" +
                                    "<meta charset=\"utf-8\">\n" +
                                    "<meta name=\"format-detection\" content=\"telephone=no\" />\n" +
                                    "<script type=\"text/javascript\">var loadTime =new Date().getTime()</script>\n" +
                                    "<script>!function(a,b){function c(){var b=f.getBoundingClientRect().width;b/i>1180&&(b=1180*i);var c=b/10;f.style.fontSize=c+\"px\",k.rem=a.rem=c}var d,e=a.document,f=e.documentElement,g=e.querySelector('meta[name=\"viewport\"]'),h=e.querySelector('meta[name=\"flexible\"]'),i=0,j=0,k=b.flexible||(b.flexible={});if(g){console.warn(\"将根据已有的meta标签来设置缩放比例\");var l=g.getAttribute(\"content\").match(/initial\\-scale=([\\d\\.]+)/);l&&(j=parseFloat(l[1]),i=parseInt(1/j))}else if(h){var m=h.getAttribute(\"content\");if(m){var n=m.match(/initial\\-dpr=([\\d\\.]+)/),o=m.match(/maximum\\-dpr=([\\d\\.]+)/);n&&(i=parseFloat(n[1]),j=parseFloat((1/i).toFixed(2))),o&&(i=parseFloat(o[1]),j=parseFloat((1/i).toFixed(2)))}}if(!i&&!j){var p=(a.navigator.appVersion.match(/android/gi),a.navigator.appVersion.match(/iphone/gi)),q=a.devicePixelRatio;i=p?q>=3&&(!i||i>=3)?3:q>=2&&(!i||i>=2)?2:1:1,j=1/i}if(f.setAttribute(\"data-dpr\",i),!g)if(g=e.createElement(\"meta\"),g.setAttribute(\"name\",\"viewport\"),g.setAttribute(\"content\",\"initial-scale=\"+j+\", maximum-scale=\"+j+\", minimum-scale=\"+j+\", user-scalable=no\"),f.firstElementChild)f.firstElementChild.appendChild(g);else{var r=e.createElement(\"div\");r.appendChild(g),e.write(r.innerHTML)}a.addEventListener(\"resize\",function(){clearTimeout(d),d=setTimeout(c,300)},!1),a.addEventListener(\"pageshow\",function(a){a.persisted&&(clearTimeout(d),d=setTimeout(c,300))},!1),\"complete\"===e.readyState?e.body.style.fontSize=12*i+\"px\":e.addEventListener(\"DOMContentLoaded\",function(){e.body.style.fontSize=12*i+\"px\"},!1),c(),k.dpr=a.dpr=i,k.refreshRem=c,k.rem2px=function(a){var b=parseFloat(a)*this.rem;return\"string\"==typeof a&&a.match(/rem$/)&&(b+=\"px\"),b},k.px2rem=function(a){var b=parseFloat(a)/this.rem;return\"string\"==typeof a&&a.match(/px$/)&&(b+=\"rem\"),b}}(window,window.lib||(window.lib={}));</script>\n" +
                                    "</head>\n" +
                                    "<body>\n" +
                                    "<div class=\"center_page announcement\">\n" +
                                    "<div class=\"details\">\n" +
                                    "<h1 class=\"title\">" + contentBean.getTitle() + "</h1>\n" +
                                    "<p class=\"time\" style=fonts-size:18px>" + format + "</p>\n" +
                                    "<div class=\"content article_content\"> \n" +
                                    "" + contentBean.getContent() +
                                    "</div>\n" +
                                    "</div>\n" +
                                    "\n" +
                                    "</body>\n" +
                                    "</html>";

//                            wvDetails.clearCache(false);
                            wvDetails.loadDataWithBaseURL(VBTApplication.appPictureUrl, htlml, "text/html", "utf-8", null);
                        }
                    });

        } else{
            try {
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();// 移除
                cookieManager.removeAllCookie();

//                if (!jsessionid.equals("")) {
//                    cookie += "JSESSIONID=" + jsessionid + ";Domain=.zhgtrade.com;Path=/";
//
//                    syncCookie(strUrl, cookie);
//                }
//
//                if (!route.equals("")) {
//                    cookie += "route=" + route + ";Domain=.zhgtrade.com;Path=/";
//                    syncCookie(strUrl, cookie);
//                }
//
//                if (!login_device.equals("")) {
//                    cookie += "login_device=" + login_device + ";Domain=.zhgtrade.com;Path=/";
//                    syncCookie(strUrl, cookie);
//                }

                wvDetails.loadUrl(strUrl);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //
        wvDetails.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //给网页注入js使其隐藏a标签
                String javascript = "javascript:function hideOther() {" +
                        "document.getElementById('gohome_wrapper').style.display=\"none\";" +
                        "}hideOther();";
                wvDetails.loadUrl(javascript);

                String javascriptClass = "javascript:function getClass(parent,sClass){" +
                        "var aEle=parent.getElementsByTagName('div');" +
                        "var aResult=[];" +
                        "var i=0;for(i<0;i<aEle.length;i++){" +
                        "if(aEle[i].className==sClass)\n" +
                        "{" +
                        "aResult.push(aEle[i]);" +
                        "}" +
                        "};" +
                        "return aResult;" +
                        "}";

                wvDetails.loadUrl(javascriptClass);

                String javascriptTab = "javascript:function hideTab() {" +
                        "getClass(document,'weui-tabbar')[0].style.display=\"none\";" +
                        "getClass(document,'show_wrapper')[0].style.display=\"none\";" +
                        "getClass(document,'weui-tab__panel')[0].style.paddingBottom=\"0px\";" +
                        "}hideTab();";

                wvDetails.loadUrl(javascriptTab);

                if (newProgress == 100) {
                    pbDetails.setProgress(newProgress);
                    pbDetails.setVisibility(View.GONE);
                } else {
                    pbDetails.setProgress(newProgress);
                    pbDetails.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    /**
     * 使网页上的图片的大小适配屏幕
     *
     * @param htmltext
     * @return
     */
    private String getNewContent(String htmltext) {
        Document doc;
        try {
            doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("style", "width:100%;height:auto;");
            }
            return doc.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_details;
    }


    @OnClick({R.id.iv_menu, R.id.rl_menu,R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
            case R.id.rl_menu:
                MPermissionUtils.requestPermissionsResult(WebPageLoadingActivity.this, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(WebPageLoadingActivity.this);
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE);
                break;
            case R.id.iv_right:
                new ShareWechatDialog(this,strUrl,strTitle,"VB Global 面向全球的数字资产交易所，致力于为全球用户构建一个更加开放、透明、公平的数字资产服务生态").show();
                break;
        }
    }

    /**
     * 为请求的方式加入cookie请求头
     *
     * @param url
     * @param cookie
     */
    public void syncCookie(String url, String cookie) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);
        String newCookie = cookieManager.getCookie(url);
        //Log.e("newcookie",newCookie);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
            cookieSyncManager.sync();
        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvDetails.canGoBack()) {
            wvDetails.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
