package redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import common.Logger;

public class RedisLock {

	private static final Logger logger = Logger.getLogger(RedisLock.class);
	
	private RedisTemplate<String, Object> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public boolean lock(String lockKey, long period) throws InterruptedException
	{
		int timeout = (int) (1000 * period);
		int expireMsecs = (int) (1000 * (period + 1));
		
		while (timeout >= 0)
		{
			long expires = System.currentTimeMillis() + expireMsecs;
			// 锁到期时间
			String expiresStr = String.valueOf(expires); 
			if (setNX(lockKey, expiresStr))
			{
				return true;
			}
			
			// redis里的时间
			String currentValueStr = get(lockKey);
			if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis())
			{
				// 判断是否为空，不为空的情况下，如果被其他的线程设置了值，则第二个条件判断是过不去的
				String oldValueStr = getSet(lockKey, expiresStr);
				// 获取上一个锁的到期时间，并设置现在的锁的到期时间  只有一个线程能获取上一个线上的到期时间，因为jedis.getSet是同步的
				if (oldValueStr != null && oldValueStr.equals(currentValueStr))
				{
					// 
					return true;
				}
			}
			timeout -= 100;
			Thread.sleep(100);
		}
		return false;
	}
	
	private boolean setNX(final String key, final String value)
	{
		Object obj = null;
		try 
		{
			obj = this.redisTemplate.execute(new RedisCallback<Object>(){

				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					StringRedisSerializer serializer = new StringRedisSerializer();
				    boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
				    connection.close();
				    return success;
				}
			});
			
		}
		catch (Exception e)
		{
			logger.error("setNX redis error, key" + key, e);
		}
		return obj == null ? false : (Boolean) obj;
	}
	
	private String get(final String key)
	{
		Object obj = null;
		obj = this.redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException 
			{
				StringRedisSerializer serializer = new StringRedisSerializer();
				byte[] data = connection.get(serializer.serialize(key));
				connection.close();
				if (data == null)
				{
					return null;
				}
				return serializer.deserialize(data);
			}
		});
		
		return obj == null ? null : obj.toString();
	}
	
	
	private String getSet(final String key, final String value)
	{
		Object obj = this.redisTemplate.execute(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException 
			{
				StringRedisSerializer serializer = new StringRedisSerializer();
				byte[] result = connection.getSet(serializer.serialize(key), serializer.serialize(value));
				connection.close();
				return serializer.deserialize(result);
			}
			
		});
		
		return obj == null ? null : obj.toString();
	}
	
}
