package com.example.springsecurity.dto;


import com.example.springsecurity.domain.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private String nickname;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

    @Builder
    public MemberDto(Long id, String email, String password,String nickname){
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
