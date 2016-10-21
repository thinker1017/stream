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

import com.pukai.stream.util.DateUtil;
import com.pukai.stream.util.RedisUtil;

/**
 * 删除redis过期keys
 * @author dell
 * @date 2015年6月9日
 */
public class DelKeyTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setBolt("DelKeyBolt", new DelKeyBolt(), 1);

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(1);
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(1);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("track-delkey", conf, builder.createTopology());

            Utils.sleep(10000000);
            cluster.killTopology("track-delkey");

            cluster.shutdown();
        }
    }

    private static class DelKeyBolt extends BaseBasicBolt {
        private static final long serialVersionUID = 5103893916397645486L;

        private static final Logger logger = LoggerFactory.getLogger(DelKeyBolt.class);

        public void execute(Tuple input, BasicOutputCollector collector) {
            if (input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)
                    && input.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID)
                    && DateUtil.isTimeDelKey()) {
                try {
                    long start = System.currentTimeMillis();
                    Set<String> keys = RedisUtil.getInstance().scan("*" + DateUtil.getDelDay() + "*");
                    long end = System.currentTimeMillis();
                    logger.info("find keys used {} ms, got {} keys need to delete.", end - start, keys.size());
                    for (String key : keys) {
                        RedisUtil.getInstance().del(key);
                        logger.info("del key {}", key);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        @Override
        public Map<String, Object> getComponentConfiguration() {
            Config conf = new Config();
            conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 3600);
            return conf;
        }

        public void declareOutputFields(OutputFieldsDeclarer declarer) { }
    }
}
