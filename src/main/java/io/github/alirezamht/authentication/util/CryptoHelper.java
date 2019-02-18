package io.github.alirezamht.authentication.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.SecureRandom;

public class CryptoHelper {

    private static final String easy = "0123456789" + "ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx";
    private static RandomSession sessionGen;

    public static String encrypt(String strClearText) throws Exception{
        String strData="";

        byte[] bytesEncoded = Base64.encodeBase64(strClearText.getBytes());
        strData=new String(bytesEncoded);

        return strData;
    }

    public static String decrypt(String strEncrypted) throws Exception{
        String strData="";
        byte[] valueDecoded = Base64.decodeBase64(strEncrypted);
        strData=new String(valueDecoded);
        return strData;
    }

    public static String getRandomSession(){
        if(sessionGen==null){
            sessionGen=new RandomSession(23, new SecureRandom(), easy);
        }
        return sessionGen.nextString();
    }





}
