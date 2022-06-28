package work.bottle.demo.model;

public class EmployeeAuth {
    private String authorizationKey;
    private Integer authTimestamp;
    private String username;
    private String mobile;

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    public Integer getAuthTimestamp() {
        return authTimestamp;
    }

    public void setAuthTimestamp(Integer authTimestamp) {
        this.authTimestamp = authTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
