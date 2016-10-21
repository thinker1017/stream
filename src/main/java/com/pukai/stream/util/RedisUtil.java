package com.pukai.stream.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(RedisUtil.class);

	private static RedisUtil instance;

	private static JedisPool writerPool;
	private static JedisPool readerPool;

	private RedisUtil() {
	}

	public static RedisUtil getInstance() {
		if (null == instance) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (instance == null) {

			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			config.setTestOnBorrow(true);

			String writerTmp[] = StringUtils.split(Constant.redisWriter, ":");
			writerPool = new JedisPool(config, writerTmp[0],
					Integer.parseInt(writerTmp[1]), 5000);

			String readerTmp[] = StringUtils.split(Constant.redisReader, ":");
			readerPool = new JedisPool(config, readerTmp[0],
					Integer.parseInt(readerTmp[1]), 5000);

			instance = new RedisUtil();
		}
	}

	public void incrByFloat(String key, float value) {
		Jedis jedis = null;
		try {
			jedis = writerPool.getResource();
			jedis.incrByFloat(key, value);
			writerPool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			logger.error(e.getMessage(), e);
			if (null != jedis) {
				writerPool.returnBrokenResource(jedis);
			}
			throw new JedisConnectionException(e);
		}
	}

	public String get(String key) {
		String result = null;

		Jedis jedis = null;
		try {
			jedis = writerPool.getResource();
			result = jedis.get(key);
			writerPool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			logger.error(e.getMessage(), e);
			if (null != jedis) {
				writerPool.returnBrokenResource(jedis);
			}
			throw new JedisConnectionException(e);
		}

		return result;
	}

	public void pfadd(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = writerPool.getResource();
			jedis.pfadd(key, value);
			writerPool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			logger.error(e.getMessage(), e);
			if (null != jedis) {
				writerPool.returnBrokenResource(jedis);
			}
			throw new JedisConnectionException(e);
		}
	}

	public long pfcount(String key) {
		long result = 0;

		Jedis jedis = null;
		try {
			jedis = readerPool.getResource();
			result = jedis.pfcount(key);
			readerPool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			logger.error(e.getMessage(), e);
			if (null != jedis) {
				readerPool.returnBrokenResource(jedis);
			}
			throw new JedisConnectionException(e);
		}

		return result;
	}

	public void sadd(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = writerPool.getResource();
			jedis.sadd(key, members);
			writerPool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			logger.error(e.getMessage(), e);
			if (null != jedis) {
				writerPool.returnBrokenResource(jedis);
			}
			throw new JedisConnectionException(e);
		}
	}

	public Set<String> smembers(String key) {
		Set<String> result = null;

		Jedis jedis = null;
		try {
			jedis = readerPool.getResource();
			result = jedis.smembers(key);
			readerPool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			logger.error(e.getMessage(), e);
			if (null != jedis) {
				readerPool.returnBrokenResource(jedis);
			}
			throw new JedisConnectionException(e);
		}

		return result;
	}
	
	public Set<String> scan(String pattern) {
        Set<String> result = new HashSet<String>(1024);

        Jedis jedis = null;
        try {
            jedis = readerPool.getResource();

            String cursor = ScanParams.SCAN_POINTER_START;

            ScanParams params = new ScanParams();
            params.match(pattern);
            params.count(10000);

            do {
                ScanResult<String> scanResult = jedis.scan(cursor, params);
                cursor = scanResult.getStringCursor();
                result.addAll(scanResult.getResult());
            } while (!cursor.equals(ScanParams.SCAN_POINTER_START));

            readerPool.returnResource(jedis);
        } catch (JedisConnectionException e) {
            logger.error(e.getMessage(), e);
            if (null != jedis) {
                readerPool.returnBrokenResource(jedis);
            }
            throw new JedisConnectionException(e);
        }

        return result;
    }
	
	public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = writerPool.getResource();
            jedis.del(key);
            writerPool.returnResource(jedis);
        } catch (JedisConnectionException e) {
            logger.error(e.getMessage(), e);
            if (null != jedis) {
                writerPool.returnBrokenResource(jedis);
            }
            throw new JedisConnectionException(e);
        }
    }
}
