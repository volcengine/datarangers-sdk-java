package com.datarangers.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class AuthUtils {

  /**
   * @param ak accessKey
   * @param sk secretKey
   * @param expirationSeconds 过期时间，单位秒
   * @param method 方法，GET, POST, PUT
   * @param uriPath 请求的path，非完整的url
   * @param params 请求参数
   * @param body 请求的json体
   */
  public static String sign(String ak, String sk, int expirationSeconds, String method,
      String uriPath, Map<String, String> params, String body) {
    String cm = canonicalMethod(method);
    String cu = canonicalUrl(uriPath);
    String cp = canonicalParam(params);
    String cb = canonicalBody(body);
    String text = cm + "\n" + cu + "\n" + cp + "\n" + cb;
    return doSign(ak, sk, expirationSeconds, text);
  }

  private static String canonicalMethod(String method) {
    return "HTTPMethod:" + method;
  }

  private static String canonicalUrl(String url) {
    return "CanonicalURI:" + url;
  }

  private static String canonicalParam(Map<String, String> params) {
    String res = "CanonicalQueryString:";
    if (params == null || params.isEmpty()) {
      return res;
    }
    for (String key : params.keySet()) {
      res += formatKeyValue(key, params.get(key)) + "&";
    }
    return res.substring(0, res.length() - 1);
  }

  private static String formatKeyValue(String key, String value) {
    return key + "=" + value;
  }

  private static String canonicalBody(String body) {
    String res = "CanonicalBody:";
    if (body == null) {
      return res;
    } else {
      return res + body;
    }
  }

  private static String doSign(String ak, String sk, int expiration, String text) {
    String signKeyInfo =
        "ak-v1/" + ak + "/" + (int) (System.currentTimeMillis() / 1000) + "/" + expiration;
    String signKey = sha256Hmac(signKeyInfo, sk);
    String signResult = sha256Hmac(text, signKey);
    return signKeyInfo + "/" + signResult;
  }


  private static String sha256Hmac(String message, String secret) {
    String hash = "";
    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
      sha256_HMAC.init(secret_key);
      byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
      hash = byteArrayToHexString(bytes);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }
    return hash;
  }

  private static String byteArrayToHexString(byte[] b) {
    StringBuilder hs = new StringBuilder();
    String stmp;
    for (int n = 0; b != null && n < b.length; n++) {
      stmp = Integer.toHexString(b[n] & 0XFF);
      if (stmp.length() == 1) {
        hs.append('0');
      }
      hs.append(stmp);
    }
    return hs.toString().toLowerCase();
  }
}
