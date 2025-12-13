package com.course.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.course.annotation.RequireTableToken;
import com.course.enums.ResultCode;
import com.course.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class TableTokenAspect {
    @Autowired
    private JwtUtil jwtUtil;

    @Before("@annotation(requireTableToken)")
    protected void validateTableToken(JoinPoint joinPoint, RequireTableToken requireTableToken) {
        try {

            // 取得 HttpServletRequest
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            String token = request.getHeader("Table-Token");
            if (token == null) {
                throw new JwtException(ResultCode.TABLE_TOKEN_MISSING.name());
            }
            Claims claims = jwtUtil.validateTableToken(token);
            Integer tableId = claims.get("tableId", Integer.class);
            request.setAttribute("tableId", tableId);

            if (requireTableToken.validateOpenedAt()) {
                String openedAtStr = claims.get("openedAt", String.class);
                LocalDateTime openedAt = LocalDateTime.parse(openedAtStr);
                request.setAttribute("openedAt", openedAt);
            }
        } catch (JwtException e) {
            throw new JwtException(e.getMessage(), e);
        }

    }
}
