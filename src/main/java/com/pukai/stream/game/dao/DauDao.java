package com.pukai.stream.game.dao;

import com.pukai.stream.util.RedisUtil;
import com.pukai.stream.vo.Model;
import com.pukai.stream.vo.RedisKeyGenerator;

public class DauDao {

	private static DauDao instance;

	private DauDao() { }

	public static DauDao getInstance() {
		if (null == instance) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new DauDao();
		}
	}

	public void exec(Model model) {
	    RedisKeyGenerator keyGet = new RedisKeyGenerator(model);
	    
	    RedisUtil.getInstance().pfadd(keyGet.getDAUChannelCntKey(), model.getWho());
        RedisUtil.getInstance().pfadd(keyGet.getDAUChannelHourCntKey(), model.getWho());
        
        RedisUtil.getInstance().pfadd(keyGet.getDAUServerCntKey(), model.getWho());
        RedisUtil.getInstance().pfadd(keyGet.getDAUServerHourCntKey(), model.getWho());
	}
}
