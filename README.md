# 日志监控

springboot版本2.5.2

```yaml
log:
  # websocket监听文件变化的请求路径
  websocket: /monitor
  # 不传logPath时，系统默认放问的日志目录
  monitor:
    defaultPath: D:/logback
```

## 接口

项目启动，访问http://localhost:10000/swagger-ui/index.html

## websocket接口

一个session对应一个监控线程，session断开线程关闭

请求格式

```
ws://127.0.0.1:10000/monitor?logPath=要实时监控的文件路径
```

如

```
ws://127.0.0.1:10000/monitor?logPath=D:/logback/test2.txt
```



