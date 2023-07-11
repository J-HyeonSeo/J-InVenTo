package com.jhsfully.inventoryManagement.restcontroller;

import com.jhsfully.inventoryManagement.dto.AuthDto;
import com.jhsfully.inventoryManagement.dto.TokenDto;
import com.jhsfully.inventoryManagement.entity.MemberEntity;
import com.jhsfully.inventoryManagement.security.TokenProvider;
import com.jhsfully.inventoryManagement.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @GetMapping("/admin/ping")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> isAdmin(){
        return ResponseEntity.ok(true);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMembers(){
        return ResponseEntity.ok(memberService.getMembers());
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
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

    @PutMapping("user/update/password")
    public ResponseEntity<?> changePassword(@RequestBody AuthDto.PasswordChangeRequest request){
        memberService.changePassword(request);
        return ResponseEntity.ok(request.getUsername());
    }

    @PutMapping("admin/update/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> initializePassword(@RequestBody AuthDto.PasswordInitializeRequest request){
        memberService.initializePassword(request);
        return ResponseEntity.ok(request.getUsername());
    }

    @PutMapping("/admin/update/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProfile(@RequestBody AuthDto.UserChangeRequest request){
        memberService.changeProfile(request);
        return ResponseEntity.ok(request.getUsername());
    }

    @GetMapping("/admin/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRoleLists(){
        return ResponseEntity.ok(memberService.getRoleLists());
    }

}
