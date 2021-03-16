package com.biyoex.app.my.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.biyoex.app.R
import com.biyoex.app.common.utils.log.Log
import com.livechatinc.inappchat.ChatWindowConfiguration
import com.livechatinc.inappchat.ChatWindowErrorType
import com.livechatinc.inappchat.ChatWindowView
import com.livechatinc.inappchat.models.NewMessageModel


class ContactActivity : AppCompatActivity() {

    var fullScreenChatWindow: ChatWindowView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val map = HashMap<String, String>()
//        map["id"] = "123"
        val configuration = ChatWindowConfiguration(
                "12453048",
                "",
                " ",
                " ",
                null
        )

        if (fullScreenChatWindow == null) {
            fullScreenChatWindow = ChatWindowView.createAndAttachChatWindowInstance(this);
            fullScreenChatWindow!!.setUpWindow(configuration);
            fullScreenChatWindow!!.setUpListener(object : ChatWindowView.ChatWindowEventsListener{
                override fun onChatWindowVisibilityChanged(visible: Boolean) {
                    Log.i("nidongliang onChatWindowVisibilityChanged visible: $visible")
                }

                override fun onNewMessage(message: NewMessageModel?, windowVisible: Boolean) {
                    Log.i("nidongliang onNewMessage message: ${message?.text}")
                }

                override fun onStartFilePickerActivity(intent: Intent?, requestCode: Int) {
                    startActivityForResult(intent, requestCode)
                    Log.i("nidongliang onStartFilePickerActivity")
                }

                override fun onError(errorType: ChatWindowErrorType?, errorCode: Int, errorDescription: String?): Boolean {
                    Log.i("nidongliang onError")
                    return false
                }

                override fun handleUri(uri: Uri?): Boolean {
                    Log.i("nidongliang handleUri")
                    return false
                }

            });
            fullScreenChatWindow!!.initialize();
        }
        fullScreenChatWindow?.showChatWindow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (fullScreenChatWindow != null) fullScreenChatWindow!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}