/*
 * Author     : shaofan
 * Date       : 4/6/2020 11:19 PM
 * Description:
 *
 */

package com.yifan.authclient.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;



public class Oauth2Interceptor implements HandlerInterceptor {

    public static final Logger LOGGER = LoggerFactory.getLogger(Oauth2Interceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String accessToken;
        String authorization = request.getHeader("Authorization");
//        if (StringUtils.isEmpty(authorization)) {
//            ResponseUtils.writeResponseData(response, fail(MyReponseStatus.AUTH_LACK));
//            return false;
//        }
//        try {
//            accessToken = new String(Base64.getDecoder().decode(authorization)).split(":")[1];
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//            ResponseUtils.writeResponseData(response, fail(MyReponseStatus.AUTH_LACK));
//            return false;
//        }
//
//        OAuth2AccessToken oauth2AccessToken = Oauth2Utils.checkTokenInOauthClient(accessToken);
//
//        if (oauth2AccessToken == null) {// 非法的Token值
//            ResponseUtils.writeResponseData(response, fail(MyReponseStatus.AUTH_LACK));
//            return false;
//        } else if (oauth2AccessToken.isExpired()) {// token失效
//            ResponseUtils.writeResponseData(response, fail(MyReponseStatus.AUTH_LACK));
//            return false;
//        }
//
//        Map<String, Object> additionalInformation = oauth2AccessToken.getAdditionalInformation();
//        int memberId = Integer.parseInt(additionalInformation.get("userId").toString());
//        request.setAttribute("memberId", memberId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    protected static String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

}
