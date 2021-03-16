package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by LG on 2017/6/20.
 */

public class MessagesBean {

    /**
     * pageCount : 1
     * code : 200
     * data : [{"short_title":"实名认证审核通知","id":513036,"time":1494929698000,"title":"实名认证审核通知","content":"恭喜您的实名认证审核通过！"}]
     */

    private int pageCount;
    private int code;
    private List<DataBean> data;

    private int totalCount;
    private int page;
    private String msg;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * short_title : 实名认证审核通知
         * id : 513036
         * time : 1494929698000
         * title : 实名认证审核通知
         * content : 恭喜您的实名认证审核通过！
         */

        private String short_title;
        private String id;
        private long time;
        private String title;
        private String content;

        private int status;

        public String getShort_title() {
            return short_title;
        }

        public void setShort_title(String short_title) {
            this.short_title = short_title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }


}
