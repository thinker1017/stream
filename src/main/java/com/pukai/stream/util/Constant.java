package com.pukai.stream.util;


public class Constant {
	private static ConfigUtil config = ConfigUtil.newInstance();
	
	public final static String kafkaZookeeper = config.getString("kafka.zookeeper");
	
	public final static String redisWriter = config.getString("redis.writer");
	public final static String redisReader = config.getString("redis.reader");
	
	public final static String phoenixDriver = config.getString("phoenix.driver");
	public final static String phoenixURL = config.getString("phoenix.url");
	public final static String phoenixUsername = config.getString("phoenix.username");
	public final static String phoenixPassword = config.getString("phoenix.password");
}
