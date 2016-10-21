package com.pukai.stream.game.topology;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.client.ConnectStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.pukai.stream.game.dao.DauDao;
import com.pukai.stream.spout.RYKafkaSpout;
import com.pukai.stream.util.Constant;
import com.pukai.stream.util.RedisUtil;
import com.pukai.stream.util.StringUtil;
import com.pukai.stream.vo.Model;
import com.pukai.stream.vo.RedisKeyGenerator;
/**
 * 统计活跃用户数
 * @author dell
 * @date 2015年6月9日
 */
public class DAUTopology {

	public static void main(String[] args) throws Exception {
		String topicName = "game-loggedin";
		
		TopologyBuilder builder = new TopologyBuilder();
		
		builder.setSpout("LoginSpout", RYKafkaSpout.getKafkaSpout(topicName, DAUTopology.class.getCanonicalName()), 1);
		builder.setBolt("LoginBolt", new LoginBolt(), 5).shuffleGrouping("LoginSpout");

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(1);
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(1);
			
			ConnectStringParser connectStringParser = new ConnectStringParser(Constant.kafkaZookeeper);
            List<InetSocketAddress> serverInetAddresses = connectStringParser.getServerAddresses();
            List<String> serverAddresses = new ArrayList<String>(serverInetAddresses.size());
            Integer zkPort = serverInetAddresses.get(0).getPort();
            for (InetSocketAddress serverInetAddress : serverInetAddresses) {
                serverAddresses.add(serverInetAddress.getHostName());
            }
            
            conf.put(Config.STORM_ZOOKEEPER_SERVERS, serverAddresses);
            conf.put(Config.STORM_ZOOKEEPER_PORT, zkPort);
			
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(topicName, conf, builder.createTopology());
			Thread.sleep(10000000);
			cluster.shutdown();
		}
	}

	private static class LoginBolt extends BaseBasicBolt {
	    private static final Logger logger = LoggerFactory.getLogger(LoginBolt.class);
	    
	    private static final long serialVersionUID = -2667711090591149427L;

		public void execute(Tuple input, BasicOutputCollector collector) {
            try {
                Model model = StringUtil.Str2Model(input.getString(0));
                
                if (model.isIntraday()) {
                    RedisKeyGenerator keyGet = new RedisKeyGenerator(model);
                    
                    DauDao.getInstance().exec(model);
                    
                    RedisUtil.getInstance().sadd(keyGet.getChannelsKey(), keyGet.getChannelId());
                    RedisUtil.getInstance().sadd(keyGet.getServersKey(), keyGet.getServerId());
                    
                    RedisUtil.getInstance().sadd(keyGet.getAppidsKey(), model.getAppid());
                } else {
                    logger.warn("The data is not intraday! rawdata: {}", input.getString(0));
                }
            } catch (Exception e) {
                logger.error(input.getString(0), e);
            }
        }

		public void declareOutputFields(OutputFieldsDeclarer declarer) { }
	}
}
