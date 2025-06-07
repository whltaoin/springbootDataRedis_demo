package cn.varin.springbootdataredis_demo;

import cn.varin.springbootdataredis_demo.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class SpringbootDataRedisDemoApplicationTests {

	@Autowired
	RedisTemplate redisTemplate;
	@Test
	void setTest() {

		ValueOperations valueOperations = redisTemplate.opsForValue();
		valueOperations.set("user:1",new User("varin",1).toString());
		Object o = valueOperations.get("user:1");
		System.out.println(o);


	}

}
