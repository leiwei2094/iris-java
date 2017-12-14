# iris

一个基于Java的RPC框架。目前可以看成是mini版的Dubbo。有注册中心，提供服务的注册，发现和负载均衡。

* 网络通信: Netty4
* 注册中心: etcd
* 动态代理: byte-buddy
* 序列化: Protobuff(Protostuff)
* 可以脱离Spring，提供API调用。
* 集成Spring，提供XML，Java配置
* 提供Spring Boot Starter

# Todo:
* 支持优雅停机
* 支持服务延迟暴露
* 提供SPI机制，实现可扩展，其他第三方开发者可以为iris开发组件，以插件的形式缝集成到iris中。见[coco](https://github.com/Leo-Lei/coco)项目
* 添加监控系统，使用Prometheus
* 更好的支持容器环境


# How to use
iris支持以下使用方式:    
1. 原生API形式，不依赖Spring,非Spring项目也可以使用
2. Spring配置方式，和Spring很好的集成
3. Spring Boot配置方式，提供了一个spring boot starter，以自动配置，快速启动


# API使用
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

服务提供者，使用自定义注解@Service来暴露服务，通过interfaceClass来指定服务的接口
```java
@Service(interfaceClass = IHelloService.class)
public class HelloService implements IHelloService {
    @Override
    public String hello(String name) throws Exception {
        return "hello" + name;
    }
}
```

服务使用者，通过@Reference来引用远程服务，就像使用本地的SpringBean一样。背后的SpringBean封装和Rpc调用对开发者透明。
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

    <!--<bean id="helloService" class="com.leibangzhu.iris.spring.HelloService"></bean>-->

    <bean id="foo" class="com.leibangzhu.iris.spring.Baz"></bean>
```

# Spring Boot配置
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

在application.properties文件中配置服务消费者
```properties
iris.registry.address=http://127.0.0.1:2379

iris.client.enable=true
```

在application.properties文件中配置服务提供者
```properties
iris.registry.address=http://127.0.0.1:2379

iris.server.enable=true
iris.server.port=2017
iris.annotation.package=com.leibangzhu.iris.springboot
```

# Why iris
`iris`取名于梵高的画**鸢尾花**
