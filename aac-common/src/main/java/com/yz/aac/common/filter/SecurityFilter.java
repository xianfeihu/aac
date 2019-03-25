package com.yz.aac.common.filter;


import com.yz.aac.common.config.SecurityConfig;
import com.yz.aac.common.exception.SerializationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_UNAUTHORIZED;
import static com.yz.aac.common.Constants.ResponseStatusInfo.HTTP_UNAUTHORIZED;
import static com.yz.aac.common.model.response.RootResponse.buildError;
import static com.yz.aac.common.util.JsonUtil.beanToJson;
import static com.yz.aac.common.util.PathUtil.match;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@SuppressWarnings("unused")
@Slf4j
@Configuration
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        if (isDisabledRequest(securityConfig.getDisabledUriPatterns(), uri)) {
            denyAccess(response);
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isDisabledRequest(List<String> disabledUriPatterns, String requestUri) {
        boolean result = false;
        for (String pattern : disabledUriPatterns) {
            if (match(pattern, requestUri)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void denyAccess(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HTTP_UNAUTHORIZED.code());
        String result = "";
        try {
            result = beanToJson(buildError(HTTP_UNAUTHORIZED.code(), MSG_UNAUTHORIZED.code(), MSG_UNAUTHORIZED.message()));
        } catch (SerializationException e) {
            log.error(e.getMessage(), e);
        }
        response.getWriter().println(result);
    }

}