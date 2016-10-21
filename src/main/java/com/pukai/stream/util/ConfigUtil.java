package com.pukai.stream.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	
	private static ResourceBundle bundle = null;
	
	private static ConfigUtil config = null;

	private ConfigUtil() {
		try {
			bundle = ResourceBundle.getBundle("conf");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public static ConfigUtil newInstance() {
		if (null == config) {
			config = new ConfigUtil();
		}
		return config;
	}
	
	public String[] getStringArray(String key) {
		return bundle.getString(key).split(",");
	}
	
	public String getString(String key) {
		return bundle.getString(key);
	}
	
	public int getInt(String key) {
		return Integer.parseInt(bundle.getString(key));
	}
}
