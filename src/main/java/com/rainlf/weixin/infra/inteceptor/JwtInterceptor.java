package com.rainlf.weixin.infra.inteceptor;

import com.rainlf.weixin.infra.db.repository.UserRepository;
import com.rainlf.weixin.infra.db.entity.User;
import com.rainlf.weixin.infra.sso.SsoService;
import com.rainlf.weixin.infra.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author rain
 * @date 6/14/2024 9:25 AM
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SsoService ssoService;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ssoService.cleanCurrentUser();

        // 从 request 获取 JWT token
        String token = getTokenFromRequest(request);

        // 校验 token
        if (StringUtils.hasText(token)) {
            // 从 token 获取 openId
            String openId = JwtUtils.getOpenId(token);

            User user = userRepository.findByOpenId(openId).orElseThrow(() -> new RuntimeException("invalid jwt token"));
            ssoService.setCurrentUser(user);
            return true;
        } else {
            throw new RuntimeException("invalid jwt token");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ssoService.cleanCurrentUser();
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
