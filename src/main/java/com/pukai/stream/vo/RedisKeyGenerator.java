package com.pukai.stream.vo;

import java.util.Date;
import com.pukai.stream.util.DateUtil;
import com.pukai.stream.util.StringUtil;

public class RedisKeyGenerator {
    private String appid;
    private String channelid;
    private String serverid;
    
    private String ds;
	private String minute;
	private String hour;

	public RedisKeyGenerator(Model model) {
	    appid = model.getAppid();
	    channelid = model.getChannelId().toLowerCase();
	    serverid = model.getServerId().toLowerCase();
	    ds = model.getDs();
		
		Date when = DateUtil.parseDate(model.getWhen());
		hour = DateUtil.getHour(when);
		minute = DateUtil.get5MFormat(when);
	}

	/******************* 新增设备数  ********************/
	/**
     * 获取游戏&每渠道&每天&新增设备数key
     * @return
     */
	public String getInstallChannelCntKey() {
		return StringUtil.format("game_channel_install:%s:%s:%s", appid, channelid, ds);
	}

	/**
     * 获取游戏&每渠道&每小时&新增设备数key
     * @return
     */
	public String getInstallChannelHourCntKey() {
		return StringUtil.format("game_channel_hour_install:%s:%s:%s", appid, channelid, hour);
	}

	/**
     * 获取游戏&每服务器&每天&新增设备数key
     * @return
     */
	public String getInstallServerCntKey() {
		return StringUtil.format("game_server_install:%s:%s:%s", appid, serverid, ds);
	}

	/**
     * 获取游戏&每服务器&每小时&新增设备数key
     * @return
     */
	public String getInstallServerHourCntKey() {
		return StringUtil.format("game_server_hour_install:%s:%s:%s", appid, serverid, hour);
	}

	/******************* 注册人数  ********************/
	/**
     * 获取游戏&每渠道&每天&注册人数key
     * @return
     */
	public String getRegisterChannelCntKey() {
		return StringUtil.format("game_channel_reged:%s:%s:%s", appid, channelid, ds);
	}

	/**
     * 获取游戏&每渠道&每小时&注册人数key
     * @return
     */
	public String getRegisterChannelHourCntKey() {
		return StringUtil.format("game_channel_hour_reged:%s:%s:%s", appid, channelid, hour);
	}

	/**
     * 获取游戏&每服务器&每天&注册人数key
     * @return
     */
	public String getRegisterServerCntKey() {
		return StringUtil.format("game_server_reged:%s:%s:%s", appid, serverid, ds);
	}

	/**
     * 获取游戏&每服务器&每小时&注册人数key
     * @return
     */
	public String getRegisterServerHourCntKey() {
		return StringUtil.format("game_server_hour_reged:%s:%s:%s", appid, serverid, hour);
	}

	/******************* 活跃人数  ********************/
	/**
     * 获取游戏&每渠道&每天&活跃人数key
     * @return
     */
	public String getDAUChannelCntKey() {
		return StringUtil.format("game_channel_dau:%s:%s:%s", appid, channelid, ds);
	}

	/**
     * 获取游戏&每渠道&每小时&活跃人数key
     * @return
     */
	public String getDAUChannelHourCntKey() {
		return StringUtil.format("game_channel_hour_dau:%s:%s:%s", appid, channelid, hour);
	}

	/**
     * 获取游戏&每服务器&每天&活跃人数key
     * @return
     */
	public String getDAUServerCntKey() {
		return StringUtil.format("game_server_dau:%s:%s:%s", appid, serverid, ds);
	}

	/**
     * 获取游戏&每服务器&每小时&活跃人数key
     * @return
     */
	public String getDAUServerHourCntKey() {
		return StringUtil.format("game_server_hour_dau:%s:%s:%s", appid, serverid, hour);
	}

	/******************* 付费人数  ********************/
	/**
	 * 获取游戏&每渠道&每天&付费人数key
	 * @return
	 */
	public String getPaymentChannelCntKey() {
		return StringUtil.format("game_channel_payers:%s:%s:%s", appid, channelid, ds);
	}

	/**
	 * 获取游戏&每渠道&每小时&付费人数key
	 * @return
	 */
	public String getPaymentChannelHourCntKey() {
		return StringUtil.format("game_channel_hour_payers:%s:%s:%s", appid, channelid, hour);
	}

	/**
	 * 获取游戏&每服务器&每天&付费人数key
	 * @return
	 */
	public String getPaymentServerCntKey() {
		return StringUtil.format("game_server_payers:%s:%s:%s", appid, serverid, ds);
	}

	/**
     * 获取游戏&每服务器&每小时&付费人数key
     * @return
     */
	public String getPaymentServerHourCntKey() {
		return StringUtil.format("game_server_hour_payers:%s:%s:%s", appid, serverid, hour);
	}

	/******************* 付费金额  ********************/
	/**
	 * 获取游戏&每渠道&每天&付费金额key
	 * @return
	 */
	public String getPaymentChannelSumKey() {
		return StringUtil.format("game_channel_amount:%s:%s:%s", appid, channelid, ds);
	}

	/**
     * 获取游戏&每渠道&每小时&付费金额key
     * @return
     */
	public String getPaymentChannelHourSumKey() {
		return StringUtil.format("game_channel_hour_amount:%s:%s:%s", appid, channelid, hour);
	}

	/**
     * 获取游戏&每服务器&每天&付费金额key
     * @return
     */
	public String getPaymentServerSumKey() {
		return StringUtil.format("game_server_amount:%s:%s:%s", appid, serverid, ds);
	}

	/**
     * 获取游戏&每服务器&每小时&付费金额key
     * @return
     */
	public String getPaymentServerHourSumKey() {
		return StringUtil.format("game_server_hour_amount:%s:%s:%s", appid, serverid, hour);
	}

	/**
     * 获取游戏&每服务器&每五分钟&实时在线key
     * @return
     */
	public String getHeartBeatServerCnt5MKey() {
		return StringUtil.format("game_server_hb:%s:%s:%s", appid, serverid, minute);
	}
	
	/**
     * 获取游戏所有渠道key
     * @return
     */
	public String getChannelsKey() {
		return StringUtil.format("game_channels:%s:%s", appid, ds);
	}
	
	/**
	 * 获取游戏所有服务器key
	 * @return
	 */
	public String getServersKey() {
		return StringUtil.format("game_servers:%s:%s", appid, ds);
	}
	
	public String getAppidsKey() {
	    return StringUtil.format("game_appids:%s", ds);
	}
	
	public String getChannelId() {
	    return channelid;
	}
	
	public String getServerId() {
	    return serverid;
	}
  public static void main(String[] args){
	  System.out.println(StringUtil.format("game_servers:%s:%s", "1313", "2015-08-09"));
  }
}
