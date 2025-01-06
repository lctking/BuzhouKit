# BuzhouKit
> ##### *西北海之外，大荒之隅，有山而不合，名曰不周负子，有两黄兽守之。有水曰寒署之水。水西有湿山，水东有幕山。*  
> ##### <div align="right"><em>—— 《山海经》海经·大荒西经</em></div>
##### A Lightweight Idempotent Component
##### 轻量级幂等组件库
## 项目概述
**BuzhouKit** 是一款专为确保幂等性设计的组件库。简单添加一个注解@Idempotent即可解决幂等问题！此库适用于需要保证操作重复性和一致性的应用场景，为开发者提供一个可靠、稳定的基础设施。
### 配置/如何使用
### 1，代码配置  
##### 方法一（将代码配置到本地maven仓库）  
1，将代码克隆/下载到本地  
2，在BuZhouKit\BuZhouKit-Idempotent>目录下运行`mvn clean install`  使得代码保存在本地maven库中，再通过pom坐标即可引用;  
![image](https://github.com/user-attachments/assets/08d3b4a4-8ea4-48e3-a88e-9e690288c866)  
![f4a1dfb9c28b938bf79b5a501c003e86](https://github.com/user-attachments/assets/35c1555e-9174-498e-afac-204baab6b1e8)  

##### 方法二（推荐，将代码作为你的项目的一个模块来使用）  
1，将代码克隆/下载到本地  
2，将代码copy到项目某模块中  
### 2，组件使用
简单概括：哪个方法需要满足幂等性，就将@Idempotent注解添加到该方法上，再根据业务特性来配置注解所需的参数。  
举个栗子：  
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
##### 加锁键 (即为下文中的key) 在真实redis中的形态如图：
![image](https://github.com/user-attachments/assets/b7e75752-e35f-4270-a53d-9c750790bbc7)  

#### 2.1 核心参数的含义与使用：  
##### 这里有必要先简单概述下该幂等注解的底层逻辑: 本质是通过缓存/数据库对键 (下文简称为key, 逻辑上: key = uniquePrefix + spEl.value) 加锁来实现幂等;  
- uniquePrefix: 锁前缀，用于表明该位置@Idempotent注解所修饰的方法有着什么样的业务特性。例如上例方法体逻辑为user信息的插入，那么uniquePrefix 设为 "user:insert" 是合适的;  
- spEL: spel语句，实现动态定义加锁键。例如上例中"#userDO.hashCode()"即为key逻辑式中的spEl.value。[spEL语法学习-官方参考](https://docs.springframework.org.cn/spring-framework/reference/core/expressions.html)
- expireTime: 顾名思义，key的过期时间。建议根据业务特性来设置expireTime， 例如上例为添加用户场景，将键设置为永不过期来去重是合适的。如果是消息队列消费业务场景，设为10-30分钟是合适的。 
- 
#### 2.2 底层原理深度剖析

### 参数类型选择
1,缓存类型选择：1，基于caffeine组件的本地缓存；2，基于redis的分布式缓存

