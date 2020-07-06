package com.gaosung.scdemo.auth.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 密钥生成器
 */
public class KeyGenerator {

    public static final String KEY_ALGORITHM = "RSA";
    private static final String SALT_KEY = "RSASaltKey";
    private static Map<Integer, String> keyMap = new HashMap<>();


    /**
     * 对称加密密钥
     * 默认AES加密
     * @param slatKey
     * @return
     * @throws Exception
     */
    public static Key getSlatKey(String slatKey) throws Exception {
        javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        kgen.init(128, random);
        Key key = kgen.generateKey();
        return key;
    }

    /**
     * 非对称加密 公钥
     * 根据slatKey获取公匙，传入的slatKey作为SecureRandom的随机种子
     * 若使用new SecureRandom()创建公匙，则需要记录下私匙，解密时使用
     *
     * 注： slatKey必须和私钥的一样
     */
    public static String getPublicKey(String slatKey) throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        keyPairGenerator.initialize(1024, random);//or 2048
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return encryptBASE64(keyPair.getPublic().getEncoded());
    }

    /**
     * 非对称加密 私钥
     * 根据slatKey获取私匙，传入的slatKey作为SecureRandom的随机种子
     *
     * 注： slatKey必须和公钥的一样
     */
    public static String getPrivateKey(String slatKey) throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        keyPairGenerator.initialize(1024, random);// or 2048
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return encryptBASE64(keyPair.getPrivate().getEncoded());
    }

    //解码返回byte
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    //编码返回字符串
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
//    public static String encrypt(String str, String publicKey) throws Exception{
//        //base64编码的公钥
//        byte[] decoded = new BASE64Decoder().decodeBuffer(publicKey);
//        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
//        //RSA加密
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
//        String outStr = new BASE64Encoder().encodeBuffer(cipher.doFinal(str.getBytes("UTF-8")));
//        return outStr;
//    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
//    public static String decrypt(String str, String privateKey) throws Exception{
//        //64位解码加密后的字符串
//        byte[] inputByte = new BASE64Decoder().decodeBuffer(str);
//        //base64编码的私钥
//        byte[] decoded = new BASE64Decoder().decodeBuffer(privateKey);
//        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));
//        //RSA解密
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, priKey);
//        String outStr = new String(cipher.doFinal(inputByte));
//        return outStr;
//    }


    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new BASE64Encoder().encodeBuffer(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = new BASE64Encoder().encodeBuffer(privateKey.getEncoded());
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception{
        //base64编码的公钥
        byte[] decoded = new BASE64Decoder().decodeBuffer(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = new BASE64Encoder().encodeBuffer(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = new BASE64Decoder().decodeBuffer(str);
        //base64编码的私钥
        byte[] decoded = new BASE64Decoder().decodeBuffer(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static void main(String[] args) throws Exception {
        try {
            //生成公钥和私钥
            genKeyPair();
            //加密字符串
            String message = "df723820";
            System.out.println("随机生成的公钥为:" + keyMap.get(0));
            System.out.println("随机生成的私钥为:" + keyMap.get(1));
            String messageEn = encrypt(message,keyMap.get(0));
            System.out.println(message + "\t加密后的字符串为:" + messageEn);
            String messageDe = decrypt(messageEn,keyMap.get(1));
            System.out.println("还原后的字符串为:" + messageDe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
