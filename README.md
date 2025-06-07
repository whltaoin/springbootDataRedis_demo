### SpringDataRedis使用
##### springData介绍
1. 项目地址：[https://spring.io/projects/spring-data-redis#learn](https://spring.io/projects/spring-data-redis#learn)

![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749263017379-62abd37c-faea-422e-b032-ea5009f56659.png)

2. Redis模版版本信息

![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749263062728-9f395a7f-5014-4938-907a-a3b6d4643480.png)

##### SpringDataRedis快速入门
![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749263201723-ec7136aa-47a3-456c-869f-90ead34613eb.png)

##### SpringbootDataRedis使用步骤
![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749263869454-da5ced22-0cbf-4605-9f50-c16d603cec3c.png)

##### 基本示例
1. 导入依赖

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.4.5</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>cn.varin</groupId>
	<artifactId>springbootDataRedis_demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>springbootDataRedis_demo</name>
	<description>springbootDataRedis_demo</description>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
<!--		springbootDataRedis-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>
<!--		pool2-->
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-pool2</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>

```

2. 配置yml

```java
spring:
  data:
    redis:
      port: 6379
      password: password
      database: 0
      lettuce:
        pool:
          max-active: 10
          max-idle: 10
          min-idle: 0
          max-wait: 100ms
      host: address

```

3. 编写测试类

```java
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

```



4. 结果：

![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749280548746-3495a58b-c8e0-4427-8c14-33b80480d161.png)

##### 重构redisTemplate序列化和反序列化工具
1. 问题

![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749281128684-49707c28-1f17-41b7-aae3-2aaa36db5cef.png)

    1. 在我们直接使用redisTemplate时，存入到redis的内容，是经过编译的字节，
    2. 影响阅读性
    3. 增加了存储空间
2. 解决方案：
    1. 自定义序列化和反序列话的编码格式
3. 步骤
    1. 建立template
    2. 设置连接工厂
    3. 设置序列化工具
    4. 分别对key和value设置不同的格式

```java
package cn.varin.springbootdataredis_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisTamplateConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(factory);
        // 创建序列化工具

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        // 对key
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());

        // 对value
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        return  template;

    }
}

```

测试类

```java
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
        // value为user对象
		valueOperations.set("user:2",new User("varin",1));
		Object o = valueOperations.get("user:1");
		System.out.println(o);


	}

}

```

4. 测试结果

![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749281842641-ce1f2f4c-1a7a-41c5-a6b3-775fd6a10cf5.png)

##### StringRedisTamplate类使用
1. 问题：
    1. 虽然自定义序列化工具可以解决上一问题，但是修改后在JSON字符串中会多存储一个类的包名
        1. 导致增大存储的空间
2. 解决方法，
    1. 使用StringRedisTamplate类，在加上自己使用第三方的序列化工具进行存储。
        1. 优点：在存储时不会增加额外的数据
        2. 缺点：增加少许的代码量
3. 示例代码

```java
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
```

![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749284552514-ff3657e1-4461-4e33-8f0b-378dbcad918b.png)

![](https://cdn.nlark.com/yuque/0/2025/png/38516294/1749284520514-dff631b0-8488-4dcd-889e-c55819e6a592.png)

