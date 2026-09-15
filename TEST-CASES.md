# 测试用例补充清单

> 基于当前 starter 全部公开行为梳理的测试缺口。现有用例的覆盖情况见文末「现有覆盖基线」。
> 优先级：**P0** = 保护已修复 bug / 核心开关，应尽快补；P1 = 重要行为契约；P2 = 边界与健壮性。
> 实现方式：`MockMvc` = 现有 MockMvc 风格；`TRT` = TestRestTemplate 走真实 HTTP（error 分发链路必须用这种）；`Unit` = 纯单元测试不起容器；`新端点` = 需要在 demo controller 补充接口。

## 实施进度（2026-09-15，总计 123 用例全绿）

**已实现**（对应新增测试类/文件）：

| 覆盖范围 | 测试类 |
| --- | --- |
| TC-001~003（+461/561） | `GlobalExceptionControllerTest` |
| TC-004/005/008/009（+@Validated参数415） | `ValidationControllerTest` |
| TC-006/007 | `LooseModeControllerTest` |
| TC-020~023（+类级@Ignore、未知配置项） | `EnableDisabledControllerTest` |
| TC-024 | `EnableDisabledForceControllerTest` |
| TC-030~035 | `ResponseShapeControllerTest` / `ResponseShapeCustomControllerTest` |
| TC-040/041/043/044 | `ErrorDispatchControllerTest` |
| TC-010（记录当前行为） | `FilterOperationExceptionTest` |
| TC-011 | starter `BtErrorControllerTest` |
| TC-013 | `CustomErrorControllerTest` |
| TC-050/052 | `JacksonCustomForceControllerTest` |
| TC-060~062 | core `GlobalErrorTest` |
| TC-063/064 | core `ExceptionsTest` |
| TC-065 | starter `BtResponseFactoryTest` |

**暂缓**：TC-012（浏览器 text/html）、TC-037（并发）、TC-042（已隐式覆盖）、TC-051/054/055（force 边界）、TC-070~072（第三方集成）。

**实施中的新发现**：
1. demo bug 已修复：`IndexController.sendSmsVerificationData` 第二次 `setExpireTimestamp(+60)` 应为 `setNextSentTimestamp`，否则 `/code/send` 序列化必然 NPE。
2. 新基线行为：void 返回会包装为默认响应（`produceDefaultResponse` 的实际触达路径）；force+@Ignore 的 String 输出为 JSON 字符串（带引号）。

---

## 一、异常处理链（BaseResponseBodyExceptionHandler / BtErrorController）

