package br.order.redis.redis;

public interface RedisService {
	// 保存数据到redis
	public String set(final String key, final String value);

	// 根据key获取数据
	public String get(final String key);

	// 根据key删除数据
	public Long delete(final String key);

	// 根据key设置存活时间
	public Long set(final String key, final Integer seconds);

	// 保存数据到redis中，同时设置存活时间
	public Long set(final String key, final String value, final Integer seconds);

	// 判断是否存在某个key
	public Boolean exists(String key);

	// 删除所有数据
	public String destroy();
}
