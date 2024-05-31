package ltd.jellyfish.modules.authentication.models;

import com.alibaba.fastjson.JSON;

public class ToeknStorage {

    private String username;
    
    private String message;

    private String token;

    public ToeknStorage(String username, String message, String token) {
        this.username = username;
        this.message = message;
        this.token = token;
    }

    public ToeknStorage() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }    

    public byte[] toBytes(){
        return JSON.toJSONBytes(this);
    }

}
