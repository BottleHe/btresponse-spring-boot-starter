### v 1.1.3
* 更新 btresponse-core依赖升级到 v 1.1.2


### v 1.1.4
* 更新btresponse-core的依赖, 其它未做太大的变动.



### v 2.0.1 
* 更新支持JAVA21

### v 2.0.2
* ErrorController不再使用BtErrorController
* BodyAdvice 会代理ErrorController
* BtErrorController(选用时): 支持异常本身/cause为OperationException或GlobalException的处理; 不再依赖ResponseEntity构造后的map惰性变异
* BindException处理器兼容类级校验错误(ObjectError), 不再强转FieldError导致ClassCastException
* 强制模式的JSON转换器优先复用容器中的ObjectMapper(保留spring.jackson.*定制), 并保留空Bean序列化为{}的契约
* 移除失效的META-INF/spring.factories(Boot 3不再读取)与未使用的注入依赖
* 依赖 btresponse-core 2.0.2

