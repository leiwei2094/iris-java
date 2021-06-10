# 广告：招人了！
>        
> 插播一条招聘信息        
> [招人了！！！！！]:tada::tada::tada::tada:
>             
> 我目前任职于杭州信公小安架构师。技术团队正在招人，有兴趣的朋友可以联系我。微信号：leiwei2094    
> 公司亮点： 
> 1. 发展快，预计今年上市。金融咨询领域领跑者。 
> 2. 双休，早9晚6，不内卷。
> 3. 技术氛围好，团队核心成员来自美国道富、阿里巴巴、网易、恒生。
> 3. 团队的极客时间课程《如何提高工程质量》:https://time.geekbang.org/dailylesson/topic/148
>         
> [招人了！！！！！]        
>         

# iris

一个基于Java的RPC框架。目前可以看成是mini版的Dubbo。有注册中心，提供服务的注册，发现和负载均衡。

* 网络通信: Netty4
* 注册中心: etcd，可扩展
* 动态代理: byte-buddy
* 序列化: Protobuff(Protostuff)
* 可以脱离Spring，提供API调用。
* 集成Spring，提供XML，Java配置
* 提供Spring Boot Starter
* 提供SPI机制，实现微内核加插件的架构。实现可扩展，开发者可以为iris开发组件，以插件的形式集成到iris中。插件的加载使用另一个微容器框架见[coco](https://github.com/vangoleo/coco)项目。该项目fork于阿里的[cooma](https://github.com/alibaba/cooma)。

# Todo:
* 支持优雅停机，不依赖Spring和Web容器。
* 支持服务延迟暴露
* 支持超时和重试配置
* 添加监控系统，使用Prometheus
* 更好的支持容器环境

# How to use
iris支持以下使用方式:    
1. 原生API形式，不依赖Spring,非Spring项目也可以使用
2. Spring配置方式，和Spring很好的集成
3. Spring Boot配置方式，提供了一个spring boot starter，以自动配置，快速启动

# API使用
Iris核心代码不依赖Spring，可脱离Spring使用。        
1. 启动etcd注册中心
2. 编写一个接口IHelloService
```java
public interface IHelloService {
    String hello(String name);
}
```
3. 编写一个IHelloService的实现
```java
public class HelloService implements IHelloService {
    @Override
    public String hello(String name){
        return "Hello, " + name;
    }
}
```
4. 启动Server
```java
IRegistry registry = new EtcdRegistry("http://127.0.0.1:2379");
RpcServer server = new RpcServer(registry)
        .port(2017)
        .exposeService(IHelloService.class,new HelloService());
server.run();
```
5. 启动client
```java
RpcClient client = new RpcClient(registry);
IHelloService helloService = client.create(IHelloService.class);
String s = helloService.hello("leo");
System.out.println(s);   // hello, leo
```
6. 停止server
```text
因为服务没有provider，client报错找不到provider        
```
7. 启动server        
```text
server启动后，会去etcd注册中心注册服务，client端马上正常工作。        
```

# Spring配置
服务提供者，使用自定义注解@Service来暴露服务，通过interfaceClass来指定服务的接口。        
该@Service注解是iris提供的，并非Spring的注解
```java
@Service(interfaceClass = IHelloService.class)
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        return "hello" + name;
    }
}
```
服务使用者，通过@Reference来引用远程服务，就像使用本地的SpringBean一样。背后的SpringBean封装和Rpc调用对开发者透明。使用体验和Dubbo是一样的。
```java
public class Baz {

    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public void hello(String name) throws Exception {
        System.out.println(helloService.hello(name));
    }
}

```
配置服务提供者，本例子使用XML配置，使用Java Code配置也可以。
```xml
<bean id="registry" class="com.leibangzhu.iris.registry.EtcdRegistry">
        <constructor-arg name="registryAddress" value="http://127.0.0.1:2379"></constructor-arg>
    </bean>

    <bean id="server" class="com.leibangzhu.iris.server.RpcServer">
        <constructor-arg name="registry" ref="registry"></constructor-arg>
    </bean>

    <bean id="serviceAnnotationBeanPostProcessor" class="com.leibangzhu.iris.spring.ServiceAnnotationBeanPostProcessor"></bean>

    <bean id="helloService" class="com.leibangzhu.iris.spring.HelloService"></bean>
```
配置服务消费者，本例子使用XML配置，使用Java Code配置也可以。
```xml
<bean id="registry" class="com.leibangzhu.iris.registry.EtcdRegistry">
        <constructor-arg name="registryAddress" value="http://127.0.0.1:2379"></constructor-arg>
    </bean>

    <bean id="client" class="com.leibangzhu.iris.client.RpcClient">
        <constructor-arg name="registry" ref="registry"></constructor-arg>
    </bean>

    <bean id="referenceAnnotationBeanPostProcessor" class="com.leibangzhu.iris.spring.ReferenceAnnotationBeanPostProcessor"></bean>

    <bean id="foo" class="com.leibangzhu.iris.spring.Baz"></bean>
```

# Spring Boot配置
使用原生的Spring配置还是有些繁琐，可以使用Spring Boot来获得更好的开发体验。        
服务提供者
```java
@Service(interfaceClass = IHelloService.class)
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        return "Hello, " + name + ", from com.leibangzhu.iris.springboot.HelloService";
    }
}
```
服务消费者
```java
@Component
public class Foo {

    @Reference(interfaceClass = IHelloService.class)
    private IHelloService helloService;

    public String hello(String name) throws Exception {
        return helloService.hello(name);
    }
}
```
在application.properties文件中配置服务提供者
```properties
iris.registry.address=http://127.0.0.1:2379

iris.server.enable=true
iris.server.port=2017
iris.annotation.package=com.leibangzhu.iris.springboot
```
在application.properties文件中配置服务消费者
```properties
iris.registry.address=http://127.0.0.1:2379

iris.client.enable=true
```
使用SpringBoot时，不许再手动配置相关的spring bean，Iris提供的spring boot starter会自动配置好这些spring bean。

# Iris的微内核加插件机制
* Iris使用了SPI机制，实现了微内核加插件的架构。
* Iris里的许多功能都是以插件的形式加载的。一个功能点就是一个扩展点，就是一个接口。通过注入的方式，将某个实现注入到Iris中。        
* 扩展点的加载没有使用第三方的容器，比如Spring，而是自己实现了一个轻量级的容器。对Java的SPI机制进行了一下扩展和加强。        
* 每个扩展点会有多个实现，开发者可以自己配置使用哪个，比如使用zookeeper注册中心或etcd注册中心。
* Iris内置的扩展实现满足大部分要求，开发者也可以自己编写扩展点的实现，加载到Iris中。
* Iris会扫描classpath下面的`/META/extensions`文件夹中的文件，读取扩展点信息，然后运行时动态加载。
* 使用时通过ExtensionLoader加载对应的插件
* ExtensionLoader支持类似于Spring的IoC和AoP功能。即，扩展点中可以自动装配其他的扩展点。Iris提供Wrapper使用Decrator模式来实现AOP。
* 所有的扩展点元数据文件都在`/META-INF/extensions`文件夹下，文件名是实现的扩展点的接口的全类名。
* 文件的内容是key=value的形式。key是扩展点实现的name，value是实现类的全类名。实现类是一个简单的Java类，不依赖任何第三方接口和注解。
* 使用Wrapper实现AOP时，在扩展点name前加上`+`。比如:`+logWrapper=com.leibangzhu.iris.core.SomeExtension`

### 一个负载均衡器的Demo
Iris中的服务提供者有多个，client端调用的时候，有一个负载均衡的策略。
Iris目前提供了1个实现：随机选择一个提供者。以后会提供更多的负载均衡策略。如果有多个，只需要在iris.properties或application.properties中添加配置：
```properties
iris.loadbalance=random
```
Iris提供的的`ILoadBalance`接口。
```java
@Extension(defaultValue = "random")
public interface ILoadBalance {
    int select(@Adaptive("loadbalance")Map<String,String> config,int amount) throws Exception;
}
```
* @Extension注解声明这是一个扩展点
* defaultValue表示默认使用random这个扩展点实现
* @Adaptive注解声明这是一个自适应扩展方法，会根据运行时信息动态选择对应的扩展点实现。类似于一个动态代理。
自带的随机的负载均衡是在下面的文件中定义的：
`/src/main/resources/META-INF/extensions/com.leibangzhu.iris.core.loadbalance.ILoadBalance`文件的内容:
```text
random=com.leibangzhu.iris.core.loadbalance.RandomLoadBalance
```
通过ExtensionLoader获取扩展点：
```java
ILoadBalance loadBalance = ExtensionLoader.getExtensionLoader(ILoadBalance.class).getAdaptiveInstance();
// get runtime configuration and load config to map
// get actual loadbalance from iris.properties or application.properties file
String loadbalance = IrisConfig.getLoadbalance();
map.put("loadbalance",loadbalance)
loadBalance.select(map,size);
```

### 扩展自己的负载均衡器
开发者想使用轮询的负载均衡策略，可以按照以下步骤来扩展：
1. 编写一个类，实现ILoadBalance接口
```java
public class RoundRobinLoadBalance implements ILoadBalance {
    @Override
    public int select(Map<String, String> config, int amount) throws Exception {
        // put your code here ...
    }
}
```
2. 在classpath中添加一个文件：
`/META-INF/extensions/com.leibangzhu.iris.core.loadbalande.ILoadBalance`
```properties
roundrobin=com.mycompany.foo.bar.RoundRobinLoadBalance
```
3. 在`iris.properties`或`application.properties`中添加配置：
```properties
iris.loadbalance=roundrobin
```
这样，Iris就会使用我们自定义的轮询负载均衡了。

# Why iris
`iris`取名于梵高的画**鸢尾花**