| ID | 优先级 | 场景 | 实现方式 | 预期结果 |
| --- | --- | --- | --- | --- |
| TC-001 | **P0** | GlobalException(404)：`GET /index/v1/e/s/404`（端点已存在，未测试） | MockMvc | HTTP 404；body `{"success":false,"code":404,"data":null,"message":"访问内容不存在"}` |
| TC-002 | **P0** | GlobalException(506 非标code)：`GET /index/v1/e/s/506` | MockMvc | HTTP 506；code=506，message="服务器配置错误"（验证非标准 status 透传） |
| TC-003 | **P0** | GlobalException 携带自定义 message+data | 新端点 `/index/v1/e/s/409-custom` 抛 `new ConflictException("自定义消息", data)` | message/data 原样出现在响应；http status=409 |
| TC-004 | **P0** | BindException 字段级校验：`GET /index/v1/valid/query?page=-1`（新端点，`@Validated` + `@Min(1)`） | MockMvc | HTTP 422；code=422；message 为字段校验消息（回归保护 FieldError 路径） |
| TC-005 | **P0** | BindException **类级校验**（ObjectError 而非 FieldError）：新端点参数对象加 `@ScriptAssert` 类约束 | MockMvc | HTTP 422 不抛 ClassCastException、返回 500（回归保护本次修复的强转 bug） |
| TC-006 | **P0** | looseMode=true + GlobalException：`properties={"bt-response.loose-mode=true"}` + `GET /e/s/404` | MockMvc | HTTP **200**；code 仍为 404（宽松模式契约首次测试） |
| TC-007 | P1 | looseMode=true + BindException | 同 TC-004 配置 | HTTP 200 + code=422 |
| TC-008 | P1 | ValidationException：端点内 `throw new ValidationException("...")` | MockMvc / 新端点 | HTTP 415；code=415；message 透传 |
| TC-009 | P1 | POST `@RequestBody` JSON 反序列化 + 包装：`POST /index/v1/login` | MockMvc | 200；data 为 EmployeeAuth 序列化（确认 canRead=false 未破坏请求解析） |
| TC-010 | P1 | Filter 中抛 `OperationException` | 新增 Filter + TRT | **预期需先定义**：当前走 BasicErrorController+Advice 包装，业务 code(如10100) 会丢失变成 500。测试暴露该缺口后决定：接受并文档化，或增强 BtErrorController |
| TC-011 | P1 | BtErrorController 单元测试（不启容器，直接构造） | Unit | ① ERROR_EXCEPTION 本身是 OperationException/GlobalException 时按业务异常返回（本次修复点）；② cause 是业务异常时同上；③ 兜底分支 data 不含 error/status 键、message 取 error 属性 |
| TC-012 | P2 | 浏览器 `Accept: text/html` 请求出错页 | TRT(带 header) | 非 force：返回 HTML 错误视图；force=true：返回 JSON（errorHtml force 分支） |
| TC-013 | P2 | 用户自定义 `ErrorController` Bean 存在时 starter 让位 | 新 demo 配置类 + MockMvc | 用户控制器处理 /error，无重复映射冲突（验证 @ConditionalOnMissingBean） |

## 二、开关与配置矩阵（BtResponseProperties / BtResponseConfig）

| ID | 优先级 | 场景 | 实现方式 | 预期结果 |
| --- | --- | --- | --- | --- |
| TC-020 | **P0** | `bt-response.enable=false`：普通对象返回 | MockMvc + `properties={"bt-response.enable=false"}` | `/ret/true` 返回裸 `true`（非 JSON 包装）——核心开关**从未被测试** |
| TC-021 | **P0** | `enable=false` + 异常：`GET /err/opt/exception` | 同上 | OperationException 不被处理 → error dispatch → Boot 默认错误 JSON（无 success/code 结构） |
| TC-022 | **P0** | `enable=false` + `GET /e/s/404` | 同上 | GlobalException 不被处理 → 500 或容器错误页（记录实际行为为基线） |
| TC-023 | P1 | `enable=false` + `@Ignore` 方法 | 同上 + `/method/ignore/v1/str` | 无影响，原样返回 |
| TC-024 | P2 | `enable=false` + `force=true` 组合 | 同上 | enable 优先，一切关闭（验证条件注解互斥关系） |
| TC-025 | P1 | 未知配置项容错：`bt-response.unknown-key=x` | properties | 正常启动（ignoreUnknownFields=true 契约） |

## 三、返回类型与包装行为（BtResponseBodyAdvice）

| ID | 优先级 | 场景 | 实现方式 | 预期结果 |
| --- | --- | --- | --- | --- |
| TC-030 | P1 | Controller 直接返回 `BtResponse` 实例 | 新端点 `/index/v1/ret/btresponse` | 不二次包装，isInstance 透传（该分支零覆盖） |
| TC-031 | P1 | Controller 直接返回自定义工厂类型 `CustomResponse` | 新端点（custom 场景类） | 同上，isInstance 透传 |
| TC-032 | P1 | `void` 返回 + 204/200 | 新端点 | 记录基线：包装为默认响应或空 body（当前行为定基线） |
| TC-033 | P1 | `ResponseEntity<String>` 返回 | 新端点 | String 部分不包装 / 记录实际行为为契约 |
| TC-034 | P1 | `ResponseEntity<BtResponse>` 返回（如 201 Created） | 新端点 | body 透传不二次包装，status 保留 |
| TC-035 | P2 | Map / List<POJO> / 嵌套对象返回 | 新端点 | 正常包装，data 结构完整 |
| TC-036 | P2 | `@RestController` 之外的 `@Controller` + `@ResponseBody` 方法 | 新端点 | 同样被包装（advice 不依赖 @RestController） |
| TC-037 | P2 | 并发请求混合场景（可选） | MockMvc 并发 | 无状态串扰（advice/factory 无共享可变状态） |

