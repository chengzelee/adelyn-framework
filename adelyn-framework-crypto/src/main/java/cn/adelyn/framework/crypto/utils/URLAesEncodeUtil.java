package cn.adelyn.framework.crypto.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLAesEncodeUtil {

    private static final String replaceStr = "asda19sdz19";

    public static String encode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    public static String decode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    public static String encryptEncode(String str) {
        // 处理 % ,防止被错误传输
        String cryptStr = AesUtil.aesEncryptStr(str);
        String cryptUrlStr = encode(cryptStr);
        String res = cryptUrlStr.replaceAll("%", replaceStr);
        return res;
    }

    public static String encryptDecode(String str) {
        str = str.replaceAll(replaceStr, "%");
        String encryptUrlstr = decode(str);
        String decryptUrlstr = AesUtil.aesDecryptStr(encryptUrlstr);
        return decryptUrlstr;
    }
}
