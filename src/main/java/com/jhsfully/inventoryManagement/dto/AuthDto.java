package com.jhsfully.inventoryManagement.dto;

import com.jhsfully.inventoryManagement.entity.MemberEntity;
import com.jhsfully.inventoryManagement.type.RoleType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class AuthDto {

    @Getter
    @AllArgsConstructor
    public static class SignIn{
        private String username;
        private String password;
    }
    @Getter
    @AllArgsConstructor
    public static class SignUp{
        private String username;
        private String password;
        private String name;
        private String department;
        private List<RoleType> roles;
    }

    @Getter
    @AllArgsConstructor
    public static class PasswordChangeRequest{
        private String username;
        private String originPassword;
        private String newPassword;

    }

    @Getter
    @AllArgsConstructor
    public static class PasswordInitializeRequest{
        private String username;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class UserChangeRequest{
        private String username;
        private String name;
        private String department;
        private List<RoleType> roles;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserResponse{
        private String username;
        private String name;
        private String department;
        private List<String> roles;

        public static UserResponse of(MemberEntity member){
            return UserResponse.builder()
                    .username(member.getUsername())
                    .name(member.getName())
                    .department(member.getDepartment())
                    .roles(member.getRoles())
                    .build();
        }
    }


}