## 四、HTTP 错误分发与路径

| ID | 优先级 | 场景 | 实现方式 | 预期结果 |
| --- | --- | --- | --- | --- |
| TC-040 | P1 | 404 未知路径：`GET /not/exist` | TRT | HTTP 404；统一格式 code=404；data.path=/not/exist（error 分发对 404 的覆盖，目前只测过 500） |
| TC-041 | P1 | 405 方法不允许：`POST /index/v1/ret/str` | TRT | HTTP 405；统一格式 code=405 |
| TC-042 | P1 | 自定义错误路径回归：`server.error.path=/____error` | 已隐式覆盖 | 显式断言错误响应来自统一格式（当前 testErrException 已隐式验证，可补注释说明） |
| TC-043 | P2 | 415 不支持的 Content-Type POST | TRT | 统一格式 code=415 |
| TC-044 | P2 | `server.error.include-message=always` 时 message 透传 | properties + TRT | data.message / 外层 message 按配置出现 |

## 五、force 强制模式专项（BtResponseBeanPostProcessor / BtMappingJackson2HttpMessageConverter）

| ID | 优先级 | 场景 | 实现方式 | 预期结果 |
| --- | --- | --- | --- | --- |
| TC-050 | **P0** | force 下 `spring.jackson.*` 定制生效：`properties={"spring.jackson.property-naming-strategy=SNAKE_CASE"}` + force | MockMvc | 包装后字段名为 snake_case（保护本次「复用容器 ObjectMapper」改动不回退） |
| TC-051 | P1 | force + 注册自定义 Jackson Module（如日期格式） | 新配置 | 定制生效于包装响应 |
| TC-052 | P1 | force + `@Ignore` 方法：`/method/ignore/v1/str` | force 场景类现有 MockMvc | 仍原样返回 "hello world"（force 不覆盖 Ignore） |
| TC-053 | P1 | force 下转换器优先级：返回 String 走 JSON 包装而非 StringHttpMessageConverter | 已覆盖（testRetStr@force） | 保持 |
| TC-054 | P2 | force 下容器无 ObjectMapper（排除 JacksonAutoConfiguration） | 新配置 | 兜底 mapper 生效，空 Bean → `{}`，启动不报错 |
| TC-055 | P2 | force 下枚举/LocalDate/Optional 返回 | 新端点 | 全部 JSON 包装成功 |

## 六、core 模块单元测试（当前为零）

| ID | 优先级 | 场景 | 实现方式 | 预期结果 |
| --- | --- | --- | --- | --- |
| TC-060 | **P0** | `GlobalError` 注册表完整性：28 个 code 全注册、client/server 分类正确 | Unit | `getDefaultExceptionMap().size()==28`；400-464 为 client 包，500-563 为 server 包 |
| TC-061 | P1 | `getExceptionClass` 未知 code 回退 500 | Unit | `getExceptionClass(99999) == UnknownException.class` |
| TC-062 | P1 | `errorInfoList` 与 `EXCEPTION_POOL` 一一对应（code/title 一致） | Unit | 28 条 ErrorInfo 的 code 与 Map 键一致，title 与类名一致 |
| TC-063 | P1 | `OperationException` 4 个构造器（含新加 cause 版本）code/message/data/cause 正确 | Unit | getter 断言 |
| TC-064 | P2 | 28 个预定义异常构造器矩阵 | Unit（参数化） | 每个异常的默认构造 code/message 正确；带参构造覆盖 message/data/cause |
| TC-065 | P2 | `BtResponseFactory.produceResponse(code==0)` → success=true；code!=0 → false | Unit | 布尔逻辑正确 |

