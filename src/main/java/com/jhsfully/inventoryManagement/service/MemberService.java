package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.AuthDto;
import com.jhsfully.inventoryManagement.exception.AuthException;
import com.jhsfully.inventoryManagement.entity.MemberEntity;
import com.jhsfully.inventoryManagement.repository.MemberRepository;
import com.jhsfully.inventoryManagement.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.*;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.AuthErrorType.*;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class MemberService implements UserDetailsService, MemberInterface {

    private PasswordEncoder passwordEncoder;
    private MemberRepository memberRepository;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ADMIN_NAME = "관리자";
    private static final String ADMIN_DEPARTMENT = "관리부서";

    @Override
    @Transactional(readOnly = true)
    public List<AuthDto.UserResponse> getMembers(){
        return memberRepository.findAll().stream()
                .map(AuthDto.UserResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getRoleLists(){
        return Arrays.stream(RoleType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("cannot find user -> " + username));
    }

    @Override
    public MemberEntity register(AuthDto.SignUp member){

        if(member.getUsername() == null || member.getUsername().trim().equals("")){
            throw new AuthException(AUTH_USERNAME_IS_NULL_OR_EMPTY);
        }

        if(member.getName() == null || member.getName().trim().equals("")){
            throw new AuthException(AUTH_NAME_IS_NULL_OR_EMPTY);
        }

        if(member.getDepartment() == null || member.getDepartment().trim().equals("")){
            throw new AuthException(AUTH_DEPARTMENT_IS_NULL_OR_EMPTY);
        }

        boolean exists = memberRepository.existsById(member.getUsername());
        if(exists) {
            throw new AuthException(AUTH_REGISTER_EXISTS_USERNAME);
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member.toEntity());
    }

    @Override
    public MemberEntity authenticate(AuthDto.SignIn member){

        if(memberRepository.count() == 0){ //첫 사용시.. 권한 처리.
            if(ADMIN_USERNAME.equals(member.getUsername()) && ADMIN_PASSWORD.equals(member.getPassword())){
                MemberEntity AdminMember = MemberEntity.builder()
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .name(ADMIN_NAME)
                        .department(ADMIN_DEPARTMENT)
                        .roles(new ArrayList<String>(List.of(RoleType.ROLE_ADMIN.name())))
                        .build();
                memberRepository.save(AdminMember);
            }
        }

        MemberEntity user = memberRepository.findById(member.getUsername())
                .orElseThrow(() -> new AuthException(AUTH_LOGIN_FAILED));

        if(!passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new AuthException(AUTH_LOGIN_FAILED);
        }

        return user;
    }

    @Override
    public void changePassword(AuthDto.PasswordChangeRequest request){
        MemberEntity user = memberRepository.findById(request.getUsername())
                .orElseThrow(() -> new AuthException(AUTH_LOGIN_FAILED));

        if(!passwordEncoder.matches(request.getOriginPassword(), user.getPassword())){
            throw new AuthException(AUTH_LOGIN_FAILED);
        }

        if(request.getNewPassword() == null || request.getNewPassword().trim().equals("")){
            throw new AuthException(AUTH_CHANGE_PASSWORD_NULL_OR_EMPTY);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(user);
    }

    @Override
    public void changeProfile(AuthDto.UserChangeRequest request){

        MemberEntity user = memberRepository.findById(request.getUsername())
                .orElseThrow(() -> new AuthException(AUTH_LOGIN_FAILED));

        user.setName(request.getName());
        user.setDepartment(request.getDepartment());
        user.setRoles(request.getRoles().stream().map(Enum::name).collect(Collectors.toList()));

        memberRepository.save(user);
    }

    //관리자가 수행하는 비밀번호 초기화.
    @Override
    public void initializePassword(AuthDto.PasswordInitializeRequest request) {

        MemberEntity member = memberRepository.findById(request.getUsername())
                .orElseThrow(() -> new AuthException(AUTH_LOGIN_FAILED));

        member.setPassword(passwordEncoder.encode(request.getPassword()));

        memberRepository.save(member);
    }
}
