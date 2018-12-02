## redis-spring-boot-starter集成使用
#### 版本
- JDK: 1.8
- Spring Boot: 2.1.0-RELEASE
- spring-data-redis: 2.1

#### 使用
1.引入依赖
```xml
<dependency>
    <groupId>com.yushi</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
2.application.yml
```yaml
spring:
  redis:
    host: shenjy.club
    port: 6379
    timeout: 10s
    lettuce:
      pool:
        max-active: 8
        max-wait: -1s
        max-idle: 8
        min-idle: 0
  main:
      allow-bean-definition-overriding: true
```
3.简单例子
```java
@Autowired
private RedisService<User> redisService;

public void get() {
    User user = redisService.get("user:1");
    System.out.println(user);
}
```