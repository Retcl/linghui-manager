package ltd.jellyfish.modules.authentication.filter;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ltd.jellyfish.models.Result;
import ltd.jellyfish.modules.tools.JwtUtils;
import ltd.jellyfish.modules.tools.RedisUtils;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String url = request.getRequestURI();
        String token = request.getHeader("X-Request-Token");
        //region add the no-need URL
        List<String> urlList = new ArrayList<>();

        //region do the filter
        for (String catUrl : urlList) {
            if (catUrl.endsWith("*")) {
                catUrl = catUrl.substring(0, catUrl.lastIndexOf("/"));
                if (url.contains(catUrl)) {
                    filterChain.doFilter(request, response);
                }else{
                    if (redisUtils.isTokenOut(token)){
                        fail(request, response);
                    } else {
                        if(jwtUtils.isTokenExpire(token)){
                            fail(request, response);
                        }else{
                            String username = jwtUtils.getToeknUsername(token);
                            if (username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                auth(userDetails, request);
                            }
                        }
                    }
                }
            }else{
                if (!catUrl.equals(url)) {
                    if(jwtUtils.isTokenExpire(token)){
                        fail(request, response);
                    }else{
                        String username = jwtUtils.getToeknUsername(token);
                        if (username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            auth(userDetails, request);
                        }
                    }
                }
            }
        }
        //endregion
        //endregion
        filterChain.doFilter(request, response);
    }

    private void fail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        Result<?> result = new Result<>();
        result.setSuccess(false);
        result.setMessage("Not request");
        outputStream.write(result.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public void auth(UserDetails userDetails, HttpServletRequest request){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


}
