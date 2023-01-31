package work.bottle.plugin.exception.global;

import java.util.ArrayList;
import java.util.List;

public class GlobalError {

    private static class ErrorInfo {
        public int code;
        public String message;
        public String description;
        public String type;
        public String title;

        public ErrorInfo(int code, String message, String description, String type, String title) {
            this.code = code;
            this.message = message;
            this.description = description;
            this.type = type;
            this.title = title;
        }
    }

    private static volatile GlobalError INSTANCE;

    public static GlobalError getInstance() {
        if (null == INSTANCE) {
            synchronized (GlobalError.class) {
                if (null == INSTANCE) {
                    INSTANCE = new GlobalError();
                    INSTANCE.populate();
                }
            }
        }
        return INSTANCE;
    }

    private int version;
    private List<ErrorInfo> errorInfoList;

    private void populate() {
        this.version = 1;
        this.errorInfoList = new ArrayList<>();
		
        errorInfoList.add(new ErrorInfo(400, "上下文错误", "一般是指客户端上下文异常, 无效的上下文, 消息帧, 欺骗性上下文等", "client", "Context"));
        errorInfoList.add(new ErrorInfo(401, "身份未认证异常, 请登录后重试", "一般是需要身份认证的接口, 用户访问时未登录触发. ", "client", "Unauthenticated"));
        errorInfoList.add(new ErrorInfo(403, "访问被拒绝, 缺失权限访问", "一般是需要授权的接口, 用户访问时未被授权", "client", "Forbidden"));
        errorInfoList.add(new ErrorInfo(404, "访问内容不存在", "访问的资源/文件/数据等不存在时抛出的异常", "client", "NotFound"));
        errorInfoList.add(new ErrorInfo(405, "不支持的访问", "不允许访问, 通常因为用户身份, 来源等因素, 区别与Forbidden", "client", "NotAllowed"));
        errorInfoList.add(new ErrorInfo(406, "不接受的访问", "服务器无法接受客户端的访问", "client", "NotAcceptable"));
        errorInfoList.add(new ErrorInfo(408, "处理超时", "操作处理超时", "client", "Timeout"));
        errorInfoList.add(new ErrorInfo(409, "状态冲突", "服务器处理业务时, 应具有一个前置状态, 但状态检查不通过的情况.", "client", "Conflict"));
        errorInfoList.add(new ErrorInfo(413, "请求过大", "请求内容超出服务能够处理的上限", "client", "TooLarge"));
        errorInfoList.add(new ErrorInfo(415, "不支持的请求内容", "一般是有特定的参数不被支持, 比如支付下单接口的第三方支付方式异常. 可根据上下文判断是什么不被支持.", "client", "Unsupported"));
        errorInfoList.add(new ErrorInfo(416, "请求越界", "请求上下文超出可容纳的范围的情况.", "client", "OutOfBounds"));
        errorInfoList.add(new ErrorInfo(418, "服务器拒绝", "请求被服务器拒绝, 原因未知", "client", "Negative"));
        errorInfoList.add(new ErrorInfo(422, "处理失败, 不能处理.", "单纯程序处理失败. 大多是由于上报的数据格式有问题而触发", "client", "Unprocessable"));
        errorInfoList.add(new ErrorInfo(423, "已锁定异常", "访问的资源被锁定", "client", "Locked"));
        errorInfoList.add(new ErrorInfo(425, "过早的异常", "表示请求的内容还未能使用(一般是指时间过早)的异常", "client", "Premature"));
        errorInfoList.add(new ErrorInfo(429, "访问过于频繁, 请稍候再试", "一般存在于限制访问频率的接口, 用户访问频率过高", "client", "Frequency"));
        errorInfoList.add(new ErrorInfo(460, "已过期异常", "表示请求的内容已过期(一般是指时间过晚)的异常 (非HTTP定义)", "client", "Expired"));
        errorInfoList.add(new ErrorInfo(461, "身份未知", "未知的身份, 一般存在于登录接口上, 表示用户不存在或未注册.", "client", "UnknownIdentity"));
        errorInfoList.add(new ErrorInfo(462, "无效的口令", "需要身份认证时(比如登录接口), 口令不正确触发的异常", "client", "InvalidPassword"));
        errorInfoList.add(new ErrorInfo(463, "唯一性异常", "一般是提交了重复的数据请求(比如重复下单, 重复注册<用户已存在>等)", "client", "Uniqueness"));
        errorInfoList.add(new ErrorInfo(464, "签名异常", "一般是某些访问需要数据签名的情况 . ", "client", "Signature"));
        errorInfoList.add(new ErrorInfo(500, "服务处理失败", "对应Internal server error", "server", "Unknown"));
        errorInfoList.add(new ErrorInfo(502, "服务下线，暂时不可用", "存在第三方外部调用的情况下, 外部链路出现未知的错误的情况", "server", "BadGateway"));
        errorInfoList.add(new ErrorInfo(503, "服务不可用，过载保护", "请求在资源未准备充足的情况下到来, 服务端无法正常处理的情况", "server", "Unavailable"));
        errorInfoList.add(new ErrorInfo(506, "服务器配置错误", "服务器在资源处理时出现配置错误.", "server", "Configuration"));
        errorInfoList.add(new ErrorInfo(561, "缺少可用资源", "资源不足时触发", "server", "Insufficient"));
        errorInfoList.add(new ErrorInfo(562, "重复调用异常", "重复调用时触发, 和Uniqueness类似", "server", "Duplicated"));
        errorInfoList.add(new ErrorInfo(563, "授权异常", "授权异常, 表示授权失败. ", "server", "Authorization"));
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ErrorInfo> getErrorInfoList() {
        return errorInfoList;
    }

    public void setErrorInfoList(List<ErrorInfo> errorInfoList) {
        this.errorInfoList = errorInfoList;
    }
}
