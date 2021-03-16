package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by xxx on 2018/8/22.
 */

public class FeedbackBean {


    /**
     * code : 200
     * data : [{"id":233,"askType":3,"fuser":{"fid":306667,"fwallet":null,"floginName":"15068810740","floginPassword":"34de5439b66ef6e9c3bb4600372d7ac1","ftradePassword":null,"fnickName":"15068810740","frealName":"倪栋樑","ftelephone":"15068810740","fisTelephoneBind":false,"femail":null,"fidentityNo":"339005199209273315","changePassTime":null,"fgoogleAuthenticator":null,"fgoogleurl":null,"fisTelValidate":false,"fisMailValidate":true,"fgoogleValidate":false,"fpostRealValidate":true,"fhasRealValidate":true,"fIdentityPath3":null,"fIdentityStatus":0,"fhasRealValidateTime":1527903633000,"fscore":null,"fneedFee":true,"zhgOpenId":"d64750ab-90c7-11e8-b7b4-6c92bf649c1a","headImgUrl":null,"wxOpenId":null},"email":"15068810740","title":"111111","status":0,"content":"1223454664646","coinName":null,"delet":0,"address":null,"txid":null,"answer":null,"fileUrl":"/upload/ask/201808221049043_yHEGS.jpg","createTime":1534906184000,"updateTime":null}]
     * totalCount : 1
     * page : 0
     * msg : null
     * id : null
     */

    private int code;
    private int totalCount;
    private int page;
    private Object msg;
    private Object id;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 233
         * askType : 3
         * fuser : {"fid":306667,"fwallet":null,"floginName":"15068810740","floginPassword":"34de5439b66ef6e9c3bb4600372d7ac1","ftradePassword":null,"fnickName":"15068810740","frealName":"倪栋樑","ftelephone":"15068810740","fisTelephoneBind":false,"femail":null,"fidentityNo":"339005199209273315","changePassTime":null,"fgoogleAuthenticator":null,"fgoogleurl":null,"fisTelValidate":false,"fisMailValidate":true,"fgoogleValidate":false,"fpostRealValidate":true,"fhasRealValidate":true,"fIdentityPath3":null,"fIdentityStatus":0,"fhasRealValidateTime":1527903633000,"fscore":null,"fneedFee":true,"zhgOpenId":"d64750ab-90c7-11e8-b7b4-6c92bf649c1a","headImgUrl":null,"wxOpenId":null}
         * email : 15068810740
         * title : 111111
         * status : 0
         * content : 1223454664646
         * coinName : null
         * delet : 0
         * address : null
         * txid : null
         * answer : null
         * fileUrl : /upload/ask/201808221049043_yHEGS.jpg
         * createTime : 1534906184000
         * updateTime : null
         */

        private int id;
        private int askType;
        private String email;
        private String title;
        private int status;
        private String content;
        private String coinName;
        private int delet;
        private String address;
        private String txid;
        private String answer;
        private String fileUrl;
        private long createTime;
        private long updateTime;

        public String getCoinName() {
            return coinName;
        }

        public void setCoinName(String coinName) {
            this.coinName = coinName;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAskType() {
            return askType;
        }

        public void setAskType(int askType) {
            this.askType = askType;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


        public int getDelet() {
            return delet;
        }

        public void setDelet(int delet) {
            this.delet = delet;
        }

        public Object getAddress() {
            return address;
        }


        public String getAnswer() {
            return answer;
        }


        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }


    }
}
