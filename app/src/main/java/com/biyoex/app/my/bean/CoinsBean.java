package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by LG on 2017/6/22.
 */

public class CoinsBean {

    /**
     * pageCount : 1
     * code : 200
     * data : [{"fid":15,"fShortName":"BTC","fname":"比特币","FIsWithDraw":false,"furl":"/upload/others/201606152050058_pzlpu.png","fisShare":true}]
     */

    private int totalCount;
    private int code;
    private List<DataBean> data;

    public static class DataBean {
        /**
         * {
         "name": "BTC",
         "id": 35,
         "key": "USDT",
         "url": "/upload/others/201805261627045_mggug.png",
         "order": 0
         }
         */
        private String name;
        private int id;
        private String key;
        private String url;
        private int order;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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
}
