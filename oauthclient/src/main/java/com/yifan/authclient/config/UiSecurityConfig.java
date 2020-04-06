/**
 * 
 */
package com.yifan.authclient.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author yifan
 * https://www.jianshu.com/p/18b6ec524d4d
 * // 注解开启Spring Security的功能
 * @EnableWebSecurity
 * // 开启自动配置
 * @EnableAutoConfiguration
 * // 启用了一个Oauth2 客户端配置
 * @EnableOAuth2Client
 */

@Configuration
@EnableOAuth2Sso
public class UiSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		http.antMatcher("/**")
			.authorizeRequests()
			.antMatchers("/","/login**")
			.permitAll()
			.anyRequest()
			.authenticated();
	}

}
