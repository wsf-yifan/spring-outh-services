package com.yifan.auth.config;

import com.yifan.auth.service.CustomOauth2RequestFactory;
import com.yifan.auth.service.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    // jwt 参数
    private String privateKey = "private key";
    private String publicKey = "public key";

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Bean
    public OAuth2RequestFactory requestFactory() {
        CustomOauth2RequestFactory requestFactory = new CustomOauth2RequestFactory(clientDetailsService);
        requestFactory.setCheckUserScopes(true);
        return requestFactory;
    }

//    @Bean
//    public JwtAccessTokenConverter tokenEnhancer() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(privateKey);
//        converter.setVerifierKey(publicKey);
//        return converter;
//    }
//    @Bean
//    public JwtTokenStore tokenStore() {
//        return new JwtTokenStore(tokenEnhancer());
//    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()"); // /oauth/check_token 可以访问
    }

    // 使用注意：redirectUris必须与client端一致，并且
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.inMemory()
                .withClient("djdemoapi")
                .secret(passwordEncoder.encode("123456"))
                //一般就加一种authorization_code
                .authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token",
                        "password", "implicit")
                .scopes("user_info")
                // 暂时不知道干什么呀
                //.resourceIds("oauth2-resource")
                .autoApprove(true)
                // 必须进行redirectUris的配置，否则请求授权码时会报错：error="invalid_request",
                // error_description="At least one redirect_uri must be registered with the client."
                .redirectUris("http://localhost:8082/ui/login", "http://localhost:8782/ui/login",
                        "http://localhost:8083/ui2/login", "http://localhost:8082/login"
                        , "http://localhost:8081/auth/user/me"
                )
                // 使用刷新token
                .accessTokenValiditySeconds(60)
                .refreshTokenValiditySeconds(300)
        ; // 1 hour
    }

    // 暂时不知道用来干嘛 20200407
//    @Autowired
//    private UserApprovalHandler userApprovalHandler;

    // 使用jwt 切换store
//    @Autowired
//    private TokenStore tokenStore;

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

//    @Override
//    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        final Map<String, Object> additionalInfo = new HashMap<>();
////        Principal user =  authentication.getUserAuthentication().getPrincipal();
////        additionalInfo.put("username", user.getUsername());
////        additionalInfo.put("authorities", user.getAuthorities());
//        additionalInfo.put("username","wsf");
//        additionalInfo.put("authorities","roles");
//        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
//        return accessToken;
//    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    @Autowired
    CustomTokenEnhancer customerEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 不加curl -i -X POST -d "username=wsf&password=123&grant_type=password&client_id=djdemoapi&client_secret=123456" http://localhost:8081/auth/oauth/token
        // 报错：{"error":"unsupported_grant_type","error_description":"Unsupported grant type"}
        endpoints.authenticationManager(authenticationManager)
//                .userApprovalHandler(userApprovalHandler)
                // 存储token
//                .tokenStore(tokenStore)
                .tokenStore(jwtTokenStore())
                .accessTokenConverter(accessTokenConverter())
                //使用jwt token store
//                .tokenStore(tokenStore()).tokenEnhancer(tokenEnhancer())
                // 不使用此将无法刷新token
                .userDetailsService(userDetailsService);
        // 自定义token生成方式 token 中需要展示其他内容时使用
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customerEnhancer, accessTokenConverter()));
        endpoints.tokenEnhancer(tokenEnhancerChain);
    }
}