package com.jhsfully.inventoryManagement.dto;

import com.jhsfully.inventoryManagement.entity.MemberEntity;
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

}
