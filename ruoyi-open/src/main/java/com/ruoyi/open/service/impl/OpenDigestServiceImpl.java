package com.ruoyi.open.service.impl;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.ruoyi.integrator.domain.InAppAccess;
import com.ruoyi.integrator.service.IInAppAccessService;
import com.ruoyi.open.service.IOpenDigestService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * @description: HTTP认证之摘要认证
 * @author: zhangxiulin
 * @time: 2021/2/2 22:17
 */
@Service
public class OpenDigestServiceImpl implements IOpenDigestService {

    private static final Logger log = LoggerFactory.getLogger(OpenDigestServiceImpl.class);

    public static final String DIGEST = "Digest ";
    public static final String NONCE = "nonce";
    public static final String QOP = "qop";
    public static final String REALM = "realm";
    public static final String NC = "nc";
    public static final String CNONCE = "cnonce";
    public static final String RESPONSE = "response";
    public static final String URI = "uri";
    public static final String HEX_LOOKUP = "0123456789abcdef";
    public static final String USERNAME = "username";

    public static final String realm = "in_";
    public static final String qop = "auth";

    private static final Charset defaultChatSet;
    private static final MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new NullPointerException("MD5");
        }
        defaultChatSet = StandardCharsets.ISO_8859_1;
    }

    @Autowired
    private IInAppAccessService inAppAccessService;


    /**
     *  HTTP Digest验证
     * @param httpServletRequest
     * @param httpResponse
     * @param url
     * @param type
     * @return
     */
    @Override
    public int auth(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse) {
        String authorization = httpServletRequest.getHeader("Authorization");
        log.info("authorization:{}", authorization);
        if (authorization != null) {
            if (authorization.startsWith(DIGEST.trim())) {
                HashMap<String, String> authFields = splitAuthFields(authorization.substring(7));
                String newResponse = authFields.get(RESPONSE);
                String username = authFields.get(USERNAME);
                String A1 = calcDigest(username, authFields.get(REALM),
                        getPasswordOfAppAccess(username));//A1 = MD5("usarname:realm:password");
                String A2 = calcDigest(httpServletRequest.getMethod(), authFields.get(URI));//A2 = MD5("httpmethod:uri");
                String oriResponse = calcDigest(A1, authFields.get(NONCE), authFields.get(NC), authFields.get(CNONCE),
                        authFields.get(QOP), A2); //response = MD5("A1:nonce:nc:cnonce:qop:A2");
                if (oriResponse.equals(newResponse)) {
                    httpResponse.setStatus(HttpStatus.SC_OK);
                    log.info("Digest认证成功，username[" + username + "]");
                    return HttpStatus.SC_OK;
                } else {
                    log.info("Digest认证失败，username[" + username + "]");
                    httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
                    return HttpStatus.SC_UNAUTHORIZED;
                }
            } else {
                log.info("返回的非摘要认证数据！");
            }
        } else {
            httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
            httpResponse.setHeader("WWW-Authenticate", getAuthenticate());
            return HttpStatus.SC_UNAUTHORIZED;
        }
        return 0;
    }

    private String getAuthenticate(){
        return "Digest " + "realm=" + realm + ",nonce=" + getNonce() + ",qop=" + qop;
    }

    private String getNonce(){
        md5.reset();
        md5.update(("Shawn" + System.currentTimeMillis()).getBytes(defaultChatSet));
        return bytesToHexString(md5.digest());
    }

    private String calcDigest(String first, String ... args){
        StringBuilder stringBuilder = new StringBuilder(first);
        for (String str : args){
            stringBuilder.append(':').append(str);
        }
        md5.reset();
        md5.update(stringBuilder.toString().getBytes());
        return bytesToHexString(md5.digest());
    }

    public static HashMap<String, String> splitAuthFields(String authString) {
        final HashMap<String, String> fields = Maps.newHashMap();
        final CharMatcher trimmer = CharMatcher.anyOf("\"\t ");
        final Splitter commas = Splitter.on(',').trimResults().omitEmptyStrings();
        final Splitter equals = Splitter.on('=').trimResults(trimmer).limit(2);
        String[] valuePair;
        for (String keyPair : commas.split(authString)) {
            valuePair = Iterables.toArray(equals.split(keyPair), String.class);
            fields.put(valuePair[0], valuePair[1]);
        }
        return fields;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(HEX_LOOKUP.charAt((bytes[i] & 0xF0) >> 4));
            sb.append(HEX_LOOKUP.charAt((bytes[i] & 0x0F) >> 0));
        }
        return sb.toString();
    }

    private String getPasswordOfAppAccess(String appKey) {
        InAppAccess inAppAccess = inAppAccessService.selectInAppAccessByAppKey(appKey);
        if (inAppAccess != null) {
            return inAppAccess.getAppSecret();
        }
        return "";
    }

}
