package ltd.jellyfish.modules.authentication.filter;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import ltd.jellyfish.modules.authentication.token.core.TokenAnnotationConfig;
import ltd.jellyfish.modules.authentication.token.core.UnuseTokenAnnotationConfig;
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

    @Value("${token.use-manual}")
    private boolean useIt;

    @Value("${token.unuse-manual}")
    private boolean unuseIt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String url = request.getRequestURI();

        // region use the @UseToken annotation.
        if (!url.equals("/login")) {
            if (useIt) {
                TokenAnnotationConfig annotationConfig = new TokenAnnotationConfig();
                List<String> needTokenPaths = new ArrayList<>();
                try {
                    needTokenPaths = annotationConfig.getUseTokenPath();
                    for (String useTokenPath : needTokenPaths) {
                        if (url.equals(useTokenPath)) {
                            String token = request.getHeader("X-Request-Token");
                            if (token != null && !token.isEmpty()) {
                                if (!redisUtils.isTokenOut(token)) {
                                    String username = jwtUtils.getToeknUsername(token);
                                    if (username != null && !username.isEmpty()
                                            && SecurityContextHolder.getContext().getAuthentication() == null) {
                                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                        auth(userDetails, request);
                                    }
                                } else {
                                    fail(request, response);
                                }
                            } else {
                                fail(request, response);
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                UnuseTokenAnnotationConfig unuseTokenAnnotationConfig = new UnuseTokenAnnotationConfig();
                List<String> unuseList = new ArrayList<>();
                try {
                    unuseList = unuseTokenAnnotationConfig.getUnusePathList();
                    for (String unuseTokenPath : unuseList) {
                        if (!url.equals(unuseTokenPath)) {
                            String token = request.getHeader("X-Request-Token");
                            if (token != null && !token.isEmpty()) {
                                if (!redisUtils.isTokenOut(token)) {
                                    String username = jwtUtils.getToeknUsername(token);
                                    if (username != null && !username.isEmpty()
                                            && SecurityContextHolder.getContext().getAuthentication() == null) {
                                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                        auth(userDetails, request);
                                    }
                                } else {
                                    fail(request, response);
                                }
                            } else {
                                fail(request, response);
                            }
                        } else {
                            break;
                        }
                    }
                } catch (ClassNotFoundException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

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

    public void auth(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
