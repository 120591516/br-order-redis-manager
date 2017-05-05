package br.order.redis.impl.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.order.redis.redis.InitRedisService;
import br.order.redis.redis.RedisService;

@Service
public class InitRedisServiceImpl implements InitRedisService {

	@Autowired
	@Qualifier("RedisInnerService")
	private RedisService redisService;

	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}

	@Override
	public void initRedis() {
		System.out.println("初始化Redis...");
	}

	@Override
	public void destroyRedis() {
		System.out.println("清空Redis...");
		redisService.destroy();
	}

}
