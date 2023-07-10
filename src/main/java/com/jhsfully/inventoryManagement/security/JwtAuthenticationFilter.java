package com.jhsfully.inventoryManagement.security;

import com.jhsfully.inventoryManagement.dto.TokenDto;
import com.jhsfully.inventoryManagement.exception.AuthException;
import com.jhsfully.inventoryManagement.type.AuthErrorType;
import com.jhsfully.inventoryManagement.type.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.jhsfully.inventoryManagement.type.AuthErrorType.*;
import static com.jhsfully.inventoryManagement.type.RoleType.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String ACCESS_TOKEN_HEADER = "AccessToken";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String TOKEN_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        if(isSkipUrl(requestURI)){
            filterChain.doFilter(request, response);
            return;
        }


        String accessToken = resolveTokenFromRequest(request, ACCESS_TOKEN_HEADER);
        String refreshToken = resolveTokenFromRequest(request, REFRESH_TOKEN_HEADER);

        System.out.println(accessToken);
        System.out.println(refreshToken);

        if(StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)){
            //권한 확인 및 부여
            List<String> roles = tokenProvider.getRoles(accessToken);
            String userName = tokenProvider.getUsername(accessToken);

            System.out.println(roles);
            System.out.println("?????");
            for(String role : roles){
                if(role.equals(REFRESH)){
                    throw new AuthException(AUTH_SECURITY_ERROR);
                }
            }

            Authentication auth = tokenProvider.getAuthentication(roles, userName);
            SecurityContextHolder.getContext().setAuthentication(auth);

        }else if(StringUtils.hasText(accessToken) &&
                    StringUtils.hasText(refreshToken) &&
                tokenProvider.validateRefreshToken(new TokenDto(accessToken, refreshToken), request)){

                String username = tokenProvider.getUsername(accessToken);
                List<String> roles = tokenProvider.getRoles(accessToken);

                String newAccessToken = tokenProvider.generateAccessToken(username, roles);
                response.addHeader("accessToken", newAccessToken);

                Authentication auth = tokenProvider.getAuthentication(roles, username);
                SecurityContextHolder.getContext().setAuthentication(auth);
        }else{
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request, String tokenHeader){
        String token = request.getHeader(tokenHeader);

        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private boolean isSkipUrl(String requestURI){
        if(requestURI.equals("/"))return true;
        if(requestURI.equals("/auth/signin"))return true;
        if(requestURI.equals("/auth/user/update/password"))return true;
        if(requestURI.startsWith("/swagger"))return true;
        if(requestURI.startsWith("/v2"))return true;
        if(requestURI.startsWith("/h2-"))return true;
        return false;
    }
}
