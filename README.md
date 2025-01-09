# BuzhouKit
#### A Lightweight Idempotent Component
> ##### *西北海之外，大荒之隅，有山而不合，名曰不周负子，有两黄兽守之。有水曰寒署之水。水西有湿山，水东有幕山。*  
> ##### <div align="right"><em>—— 《山海经》海经·大荒西经</em></div>
#### BuzhouKit，一个轻量级幂等组件库
## 项目概述
**BuzhouKit** 是一款专为确保幂等性设计的组件库。简单添加一个注解@Idempotent即可解决幂等问题！此库适用于需要保证操作重复性和一致性的应用场景，为开发者提供一个可靠、稳定的基础设施。  
### 配置/如何使用
### 1，代码配置  
##### 方法一（将代码配置到本地maven仓库）  
1，将代码克隆/下载到本地  
2，进入到BuZhouKit\BuZhouKit-Idempotent>目录下运行`mvn clean install`  使得代码保存在本地maven库中，再通过pom坐标即可引用；  
![image](https://github.com/user-attachments/assets/08d3b4a4-8ea4-48e3-a88e-9e690288c866)  
![f4a1dfb9c28b938bf79b5a501c003e86](https://github.com/user-attachments/assets/35c1555e-9174-498e-afac-204baab6b1e8)  
##### pom坐标形如:
```
  <dependency>
      <groupId>com.lctking</groupId>
      <artifactId>BuZhouKit-Idempotent</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  </dependency>
```

##### 方法二（推荐，将代码作为你的项目的一个模块来使用）  
1，将代码克隆/下载到本地  
2，将代码copy到项目某模块中  
### 2，组件使用
##### 简单概括：哪个方法需要满足幂等性，就将@Idempotent注解添加到该方法上，再根据业务特性来配置注解所需的参数。  
##### 举个栗子(插入用户信息场景)：  
```
  @PostMapping("/redisCache-test")
  @Idempotent(
          uniquePrefix = "user:insert",
          spEL = "#userDO.hashCode()",
          expireTime = -1L,
          cacheType = CacheTypeEnum.REDIS,
          message = "添加用户请求重复,[BY_REDIS]",
          exceptionClass = CustomizeException.class
  )
  public void testIdempotentRelyOnDistributedCache(@RequestBody UserDO userDO){
      userService.insertUser(userDO);
  }
```
##### 加锁键 (即为下文中的key) 在真实redis中的形态如下图所示：
![image](https://github.com/user-attachments/assets/b7e75752-e35f-4270-a53d-9c750790bbc7)  

#### 2.1 核心参数的含义与使用建议：  
##### 这里有必要先简单概述下该幂等注解的底层逻辑: 本质是通过缓存/数据库对键 (下文简称为key，在逻辑上key的构成: key = uniquePrefix + spEl.value) 加锁来实现幂等；  
- uniquePrefix：锁前缀，用于表明该位置@Idempotent注解所修饰的方法有着什么样的业务特性。例如上例方法体逻辑为user信息的插入，那么uniquePrefix 设为 "user:insert" 是合适的；  

- spEL：spel语句，实现动态定义加锁键。例如上例中"#userDO.hashCode()"即为key逻辑式中的spEl.value。[spEL语法学习-官方参考](https://docs.springframework.org.cn/spring-framework/reference/core/expressions.html)  

- expireTime：顾名思义，是key的过期时间。建议根据业务特性来设置expireTime， 例如上例为添加用户场景，将键设置为永不过期来去重是合适的。如果是消息队列消费业务场景，设为10-30分钟是合适的；  

- cacheType：缓存类型选择，按照缓存位置划分目前可分为两类，分别是：1，基于caffeine组件的本地缓存(对应枚举为 CacheTypeEnum.LOCAL 或 CacheTypeEnum.CAFFEINE)；2，基于redis的分布式缓存(CacheTypeEnum.REDIS)；  

- message：幂等奏效抛出异常时的提示信息，建议根据业务场景来填写。  

- exceptionClass：可由注解使用者自由设定的报错时抛出的异常类型，例如上例中的CustomizeException.class即为在测试项目中所配置的自定义异常类（该异常类不在幂等组件库中，可自由设定）。  

### 3，功能测试
#### 功能测试详见Test模块（与BuZhouKit-Idempotent模块并列）
在application.yml中配置好你的本地redis服务后启动Test服务，进行功能测试：   
路径：` http://127.0.0.1:8089/Idempotent-test/redisCache-test `  
请求体[json] :  
```
  {
    "id": 1,
    "username": "testUser0",
    "password": "testPassworwd123",
    "phone": "123-456-7890"
  }
```
#### 3.1 简单测试
第一次请求，控制台打印：  
![image](https://github.com/user-attachments/assets/3539277f-6e74-4e0b-b91b-620752238939)
第二次请求，控制台打印：
![image](https://github.com/user-attachments/assets/fe5d21b4-c667-4b0b-9926-f6064abe2edc)
如图所示，不仅抛出了注解使用者自定义的异常类型，还附加了自定义的提示信息。
#### 3.2 jmeter压测
##### 条件：25个线程循环一百次；CSV数据文件设置包含一个含有20组json请求的txt文本。预计请求成功率0.8% (20/2500)
##### 如图，符合预期，组件幂等性验证成功！
![image](https://github.com/user-attachments/assets/9003c912-8823-47d7-8012-7b43b24b2fbf)





### 4，tips
- 如果你发现幂等注解在某些位置不奏效，请考虑AOP代理失效问题，如：同类内部的直接方法调用，这种调用不经过AOP代理对象，造成幂等注解失效。推荐的解决方法：将被调用方法抽取到合适的服务类接口及其实现类中。
- ...

### 5，TODO
- 引入更详细的日志记录功能；  
- 增加mysql相关功能支持；
- ...
