package work.bottle.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 验证码 pojo
 */
public class VerificationData {
    // 验证码内容
    private String code;
    // 过期时间
    private Integer expireTimestamp;
    // 下次重发时间
    private Integer nextSentTimestamp;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getExpireTimestamp() {
        return expireTimestamp;
    }

    public void setExpireTimestamp(Integer expireTimestamp) {
        this.expireTimestamp = expireTimestamp;
    }

    public Integer getNextSentTimestamp() {
        return nextSentTimestamp;
    }

    public void setNextSentTimestamp(Integer nextSentTimestamp) {
        this.nextSentTimestamp = nextSentTimestamp;
    }

    @DateTimeFormat(pattern = "yyyy-mm-dd HH:MM:SS")
    public Date getExpireTime() {
        return new Date((long)this.expireTimestamp * 1000);
    }

    @DateTimeFormat(pattern = "yyyy-mm-dd HH:MM:SS")
    public Date getNextSentTime() {
        return new Date((long)this.nextSentTimestamp * 1000);
    }
}
