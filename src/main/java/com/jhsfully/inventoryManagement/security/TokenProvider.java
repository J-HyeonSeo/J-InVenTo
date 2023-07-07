package com.jhsfully.inventoryManagement.security;

import com.jhsfully.inventoryManagement.dto.TokenDto;
import com.jhsfully.inventoryManagement.exception.AuthException;
import com.jhsfully.inventoryManagement.entity.RefreshToken;
import com.jhsfully.inventoryManagement.repository.RefreshTokenRepository;
import com.jhsfully.inventoryManagement.service.MemberService;
import com.jhsfully.inventoryManagement.type.AuthErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.RoleType.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    //Access Token 기한 = 30분
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30 * 24;
    //Refresh Token 기한 = 2주
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;

    private static final String KEY_ROLES = "roles";
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${spring.jwt.secret}")
    private String secretKey;
    
    //토큰 생성 메서드
    public String generateAccessToken(String username, List<String> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        Date now = new Date();
        Date accessExpiredDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessExpiredDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String generateRefreshToken(String username, HttpServletRequest request){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, new ArrayList<String>(Arrays.asList(REFRESH)));

        Date now = new Date();
        Date refreshExpiredDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        String refreshTokenString = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshExpiredDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        RefreshToken refreshToken = new RefreshToken(refreshTokenString, request.getRemoteAddr(), request.getHeader("User-Agent"));
        refreshTokenRepository.save(refreshToken);

        return refreshTokenString;
    }


    public Authentication getAuthentication(List<String> roles, String userName){

        List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userName, "", grantedAuthorities);
    }

    public String getUsername(String token){
        return this.parseClaims(token).getSubject();
    }

    public List<String> getRoles(String token){
        Claims claims = parseClaims(token);
        String rolesString = claims.get(KEY_ROLES).toString();
        rolesString = rolesString.substring(1, rolesString.length() - 1);
        return Arrays.stream(rolesString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token){
        if(!StringUtils.hasText(token)){
            return false;
        }

        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    public boolean validateRefreshToken(TokenDto token, HttpServletRequest request){

        //값이 비었는지 여부.
        if(!StringUtils.hasText(token.getAccessToken())){
            return false;
        }
        if(!StringUtils.hasText(token.getRefreshToken())){
            return false;
        }

        //토큰 복호화
        Claims claimsAccess = parseClaims(token.getAccessToken());
        Claims claimsRefresh = parseClaims(token.getRefreshToken());

        //refreshToken 기한 만료
        if(claimsRefresh.getExpiration().before(new Date())){
            return false;
        }

        //accessToken, refreshToken username 불일치
        if(!claimsAccess.getSubject().equals(claimsRefresh.getSubject())){
            return false;
        }

        //redis에 접속해서 검증이 필요함.
        RefreshToken refreshToken = refreshTokenRepository.findById(token.getRefreshToken())
                .orElse(null);

        if(refreshToken == null){
            return false;
        }

        //로그인 할 때 접속했던 IP와 동일한지 체크.
        if(!refreshToken.getRemoteAddress().equals(request.getRemoteAddr())){
            //접속된 IP가 다르다면, 이건 진짜 탈취가능성이 높으므로, refreshToken 파기.
            refreshTokenRepository.delete(refreshToken);
            return false;
        }

        //로그인 할 때 접속했던 접속환경이 동일한지 체크.
        if(!refreshToken.getUserAgent().equals(request.getHeader("User-Agent"))){
            return false;
        }

        return true;
    }

    //토큰 파싱
    private Claims parseClaims(String token){
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}
