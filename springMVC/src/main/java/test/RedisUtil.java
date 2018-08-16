package test;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisUtil {

	private RedisTemplate<String, Object> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	
	public boolean exists(final String key)
	{
		return this.redisTemplate.hasKey(key);
	}
	
	public void remove(final String key)
	{
		if (exists(key))
		{
			this.redisTemplate.delete(key);
		}
	}
	
	public Object get(final String key)
	{
		ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
		Object result = operations.get(key);
		return result;
	}
	
	public boolean set(final String key, Object value)
	{
		try
		{
			ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
			operations.set(key, value);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false; 
	}
	
	
}
