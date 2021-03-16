package com.biyoex.app.common.bean

class HomeNoticeBean (
        val code: Int,
        val data: List<Notice>
)

class Notice(
        val id: Int,
        val type: Int,
        val title: String,
        val enTitle: String,
        val content: String,
        val enContent: String,
        val top: String,
        val createTime:Long
)