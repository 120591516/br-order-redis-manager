package br.order.redis.impl.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.order.redis.redis.RedisService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @ClassName: RedisCrmServiceImpl
 * @Description: RedisCrmService接口实现
 * @author zxy
 * @date 2016年9月13日 下午4:19:55
 *
 */
@Service
public class RedisServiceImpl implements RedisService {

	@Autowired
	private JedisPool jedisPool;

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Long delete(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();
		return result;
	}

	@Override
	public Long set(String key, Integer seconds) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key, seconds);
		jedis.close();
		return result;
	}

	@Override
	public Long set(String key, String value, Integer seconds) {
		Jedis jedis = jedisPool.getResource();
		jedis.set(key, value);
		Long result = jedis.expire(key, seconds);
		jedis.close();
		return result;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.exists(key);
		jedis.close();
		return result;
	}

	@Override
	public String destroy() {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.flushDB();
		jedis.close();
		return result;
	}

}
