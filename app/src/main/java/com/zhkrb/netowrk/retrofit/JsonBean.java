package com.zhkrb.netowrk.retrofit;

public class JsonBean {

    private int ret;
    private String msg;
    private String data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess(){
//        return state == 1;
        return ret == ApiException.ApiErrorCode.RESPONSE_SUCCESS;
    }
}
