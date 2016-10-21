package com.pukai.stream.vo;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.pukai.stream.util.DateUtil;
import com.pukai.stream.util.ValidateUtil;

public class Model {

    private String who;

    private String when;

    private String where;

    private String what;

    private Map<String, String> context;

    private String appid;

    private String ds;

    /**
     * 判断model为当天
     * 
     * @return
     */
    @JSONField(serialize=false)
    public boolean isIntraday() {
        return ValidateUtil.isValid(when) && StringUtils.substringBefore(when, " ").equals(DateUtil.getTodayWithStrike());
    }

    @JSONField(serialize=false)
    public String getDeviceid() {
        return context.get("deviceid");
    }
    
    @JSONField(serialize=false)
    public String getChannelId() {
        return context.get("channelid");
    }
    
    @JSONField(serialize=false)
    public String getServerId() {
        return context.get("serverid");
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
