package com.biyoex.app.common.okhttp;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.biyoex.app.common.Constants;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
/**
 * Created by xxx on 2018/10/10.
 */

public class SocketUtils {
    private static SocketUtils ins;
    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    private WebSocketListener mWebSocketListener;
    private WebSocket mWebSocket;
    private String TAG = "SocketUtils";
    private String url;

    //断开连接与抛异常进行请求的次数
    private int intLoopCount = 0;
    //超过请求次数之后每隔多少秒再起去重新连接
    private boolean isOverLoopCount = false;

    Timer timer;

    private List<OnReceiveMessageListener> onReceiveMessageListeners;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1649) {
                if (mWebSocket != null) {
                    android.util.Log.i(TAG, "handleMessage: ");
                    mWebSocket.send("2");
                }
            }
        }
    };

    private SocketUtils() {
        if (onReceiveMessageListeners == null) {
            onReceiveMessageListeners = new ArrayList<>(1);
        }
    }

    public static SocketUtils getIns() {
        if (ins == null) {
            SocketUtils socketUtilsWeakReference = new SocketUtils();
            ins = socketUtilsWeakReference;
        }
        return ins;
    }


    /**
     * 长连接服务器
     *
     * @param token
     */
    public void requestWebSocketInfo(String id, String token) {
//        SocketUtils.getIns().removeSocketClose();
//        mWebSocketListener = null;
        if (mOkHttpClient != null) {
            android.util.Log.i(TAG, "断开所有连接: ");
            mOkHttpClient.dispatcher().cancelAll();
        }
        String jsessionid = SharedPreferencesUtils.getInstance().getString("JSESSIONID", "");
        String route = SharedPreferencesUtils.getInstance().getString("route", "");
        String login_device = SharedPreferencesUtils.getInstance().getString("login_device", "");
        final String uid = SharedPreferencesUtils.getInstance().getString("uid", "");
        String cookie = "";
        if (!jsessionid.equals("")) {
            cookie += "JSESSIONID=" + jsessionid + "; ";
        }
        if (!route.equals("")) {
            cookie += "route=" + route + "; ";
        }

        if (!login_device.equals("")) {
            cookie += "login_device=" + login_device + "; ";
        }

        if (!uid.equals("")) {
            cookie += "uid=" + uid;
        }
        url = Constants.WEBSOCKET_URL + "symbol=" + id + "&deep=4&token=" + token + "&EIO=3&transport=websocket";
        mOkHttpClient = RetrofitHelper.getIns().getOkHttpClient();
        mRequest = new Request.Builder()
//                .url("ws://47.104.111.186:8889/socket.io/?symbol=" + id + "&deep=4&token=" + token + "&EIO=3&transport=websocket")
                .url(url)
                .addHeader("Cookie", cookie)
                .build();
        android.util.Log.i(TAG, "长链接地址: " + url);
        mWebSocketListener = new WebSocketListener() {
            //连接成功
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                if (mWebSocket != null) {
                    mWebSocket.cancel();
                }
                String url ;
                if(SocketUtils.this.url.contains("wss")){
                   url =   SocketUtils.this.url.replace("wss", "https");
                }
                else {
                   url =  SocketUtils.this.url.replace("ws", "http");
                }
                //当websocket还没连上的时候退出页面，会导致websocket断开连接失败
                if (TextUtils.isEmpty(url) || !url.equals(response.request().url().toString())) {
                    Log.i("connect error");
                    //担心两次websocket相同，测试一段时间看日志
                    if (webSocket == mWebSocket) {
                        Log.e("websocket maybe the same one");
                    }
                    webSocket.cancel();
                    return;
                }
                mWebSocket = webSocket;
                mWebSocket.send("40" + "/trade");
                timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 0x1649;
                        handler.sendMessage(message);
                    }
                };
                android.util.Log.i(TAG, "onOpen: ");
                timer.schedule(task, 25000, 25000);
                //handler.sendEmptyMessage(0x1649);
            }

            //服务器推送过来的消息
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
//                android.util.Log.i("服务器推送过来的消息", "onMessage: ");
                int index = text.indexOf(",[");
                if (index != -1) {
                    String message = text.substring(index + 1);
                    if (onReceiveMessageListeners.size() != 0) {
                        for (OnReceiveMessageListener onReceiveMessageListener : onReceiveMessageListeners) {
                            android.util.Log.i(TAG, "推送消息: " + message);
                            onReceiveMessageListener.onMessage(message);
                        }
                    }
                }
            }

            //关闭连接中
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.d("socket close");
                android.util.Log.i(TAG, "onClosing: ");
                mWebSocket = null;

                if (code == 1000) {
                    return;
                }

                if (timer != null) {
                    timer.cancel();
                }

                //当异常情况、或者断开连接进行重新请求连接，并且最大次数为5次，大于5次以后进行每5秒进行重连操作
                intLoopCount++;

                if (intLoopCount <= 5) {
                    mOkHttpClient.newWebSocket(mRequest, mWebSocketListener);
                }

                if (intLoopCount == 6) {
                    isOverLoopCount = true;
                }

                if (isOverLoopCount) {
                    handler.postDelayed(() -> mOkHttpClient.newWebSocket(mRequest, mWebSocketListener), 5000);
                }
            }

            //抛异常会进入其中
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                Log.d("socket onFailure : " + t.toString());
                android.util.Log.i(TAG, "onFailure: ");
                mWebSocket = null;
                if (timer != null) {
                    timer.cancel();
                }
                if (t.getMessage() != null) {

                }
            }
        };
        mOkHttpClient.newWebSocket(mRequest, mWebSocketListener);
    }

    public void setOnReceiveMessageListener(OnReceiveMessageListener onReceiveMessageListener) {
        try {
            if (!onReceiveMessageListeners.contains(onReceiveMessageListener)) {
                onReceiveMessageListeners.add(onReceiveMessageListener);
                if (mWebSocket != null) {
                    mWebSocket.send("40/trade");
                    Log.i("send 40/trade");
                }
            }
        } catch (Exception e) {
        }
    }


    public void removeListener(OnReceiveMessageListener onReceiveMessageListener) {
//        try {
        if (onReceiveMessageListeners.contains(onReceiveMessageListener)) {
            onReceiveMessageListeners.remove(onReceiveMessageListener);
        }
        if (onReceiveMessageListeners.size() == 0) {
            url = "";
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (mWebSocket != null) {
//                mWebSocket.close(1000, VBTApplication.getContext().getString(R.string.close_connect));
                try {
                    Log.i("socket cancel");
                    android.util.Log.i(TAG, "removeListener: ");
                    mWebSocket.close(1000, null);
                    mWebSocket.cancel();
                } catch (Exception e) {

                }
//                mOkHttpClient.dispatcher().executorService().shutdown();
//                mWebSocket = null;
            }
        }
//        } catch (Exception e) {
//
//        }
    }

    public void removeSocketClose() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (mWebSocket != null) {
                mWebSocket.close(1000, null);
                mWebSocketListener = null;
                mWebSocket.cancel();

//            mWebSocket.cancel();
            }
        } catch (Exception e) {
        }
    }

    public interface OnReceiveMessageListener {
        void onMessage(String text);
    }

}
