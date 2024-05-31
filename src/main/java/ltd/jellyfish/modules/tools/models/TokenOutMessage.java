package ltd.jellyfish.modules.tools.models;

import java.util.*;

public class TokenOutMessage {

    private String username;

    private String uuid;

    private String token;

    private Date issueTime;

    public TokenOutMessage() {
    }

    public TokenOutMessage(String username, String uuid, String token, Date issueTime) {
        this.username = username;
        this.uuid = uuid;
        this.token = token;
        this.issueTime = issueTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

}
