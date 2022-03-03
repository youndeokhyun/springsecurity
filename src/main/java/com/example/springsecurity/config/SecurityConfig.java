package com.example.springsecurity.config;

import com.example.springsecurity.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration 클래스에 @EnableWebSecurity 어노테이션을 추가하여 Spring Security 설정할 클래스라고 정의합니다.
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder{
        return new BCryptPasswordEncoder();
    }

    //WebSecurity는 FilterChainProxy를 생성하는 필터입니다.
    @Override
    public void configure(WebSecurity web)throws Exception{
        //static 디렉터리의 하위 파일 목록은 인중 무시 (= 항상통과)0
        web.ignoring().antMatchers("/css/**", "/js/**" ,"/img/**" , "/lib/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests() // HttpServletRequest에 따라 접근(access)을 제한합니다.
                /*페이지 권한설정
                 antMatchers() 메서드로 특정 경로를 지정하며,
                 permitAll(), hasRole() 메서드로 역할(Role)에 따른 접근 설정을 잡아줍니다.
                 여기서 role 는 권한을 의미 */
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/myinfo").hasRole("MEMBER")
                .antMatchers("/**").permitAll()
              .and()
                /* 로그인 설정
                form 기반으로 인증을 하도록 합니다. 로그인 정보는 기본적으로 HttpSession을 이용합니다.
                login 경로로 접근하면, Spring Security에서 제공하는 로그인 form을 사용할 수 있습니다.
                 */
                .formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/user/login/result")
                .permitAll()
             .and()
                /* 로그아웃
                   로그아웃을 지원하는 메서드이며, WebSecurityConfigurerAdapter를 사용할 때 자동으로 적용됩니다.
                   기본적으로 "/logout"에 접근하면 HTTP 세션을 제거합니다.
                */
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/logout/result")
                .invalidateHttpSession(true)
             .and()
                // 403 예외처리 핸들링
                .exceptionHandling().accessDeniedPage("/user/denied");
    }

    /*
    Spring Security에서 모든 인증은 AuthenticationManager를 통해 이루어지며 AuthenticationManager를
     생성하기 위해서는 AuthenticationManagerBuilder를 사용합니다.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth)throws  Exception{
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
