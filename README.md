# 使用介绍

> btresponse-spring-boot-starter 会自动的地将控制器返回的数据格式化成固定格式的JSON字符串返回给调用端. 

提供一个默认的返回数据格式, 如下:

```json
{
    "success": true,				// Boolean 表示请求的成功与否
    "code": 0,							// int 表示请求返回的编码, 默认情况下, 请求成功这个值为0
    "data": true,						// Object/Mixed 实际返回的数据内容
    "message": ""						// String 外带的描述信息, 一般在出错时返回
}
```

它使用两个异常来处理出错返回, 插件预定义了两个异常. 此时就可以统计使用异常的方式做统一的错误返回. 

* work.bottle.plugin.exception.OperationException : 操作异常, 一般做为一些可控错误返回. Springboot拦截到这个异常及其派生异常. 都会处理成一个标准的错误返回格式, 通过这种方式给调用端返回时, http status都为 200.

```java
package work.bottle.plugin.exception;

public class OperationException extends RuntimeException {

    private int code;
    private Object data;

    private OperationException()
    {
        super();
    }

    public OperationException(int code, String message)
    {
        super(message);
        this.code = code;
        this.data = null;
    }

    public OperationException(int code, String message, Object data)
    {
        super(message);
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
```

* work.bottle.plugin.exception.ServerException : 服务异常, Springboot拦截到这个异常及其派生异常. 都会处理成一个标准的错误返回格式. 并将它的code字段处理成 http status, 比如 500, 403等.

```java
package work.bottle.plugin.exception;

public class ServerException extends RuntimeException {

    private int code;
    private Object data;

    private ServerException()
    {
        super();
    }

    public ServerException(int code, String message)
    {
        super(message);
        this.code = code;
        this.data = null;
    }

    public ServerException(int code, String message, Object data)
    {
        super(message);
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
```



## 安装



### 一. Maven引入

```xml
<!-- 项目依赖于 validation -->
<dependency>
  	<groupId>org.springframework.boot</groupId>
  	<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<dependency>
    <groupId>work.bottle.plugin</groupId>
    <artifactId>btresponse-spring-boot-starter</artifactId>
    <version>1.0.10</version>
</dependency>
```



### 二. 编辑配置

> 这里以yml格式配置为例子

```yml
bt-response:
  enable: true		# 是否开启, 默认为true, 针对byte[], string, null等控制器方法返回. 保留原有行为. 其它类型返回, 将自动格式化为我们默认定义的数据格式
  force: false		# 是否强制JSON返回, 默认为false. 如果开启, 会覆盖默认行为(byte[], string, null等都会以JSON包装形式返回).
```



## 基本使用





这个数据格式可以通过提供一个实现 work.bottle.plugin.StandardResponseFactory<T> 接口的Bean来替换, 实际的包装类通过泛型提供 . 例子如下:

```java
package work.bottle.demo.model;
// 自定义返回结构定义
public class CustomResponse {
    private int code;
    private String msg;
    private Object data;

    public CustomResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
```



```java
package work.bottle.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import work.bottle.demo.model.CustomResponse;
import work.bottle.plugin.StandardResponseFactory;

// 例子中自定义的返回类型为CustomResponse
@Configuration
public class CustomerResponseFactory implements StandardResponseFactory<CustomResponse> {
		// 提供一个函数判断返回类型是否已经是包装类型了
  	@Override
    public boolean isInstance(Object o) {
        return o instanceof CustomResponse;
    }

  	// 生产一个返回结构类的实例
    @Override
    public CustomResponse produceResponse(int code, String message, Object data) {
        return new CustomResponse(code, message, data);
    }

  	// 生产一个返回结构类的实例
    @Override
    public CustomResponse produceResponse(boolean success, int code, String message, Object data) {
        return new CustomResponse(code, message, data);
    }

  	// 生产一个返回结构类的ResponseEntity包装实例
    @Override
    public ResponseEntity<CustomResponse> produceResponseEntity(boolean success, int code, String message, Object data, HttpStatus httpStatus, MultiValueMap<String, String> headers) {
        return new ResponseEntity<CustomResponse>(new CustomResponse(code, message, data), headers, httpStatus);
    }
}

```





#### 