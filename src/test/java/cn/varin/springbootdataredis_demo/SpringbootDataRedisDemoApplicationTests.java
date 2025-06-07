package cn.varin.springbootdataredis_demo;

import cn.varin.springbootdataredis_demo.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Stack;

@SpringBootTest
class SpringbootDataRedisDemoApplicationTests {

	@Autowired
	RedisTemplate redisTemplate;
	@Test
	void setTest1() {

		ValueOperations valueOperations = redisTemplate.opsForValue();
		valueOperations.set("user:2",new User("varin",1).toString());
		Object o = valueOperations.get("user:1");
		System.out.println(o);


	}
	@Test
	void setTest2() {

		ValueOperations valueOperations = redisTemplate.opsForValue();
		valueOperations.set("user:2",new User("varin",1));
		Object o = valueOperations.get("user:1");
		System.out.println(o);


	}

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	// 用于转Json格式
	private static  final ObjectMapper mapper = new ObjectMapper();
	@Test
	void StringRedisTamplateTest() throws JsonProcessingException {
		User user = new User("varya",1);
		// 转JSON格式
		String s = mapper.writeValueAsString(user);
		// 写入数据
		stringRedisTemplate.opsForValue().set("user:3",s);

		// 读取数据
		String s1 = stringRedisTemplate.opsForValue().get("user:3");
		// 反序列化
		User user1 = mapper.readValue(s1, User.class);
		System.out.println(user1);


	}


}
