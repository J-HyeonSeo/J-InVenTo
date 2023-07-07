package com.jhsfully.inventoryManagement.dto;

import com.jhsfully.inventoryManagement.entity.MemberEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class AuthDto {

    @Data
    public static class SignIn{
        private String username;
        private String password;
    }
    @Data
    public static class SignUp{
        private String username;
        private String password;
        private String name;
        private String department;
        private List<String> roles;

        public MemberEntity toEntity(){
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .name(this.name)
                    .department(this.department)
                    .roles(this.roles)
                    .build();
        }
    }

    @Data
    public static class PasswordChangeRequest{
        private String username;
        private String originPassword;
        private String newPassword;

    }

    @Data
    public static class PasswordInitializeRequest{
        private String username;
        private String password;
    }

    @Data
    public static class UserChangeRequest{
        private String username;
        private String name;
        private String department;
        private List<String> roles;
    }

    @Data
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
