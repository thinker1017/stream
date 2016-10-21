package com.pukai.stream.spout;

import java.util.UUID;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.spout.SchemeAsMultiScheme;

import com.pukai.stream.util.Constant;
/**
 * 读取kafka数据
 * @author dell
 * @date 2015年6月9日
 */
public class RYKafkaSpout {
	public static KafkaSpout getKafkaSpout(String topicName) {
	    BrokerHosts brokerHosts = new ZkHosts(Constant.kafkaZookeeper);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topicName, "/"
                + topicName, UUID.randomUUID().toString());
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        return new KafkaSpout(spoutConfig);
	}

	public static KafkaSpout getKafkaSpout(String topicName, String id) {
		return getKafkaSpout(topicName, id, false);
	}
	
	public static KafkaSpout getKafkaSpout(String topicName, String id,boolean forceFromStart) {
		BrokerHosts brokerHosts = new ZkHosts(Constant.kafkaZookeeper);
		SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topicName,
				"/stream/" + topicName, id);
		spoutConfig.forceFromStart=forceFromStart;
		
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		return new KafkaSpout(spoutConfig);
	}
}
