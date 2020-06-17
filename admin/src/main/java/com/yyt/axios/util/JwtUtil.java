package com.yyt.axios.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yyt.axios.entity.Header;
import com.yyt.axios.entity.PayLoad;
import com.yyt.axios.enums.AlgorithmEnum;
import com.yyt.axios.exception.AlgorithmNotRegisterException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {
    // 建议使用StandardCharset nio包下的
    private static final String ENCODEING = "UTF-8";
    public static final String BEARER = "bearer:";
    @Value("${com.yyt.keys.RSPrivateKey}")
    private String rs256PrivateKey;
    @Value("${com.yyt.keys.RSPublicKey}")
    private String rs256PublicKey;
    @Value("${com.yyt.keys.HSPrivateKey}")
    private String hs256PrivateKey;
    @Value("${com.yyt.jwt.type}")
    private String type;
    @Value("${com.yyt.jwt.algorithm}")
    private String algorithm;

    public Algorithm getAlgorithm() {
        return getAlgorithm(AlgorithmEnum.RS256);
    }

    public Algorithm getAlgorithm(@NotNull AlgorithmEnum algorithm) {
        if(algorithm.equals(AlgorithmEnum.HS256)) {
            return Algorithm.HMAC256(hs256PrivateKey);
        } else if(algorithm.equals(AlgorithmEnum.RS256)) {
            try {
                RSAPublicKey publicKey = RsaUtil.getPublicKey(rs256PublicKey);
                RSAPrivateKey privateKey = RsaUtil.getPrivateKey(rs256PrivateKey);
                return Algorithm.RSA256(publicKey, privateKey);
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
        }
        throw new AlgorithmNotRegisterException(algorithm.getName() + "未注册");
    }

    public Algorithm getAlgorithm(@NotNull String algoName) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        if(algoName.equals(AlgorithmEnum.HS256.getName())) {
            return Algorithm.HMAC256(hs256PrivateKey);
        } else if (algoName.equals(AlgorithmEnum.RS256.getName())) {
            RSAPublicKey publicKey = RsaUtil.getPublicKey(rs256PublicKey);
            RSAPrivateKey privateKey = RsaUtil.getPrivateKey(rs256PrivateKey);
            return Algorithm.RSA256(publicKey, privateKey);
        }
        throw new NoSuchAlgorithmException("未找到该算法:" + algoName);
    }

    public String encryptToken(Header header, PayLoad payLoad, Algorithm algorithm) throws JsonProcessingException {
        Map<String, Object> map = JsonUtil.toObject(JsonUtil.toJson(header), new HashMap<>());
        return BEARER + JWT.create().withHeader(map)
                .withIssuedAt(new Date()).withClaim(PayLoad.USERNAME, payLoad.getUsername())
                .sign(algorithm);
    }

    public Header getHeader() {
        return new Header(type, algorithm);
    }

    public PayLoad getPayLoad(String username) {
        return new PayLoad(username, new Date());
    }
    public String getRs256PrivateKey() {
        return rs256PrivateKey;
    }

    public void setRs256PrivateKey(String rs256PrivateKey) {
        this.rs256PrivateKey = rs256PrivateKey;
    }

    public String getRs256PublicKey() {
        return rs256PublicKey;
    }

    public void setRs256PublicKey(String rs256PublicKey) {
        this.rs256PublicKey = rs256PublicKey;
    }

    public String getHs256PrivateKey() {
        return hs256PrivateKey;
    }

    public void setHs256PrivateKey(String hs256PrivateKey) {
        this.hs256PrivateKey = hs256PrivateKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlgo() {
        return algorithm;
    }

    public void setAlgo(String algo) {
        this.algorithm = algo;
    }


}
