package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.AuthDto;
import com.jhsfully.inventoryManagement.dto.TokenDto;
import com.jhsfully.inventoryManagement.model.MemberEntity;
import com.jhsfully.inventoryManagement.security.TokenProvider;
import com.jhsfully.inventoryManagement.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthDto.SignUp request){
        log.info("user register -> " + request.getUsername());
        return ResponseEntity.ok(memberService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(HttpServletRequest httpRequest, @RequestBody AuthDto.SignIn request){
        MemberEntity member = memberService.authenticate(request);
        return ResponseEntity.ok(
                new TokenDto(tokenProvider.generateAccessToken(member.getUsername(), member.getRoles()),
                tokenProvider.generateRefreshToken(member.getUsername(), httpRequest)
                ));
    }

}
