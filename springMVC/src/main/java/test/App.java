package test;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.matt.business.good.entities.Goods;


public class App {

//	private Logger logger = Logger.getLogger(App.class);
	
	private static final String log4j = "log4j.properties";
	
	private static final String[] spring_config = new String[]{"classpath:/WEB-INF/redis-spring.xml"};
	
	private static Logger logger = null;
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure(log4j);
		logger = Logger.getLogger(App.class);
		logger.info("log4j构建完成");
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(spring_config);
		
		RedisUtil redisUtil = (RedisUtil) applicationContext.getBean("redisUtil");
		String key = "zhongchang123";
		redisUtil.set(key, "zhognchang234e3");
		
		Object obj = redisUtil.get(key);
		logger.info("obj:" + obj);
		System.out.println(obj);
		
		
		Goods goods = new Goods();
		goods.setId(10);
		goods.setName("goods");
		goods.setPicture("proc");
		goods.setPrice(22.3);
		
		redisUtil.set("goods", goods);
		
		Goods g = (Goods) redisUtil.get("goods");
		System.out.println(g);
		
		
	}
}
