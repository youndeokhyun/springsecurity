package com.example.springsecurity.service;

import com.example.springsecurity.domain.Role;
import com.example.springsecurity.domain.entity.MemberEntity;
import com.example.springsecurity.domain.repository.MemberRepository;
import com.example.springsecurity.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepostory;

    // 회원가입 저장 및 비번 암호화
    @Transactional
     public Long joinUser(MemberDto memberDto){
        // 비번 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        return memberRepostory.save(memberDto.toEntity()).getid();
    }

    //상세 정보를 조회하는 메서드이며, 사용자의 계정정보와 권한을 갖는 UserDetails 인터페이스를 반환
    @Override
    public UserDetails loadUserbyUsername(String userEmanil) throws UsernameNotFoundException {

        Optional<MemberEntity> userEntityWrapper = memberRepostory.findByEmail(userEmanil);
        MemberEntity userEntity = userEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();
        //롤(권한) 을 부여하는 코드 RoleEntity를 만들어 매핑
        if(("admin@example.com").equals(userEmanil)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }else{
            authorities.add(new SimpleGrantedAuthority((Role.MEMBER.getValue())));
        }
        //return은 SpringSecurity에서 제공하는 UserDetails를 구현한 User를 반환
        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);

    }

}
