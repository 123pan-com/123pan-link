import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

public class DirectLink {

    // 123云盘中设置的密钥
    private static final String PRIVATE_KEY = "289ds32418bxdba";
    // 123云盘用户UID
    private static final long UID = 29;
    // 防盗链过期时间间隔(秒)
    private static final long EXPIRED_TIME_SEC = 3 * 60;
    // 下载地址
    private static final String DOWNLOAD_ADDRESS = "http://vip.123pan.com/29/13/123.txt";

    private static String getSignedUrl(String path) {
        // 下载链接的过期时间戳（秒）
        long timestamp = new Date().getTime() / 1000 + EXPIRED_TIME_SEC;
        // 生成随机数（建议使用UUID，不能包含中划线（-））
        String randomUUID = UUID.randomUUID().toString().replaceAll("-", "");
        // 待签名字符串="URI-timestamp-rand-uid-PrivateKey" (注:URI是用户的请求对象相对地址，不包含参数)
        String unsignedStr = String.format("%s-%d-%s-%d-%s", path, timestamp, randomUUID, UID, PRIVATE_KEY);
        return DOWNLOAD_ADDRESS + "?auth_key=" + String.format("%d-%s-%d-", timestamp, randomUUID, UID) + md5sum(unsignedStr);
    }

    // MD5加密
    private static String md5sum(String unsignedStr) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] secretBytes = md5.digest(unsignedStr.getBytes());
            return bytesToHex(secretBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    // 二进制转为十六进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder md5str = new StringBuilder();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (byte aByte : bytes) {
            digital = aByte;

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toLowerCase();
    }

    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL(DOWNLOAD_ADDRESS);
        // 下载path
        String path = url.getPath();
        // 获得签名后的链接
        String signedUrl = getSignedUrl(path);
        System.out.println(signedUrl);
    }
}