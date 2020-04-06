/*
 * Author     : shaofan
 * Date       : 4/6/2020 10:20 AM
 * Description:
 *
 */

package com.yifan.oauth2server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.net.PasswordAuthentication;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    // 密码加密
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new StandardPasswordEncoder("mypasssecret");
    }

    /* 定义用户查询规则，支持内存、jdbc、ldap以及自定义
    *  内存： auth.inMemoryAuthentication()
                .withUser("wsf")
                .password("wsf")
                .authorities("ROLE_USER"); 多用户使用and()
    *
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//        auth.inMemoryAuthentication()
//                .withUser("wsf")
//                .password("wsf")
//                .authorities("ROLE_USER")
//                .and()
//                .withUser("test")
//                .password("test")
//                .authorities("ROLE_USER");
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/design","/orders")
                .hasRole("ROLE_USER") // 访问上面两个接口必须要有ROLE_USER的角色
                .antMatchers("/","/**").permitAll() // 其他路径全部允许
                .and()
                .formLogin()
                .loginPage("/login")    // 自定义登录界面,默认的用户名、密码为username、与password，需要自定义时：
                                        // .loginProcessingUrl("/authenticate")
                                        // .usernameParameter("user")
                                        // .passwordParameter("pwd")
               .defaultSuccessUrl("")     // 设置登录成功界面
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
        ;
    }
}
