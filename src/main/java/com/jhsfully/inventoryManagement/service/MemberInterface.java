package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.AuthDto;
import com.jhsfully.inventoryManagement.entity.MemberEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberInterface {

    List<AuthDto.UserResponse> getMembers();

    List<String> getRoleLists();

    MemberEntity register(AuthDto.SignUp member);

    MemberEntity authenticate(AuthDto.SignIn member);

    void changePassword(AuthDto.PasswordChangeRequest request);

    void changeProfile(AuthDto.UserChangeRequest request);

    //관리자가 수행하는 비밀번호 초기화.
    void initializePassword(AuthDto.PasswordInitializeRequest request);
}
