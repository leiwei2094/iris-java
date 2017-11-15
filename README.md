# iris

一个基于Java的RPC框架。目前可以看成是mini版的Dubbo。有注册中心，提供服务的注册，发现和负载均衡。

* 网络通信: Netty4
* 注册中心: etcd
* 动态代理: byte-buddy
* 序列化: Protobuff(Protostuff)
* 可以脱离Spring，提供API调用。


# Todo:
* 添加优雅停机
* 添加Spring支持
* 添加Spring Boot的Starter
* 添加Prometheus监控
* 更好的支持容器环境


# How to use
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

# Why iris
`iris`取名于梵高的画**鸢尾花**
