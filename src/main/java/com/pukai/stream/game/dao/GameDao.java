package com.pukai.stream.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pukai.stream.util.DateUtil;
import com.pukai.stream.util.PhoenixUtil;
import com.pukai.stream.util.RedisUtil;

public class GameDao {

    private static GameDao instance;
    
    private Connection conn;
    
    private GameDao() { }

    public static GameDao getInstance() {
        if (null == instance) {
            syncInit();
        }
        return instance;
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new GameDao();
        }
    }
    
    public void start() throws SQLException {
        conn = PhoenixUtil.getInstance().getConn();
        conn.setAutoCommit(true);
    }
    
    public void finish() throws SQLException {
        conn.commit();
        PhoenixUtil.getInstance().release(null, conn);
    }
    
    public void upsertChannel(String appid, String channelid, String ds) throws SQLException {
        int install = new Long(RedisUtil.getInstance().pfcount("game_channel_install:" + appid + ":" + channelid + ":" + ds)).intValue();
        int reged = new Long(RedisUtil.getInstance().pfcount("game_channel_reged:" + appid + ":" + channelid + ":" + ds)).intValue();
        int dau = new Long(RedisUtil.getInstance().pfcount("game_channel_dau:" + appid + ":" + channelid + ":" + ds)).intValue();
        int payers = new Long(RedisUtil.getInstance().pfcount("game_channel_payers:" + appid + ":" + channelid + ":" + ds)).intValue();
        
        String amountStr = RedisUtil.getInstance().get("game_channel_amount:" + appid + ":" + channelid + ":" + ds);
        
        float amount = 0;
        if (amountStr != null) {
            amount = Float.parseFloat(amountStr);
        }
        //写到hbase
        PreparedStatement pstmt = PhoenixUtil.getInstance().getStatement(conn, "upsert into realtime_channel (appid, ds, channel, install, reged, dau, amount, payers) values (?, ?, ?, ?, ?, ?, ?, ?)");
        
        pstmt.setString(1, appid);
        pstmt.setString(2, ds);
        pstmt.setString(3, channelid.toLowerCase());
        pstmt.setInt(4, install);
        pstmt.setInt(5, reged);
        pstmt.setInt(6, dau);
        pstmt.setFloat(7, amount);
        pstmt.setInt(8, payers);
        
        pstmt.executeUpdate();
        
        PhoenixUtil.getInstance().release(pstmt, null);
    }
    
    public void upsertChannelHour(String appid, String channelid) throws SQLException {
        List<String> hours = DateUtil.getNeedSyncHourList();
        
        for (String hour : hours) {
            upsertChannelHour(appid, channelid, hour);
        }
    }
    
    public void upsertChannelHour(String appid, String channelid, String hour) throws SQLException {
        int install = new Long(RedisUtil.getInstance().pfcount("game_channel_hour_install:" + appid + ":" + channelid + ":" + hour)).intValue();
        int reged = new Long(RedisUtil.getInstance().pfcount("game_channel_hour_reged:" + appid + ":" + channelid + ":" + hour)).intValue();
        int dau = new Long(RedisUtil.getInstance().pfcount("game_channel_hour_dau:" + appid + ":" + channelid + ":" + hour)).intValue();
        int payers = new Long(RedisUtil.getInstance().pfcount("game_channel_hour_payers:" + appid + ":" + channelid + ":" + hour)).intValue();
        
        String amountStr = RedisUtil.getInstance().get("game_channel_hour_amount:" + appid + ":" + channelid + ":" + hour);
        
        float amount = 0;
        if (amountStr != null) {
            amount = Float.parseFloat(amountStr);
        }
        
        PreparedStatement pstmt = PhoenixUtil.getInstance().getStatement(conn, "upsert into realtime_channel_hour (appid, ds, channel, hour, install, reged, dau, amount, payers) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        
        pstmt.setString(1, appid);
        // ds
        pstmt.setString(2, StringUtils.substringBefore(hour, "T"));
        pstmt.setString(3, channelid.toLowerCase());
        // hour
        pstmt.setString(4, StringUtils.substringBetween(hour, "T", "Z"));
        pstmt.setInt(5, install);
        pstmt.setInt(6, reged);
        pstmt.setInt(7, dau);
        pstmt.setFloat(8, amount);
        pstmt.setInt(9, payers);
        
        pstmt.executeUpdate();
        
        PhoenixUtil.getInstance().release(pstmt, null);
    }
    
    public void upsertServer(String appid, String serverid, String ds) throws SQLException {
        int install = new Long(RedisUtil.getInstance().pfcount("game_server_install:" + appid + ":" + serverid + ":" + ds)).intValue();
        int reged = new Long(RedisUtil.getInstance().pfcount("game_server_reged:" + appid + ":" + serverid + ":" + ds)).intValue();
        int dau = new Long(RedisUtil.getInstance().pfcount("game_server_dau:" + appid + ":" + serverid + ":" + ds)).intValue();
        int payers = new Long(RedisUtil.getInstance().pfcount("game_server_payers:" + appid + ":" + serverid + ":" + ds)).intValue();
        
        String amountStr = RedisUtil.getInstance().get("game_server_amount:" + appid + ":" + serverid + ":" + ds);
        
        float amount = 0;
        if (amountStr != null) {
            amount = Float.parseFloat(amountStr);
        }
        
        PreparedStatement pstmt = PhoenixUtil.getInstance().getStatement(conn, "upsert into realtime_server (appid, ds, serverid, install, reged, dau, amount, payers) values (?, ?, ?, ?, ?, ?, ?, ?)");
        
        pstmt.setString(1, appid);
        pstmt.setString(2, ds);
        pstmt.setString(3, serverid.toLowerCase());
        pstmt.setInt(4, install);
        pstmt.setInt(5, reged);
        pstmt.setInt(6, dau);
        pstmt.setFloat(7, amount);
        pstmt.setInt(8, payers);
        
        pstmt.executeUpdate();
        
        PhoenixUtil.getInstance().release(pstmt, null);
    }
    
    public void upsertServerHour(String appid, String serverid) throws SQLException {
        List<String> hours = DateUtil.getNeedSyncHourList();
        
        for (String hour : hours) {
            upsertServerHour(appid, serverid, hour);
        }
    }
    
    public void upsertServerHour(String appid, String serverid, String hour) throws SQLException {
        int install = new Long(RedisUtil.getInstance().pfcount("game_server_hour_install:" + appid + ":" + serverid + ":" + hour)).intValue();
        int reged = new Long(RedisUtil.getInstance().pfcount("game_server_hour_reged:" + appid + ":" + serverid + ":" + hour)).intValue();
        int dau = new Long(RedisUtil.getInstance().pfcount("game_server_hour_dau:" + appid + ":" + serverid + ":" + hour)).intValue();
        int payers = new Long(RedisUtil.getInstance().pfcount("game_server_hour_payers:" + appid + ":" + serverid + ":" + hour)).intValue();
        
        String amountStr = RedisUtil.getInstance().get("game_server_hour_amount:" + appid + ":" + serverid + ":" + hour);
        
        float amount = 0;
        if (amountStr != null) {
            amount = Float.parseFloat(amountStr);
        }
        
        PreparedStatement pstmt = PhoenixUtil.getInstance().getStatement(conn, "upsert into realtime_server_hour (appid, ds, serverid, hour, install, reged, dau, amount, payers) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        
        pstmt.setString(1, appid);
        // ds
        pstmt.setString(2, StringUtils.substringBefore(hour, "T"));
        pstmt.setString(3, serverid.toLowerCase());
        // hour
        pstmt.setString(4, StringUtils.substringBetween(hour, "T", "Z"));
        pstmt.setInt(5, install);
        pstmt.setInt(6, reged);
        pstmt.setInt(7, dau);
        pstmt.setFloat(8, amount);
        pstmt.setInt(9, payers);
        
        pstmt.executeUpdate();
        
        PhoenixUtil.getInstance().release(pstmt, null);
    }
    
    public void upsertHb(String appid, String serverid) throws SQLException {
        List<String> minutes = DateUtil.getNeedSync5MList();
        
        for (String minute : minutes) {
            upsertHb(appid, serverid, minute);
        }
    }
    
    public void upsertHb(String appid, String serverid, String minute) throws SQLException {
        int users = new Long(RedisUtil.getInstance().pfcount("game_server_hb:" + appid + ":" + serverid + ":" + minute)).intValue();
        
        PreparedStatement pstmt = PhoenixUtil.getInstance().getStatement(conn, "upsert into realtime_hb (appid, ds, serverid, minutes, users) values (?, ?, ?, ?, ?)");
        
        pstmt.setString(1, appid);
        // ds
        pstmt.setString(2, StringUtils.substringBefore(minute, "T"));
        pstmt.setString(3, serverid.toLowerCase());
        // minute
        pstmt.setString(4, StringUtils.substringBetween(minute, "T", "Z"));
        pstmt.setInt(5, users);
        
        pstmt.executeUpdate();
        
        PhoenixUtil.getInstance().release(pstmt, null);
    }
    
    public void upsertChannelInfo(String appid, String channelid, String ds) throws SQLException {
        PreparedStatement pstmt = PhoenixUtil.getInstance().getStatement(conn, "upsert into realtime_channelinfo_new (appid, ds, channel) values (?, ?, ?)");
        
        pstmt.setString(1, appid);
        pstmt.setString(2, ds);
        pstmt.setString(3, channelid.toLowerCase());
        
        pstmt.executeUpdate();
        
        PhoenixUtil.getInstance().release(pstmt, null);
    }
    
    public void upsertServerInfo(String appid, String serverid, String ds) throws SQLException {
        PreparedStatement pstmt = PhoenixUtil.getInstance().getStatement(conn, "upsert into realtime_serveropendate_new (appid, ds, serverid, opendate) values (?, ?, ?, ?)");
        
        pstmt.setString(1, appid);
        pstmt.setString(2, ds);
        pstmt.setString(3, serverid.toLowerCase());
        pstmt.setString(4, ds);
        
        pstmt.executeUpdate();
        
        PhoenixUtil.getInstance().release(pstmt, null);
    }
}
