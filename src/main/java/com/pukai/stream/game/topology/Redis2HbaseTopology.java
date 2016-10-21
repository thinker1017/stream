package com.pukai.stream.game.topology;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;

import com.pukai.stream.game.dao.GameDao;
import com.pukai.stream.util.DateUtil;
import com.pukai.stream.util.RedisUtil;
/**
 * redis数据持久化到hbase
 * @author dell
 * @date 2015年6月9日
 */
public class Redis2HbaseTopology {
	
	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		
		builder.setBolt("Redis2HbaseBolt", new Redis2HbaseBolt(), 1);

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(1);
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(1);

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("game_redis2hbase", conf, builder.createTopology());

			Utils.sleep(10000000);
			cluster.killTopology("game_redis2hbase");

			cluster.shutdown();
		}
	}
	//redis2hbase做同步的时候只同步当天的数据
	private static class Redis2HbaseBolt extends BaseBasicBolt {
        private static final Logger logger = LoggerFactory.getLogger(Redis2HbaseBolt.class);
        
        private static final long serialVersionUID = -5772240973135869116L;

        public void execute(Tuple input, BasicOutputCollector collector) {
            if (input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)
                    && input.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID)) {
                try {
                    String ds = DateUtil.getTodayWithStrike();
                    Set<String> appids = RedisUtil.getInstance().smembers("game_appids:" + ds);
                    
                    GameDao.getInstance().start();
                    
                    for (String appid : appids) {
                        Set<String> channelids = RedisUtil.getInstance().smembers("game_channels:" + appid + ":" + ds);
                        
                        Set<String> serverids = RedisUtil.getInstance().smembers("game_servers:" + appid + ":" + ds);
                        
                        for (String channelid : channelids) {
                            GameDao.getInstance().upsertChannel(appid, channelid, ds);
                            
                            GameDao.getInstance().upsertChannelHour(appid, channelid);
                            
                            GameDao.getInstance().upsertChannelInfo(appid, channelid, ds);
                        }
                        
                        for (String serverid : serverids) {
                            GameDao.getInstance().upsertServer(appid, serverid, ds);
                            
                            GameDao.getInstance().upsertServerHour(appid, serverid);
                            
                            GameDao.getInstance().upsertHb(appid, serverid);
                            
                            GameDao.getInstance().upsertServerInfo(appid, serverid, ds);
                        }
                    }
                    
                    GameDao.getInstance().finish();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        
        @Override
        public Map<String, Object> getComponentConfiguration() {
            Config conf = new Config();
            //同步最近半小时的数据，就是说现在是16:15的话，
           //同步的时间节点是15:50, 15:55, 16:00, 16:05, 16:10, 16:15
           //这几个时间节点的,前面的即使增加了也不同步了
            conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 30);
            return conf;
        }

        public void declareOutputFields(OutputFieldsDeclarer declarer) { }
    }
}
