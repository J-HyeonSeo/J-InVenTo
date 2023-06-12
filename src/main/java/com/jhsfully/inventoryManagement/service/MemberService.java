package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.AuthDto;
import com.jhsfully.inventoryManagement.exception.AuthException;
import com.jhsfully.inventoryManagement.entity.MemberEntity;
import com.jhsfully.inventoryManagement.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.jhsfully.inventoryManagement.type.AuthErrorType.AUTH_LOGIN_FAILED;
import static com.jhsfully.inventoryManagement.type.AuthErrorType.AUTH_REGISTER_EXISTS_USERNAME;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("cannot find user -> " + username));
    }

    public MemberEntity register(AuthDto.SignUp member){
        boolean exists = memberRepository.existsByUsername(member.getUsername());
        if(exists){
            throw new AuthException(AUTH_REGISTER_EXISTS_USERNAME);
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member.toEntity());
    }

    public MemberEntity authenticate(AuthDto.SignIn member){
        MemberEntity user = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new AuthException(AUTH_LOGIN_FAILED));

        if(!passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new AuthException(AUTH_LOGIN_FAILED);
        }

        return user;
    }

}
