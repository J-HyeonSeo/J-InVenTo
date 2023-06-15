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

import javax.management.relation.Role;
import java.util.*;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.AuthErrorType.*;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private MemberRepository memberRepository;

    public List<String> getRoleLists(){
        return Arrays.stream(RoleType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("cannot find user -> " + username));
    }

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

        HashSet<String> roleSets = new HashSet<>();
        Arrays.stream(RoleType.values())
                .forEach(x -> roleSets.add(x.name()));

        for(String role : member.getRoles()){
            if(!roleSets.contains(role)){
                throw new AuthException(AUTH_NOT_DEFINITION_ROLE_TYPE);
            }
        }

        boolean exists = memberRepository.existsById(member.getUsername());
        if(exists){
            throw new AuthException(AUTH_REGISTER_EXISTS_USERNAME);
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member.toEntity());
    }

    public MemberEntity authenticate(AuthDto.SignIn member){

        if(memberRepository.count() == 0){ //첫 사용시.. 권한 처리.
            if("admin".equals(member.getUsername()) && "admin".equals(member.getPassword())){
                MemberEntity AdminMember = MemberEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .name("관리자")
                        .department("관리자")
                        .roles(new ArrayList<String>(Arrays.asList(RoleType.ROLE_ADMIN.name())))
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

    public void changeProfile(AuthDto.UserChangeRequest request){

        MemberEntity user = memberRepository.findById(request.getUsername())
                .orElseThrow(() -> new AuthException(AUTH_LOGIN_FAILED));

        boolean changeName = false;
        boolean changeDepartment = false;
        boolean changeRoles = false;

        if(request.getName() != null && request.getName().trim().equals("")){
            changeName = true;
        }

        if(request.getDepartment() != null && request.getDepartment().trim().equals("")){
            changeDepartment = true;
        }

        HashSet<String> roleSets = new HashSet<>();
        Arrays.stream(RoleType.values())
                .forEach(x -> roleSets.add(x.name()));


        for(int i = 0; i < request.getRoles().size(); i++){

            String role = request.getRoles().get(i);

            if(!roleSets.contains(role)){
                throw new AuthException(AUTH_NOT_DEFINITION_ROLE_TYPE);
            }
            changeRoles = true;
        }

        if(changeName){
            user.setName(request.getName());
        }

        if(changeDepartment){
            user.setDepartment(request.getDepartment());
        }

        if(changeRoles){
            user.setRoles(request.getRoles());
        }

    }

}