## 七、集成与兼容（可选）

| ID | 优先级 | 场景 | 实现方式 | 预期结果 |
| --- | --- | --- | --- | --- |
| TC-070 | P2 | springdoc-openapi 端点不被包装（`/v3/api-docs`） | 引依赖 + MockMvc | 需 @Ignore 或文档说明限制（记录兼容性结论） |
| TC-071 | P2 | actuator `/health` 不被包装 | 引依赖 + TRT | 同上（advice 默认会包装 Jackson 响应，需评估） |
| TC-072 | P3 | spring-boot 3.x 小版本升级冒烟 | 升版本跑全量 | 89+ 用例全过 |

---

## 现有覆盖基线（123 用例）

| 测试类 | 场景 | 覆盖内容 |
| --- | --- | --- |
| IndexControllerTest (25) | 默认模式 | 各基础类型包装、String/byte[]/null 原样、OperationException(10100)→200、未处理异常→error分发→统一500格式（TRT） |
| IndexForceControllerTest (21) | force=true | 全类型 JSON 包装（含 String/byte[]/null/空Object{}） |
| IndexCustomControllerTest (21) | 自定义工厂 | CustomResponse{code,msg,data} 结构同矩阵 |
| IndexCustomForceControllerTest (21) | 自定义+force | 同上叠加 force |
| MethodIgnoreControllerTest (3) | @Ignore 方法级 | String/byte[]/POJO 原样返回 |
| GlobalExceptionControllerTest (5) | GlobalException | 404/506/461/561 code透传、自定义message+data |
| ValidationControllerTest (5) | 校验异常 | 字段级422/类级422/参数415/手动415/JSON请求体 |
| LooseModeControllerTest (2) | 宽松模式 | GlobalException→200、BindException→200 |
| EnableDisabledControllerTest (5) | enable=false | 裸返回、默认错误体、@Ignore不受影响 |
| EnableDisabledForceControllerTest (1) | enable=false+force | enable优先 |
| ResponseShapeControllerTest (6) | 返回结构 | isInstance透传/void基线/ResponseEntity/Map |
| ResponseShapeCustomControllerTest (2) | 自定义结构透传 | CustomResponse透传、BtResponse作为data |
| ErrorDispatchControllerTest (4) | 容器错误 | 404/405/415分发、include-message |
| FilterOperationExceptionTest (1) | Filter异常 | 当前基线：业务code丢失变500 |
| CustomErrorControllerTest (1) | 自定义ErrorController | starter让位+Advice代理 |
| JacksonCustomForceControllerTest (2) | force+Jackson定制 | snake_case生效、Ignore+force基线 |
| HttpStatusTest (1) | 杂项 | HttpStatus.resolve |
| core GlobalErrorTest/ExceptionsTest (8) | 注册表/构造器 | 28code完整性、client/server分类、fallback、构造器矩阵 |
| starter BtErrorControllerTest/BtResponseFactoryTest (9) | 单元 | ERROR_EXCEPTION各分支、data净化、工厂行为 |

**已知基线行为（测试时作为预期，非 bug）**：
1. 非 force 下 `null`（Object返回）为空 body；**void 返回会包装为默认响应**（`produceDefaultResponse` 的触达路径）。
2. 非 force 下 `new Object()` 返回 406（Boot 默认 Jackson 空 Bean 策略）。
3. 未处理异常经 BasicErrorController + Advice 代理，外层 `message` 为空串、`data` 含 `timestamp/status/error/path`（v2.0.2 设计）。
4. force + `@Ignore` 的 String 输出为 JSON 字符串（带引号）——Ignore 跳过包装但不跳过 force 序列化。
5. Filter 中抛 `OperationException` 业务 code 丢失（变 500），见 TC-010。
6. demo 中 `throw XxxException.Default` 使用静态单例，堆栈为类加载时点（已知设计债，勿在新增端点模仿）。
