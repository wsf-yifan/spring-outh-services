package com.yifan.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    
    @Autowired    
    private BCryptPasswordEncoder passwordEncoder;

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) 
    throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
    }

    // 使用注意：redirectUris必须与client端一致，并且
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) 
    throws Exception {
        clients.inMemory()
            .withClient("djdemoapi")
            .secret(passwordEncoder.encode( "123456"))
                //一般就加一种authorization_code
            .authorizedGrantTypes("authorization_code","client_credentials", "refresh_token",
                    "password", "implicit")
            .scopes("user_info")
                // 暂时不知道干什么呀
                //.resourceIds("oauth2-resource")
            .autoApprove(true)
                // 必须进行redirectUris的配置，否则请求授权码时会报错：error="invalid_request",
                // error_description="At least one redirect_uri must be registered with the client."
            .redirectUris("http://localhost:8082/ui/login","http://localhost:8782/ui/login",
                    "http://localhost:8083/ui2/login","http://localhost:8082/login"
                    ,"http://localhost:8081/auth/user/me"
            )
            // 使用刷新token
                .accessTokenValiditySeconds(120)
            .refreshTokenValiditySeconds(60)
        ; // 1 hour
    }

//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager)
//                // 存储token
////                .tokenStore(tokenStore())
//               .userDetailsService(userDetailsService);
//
//    }
}