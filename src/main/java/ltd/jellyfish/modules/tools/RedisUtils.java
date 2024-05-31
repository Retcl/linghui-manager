package ltd.jellyfish.modules.tools;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import ltd.jellyfish.modules.authentication.models.ToeknStorage;
import ltd.jellyfish.modules.tools.models.TokenOutMessage;

@Configuration
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, TokenOutMessage> tokenOutMessageTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    public void tokenOutTheToken(String token){
        String uuid = jwtUtils.getTokenUUID(token);
        String username = jwtUtils.getToeknUsername(token);
        Date issueDate = jwtUtils.getIssueTime(token);
        TokenOutMessage tokenOutMessage = new TokenOutMessage();
        tokenOutMessage.setUuid(uuid);
        tokenOutMessage.setUsername(username);
        tokenOutMessage.setToken(token);
        tokenOutMessage.setIssueTime(issueDate);
        tokenOutMessageTemplate.opsForValue().set(uuid, tokenOutMessage);
    }

    public boolean isTokenOut(String token) {
        String uuid = jwtUtils.getTokenUUID(token);
        boolean reply = false;
        if (tokenOutMessageTemplate.hasKey(uuid)){
            TokenOutMessage tokenOutMessage = tokenOutMessageTemplate.opsForValue().get(uuid);
            String username = jwtUtils.getToeknUsername(token);
            Date issueTime = jwtUtils.getIssueTime(token);
            if (tokenOutMessage.getUsername().equals(username) && tokenOutMessage.getIssueTime().getTime() == issueTime.getTime() && tokenOutMessage.getToken().equals(token)) {
                reply = true;
            }
        }
        return reply;
    }

}
