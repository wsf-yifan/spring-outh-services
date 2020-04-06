package com.yifan.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    
    @Autowired    
    private BCryptPasswordEncoder passwordEncoder;
    
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
            .authorizedGrantTypes("authorization_code")
            .scopes("user_info")
            .autoApprove(true)
                // 必须进行redirectUris的配置，否则请求授权码时会报错：error="invalid_request",
                // error_description="At least one redirect_uri must be registered with the client."
            .redirectUris("http://localhost:8082/ui/login","http://localhost:8782/ui/login","http://localhost:8083/ui2/login","http://localhost:8082/login")
        // .accessTokenValiditySeconds(3600)
        ; // 1 hour
    }


}