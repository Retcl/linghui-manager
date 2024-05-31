package ltd.jellyfish.modules.authentication.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ltd.jellyfish.models.Result;
import ltd.jellyfish.modules.authentication.models.ToeknStorage;
import ltd.jellyfish.modules.tools.JwtUtils;

public class SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        String username = (String) authentication.getPrincipal();
        String token = jwtUtils.buildToken(username);
        ToeknStorage toeknStorage = new ToeknStorage(username, "success token", token);
        Result<ToeknStorage> result = new Result<>();
        result.setSuccess(true);
        result.setMessage("Login Success");
        result.setData(toeknStorage);
        outputStream.write(result.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
