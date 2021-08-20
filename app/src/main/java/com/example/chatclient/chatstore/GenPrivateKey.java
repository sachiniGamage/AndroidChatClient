package com.example.chatclient.chatstore;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class GenPrivateKey {
    static PublicKey publicKey;
    static PrivateKey privateKey;
    static KeyPairGenerator kpg;
    static KeyPair kp;
    static String arr[] ;
    private final static String CRYPTO_METHOD = "RSA";
    private final static int CRYPTO_BITS = 2048;


    public static String read(Context context,String filename) throws IOException {

        File fileEvents = new File(context.getFilesDir(), filename);
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(fileEvents));

        String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

            br.close();

        String result = text.toString();
        return result;
    }

    public static void genKeyPairIfNotExist1(Context context) throws IOException, NoSuchAlgorithmException {
        String prvtkeyFile = "prvtKey.txt";
        String pbKeyFile = "pbKey.txt";

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();

        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCntGa4euy8Trv8CiHjlYqkvdKh0yO/+ujKscwreuRpzu8siTT+FwPisSPgKb/eRTB2JMyROr5pp2Iqf/yM0xYrWMXvCGeigAEydNG00Lp5nGJrGsTa1UCJx9/Bulftk3VdPlxL2PdIuNEG95uacGKxS3T8BV2V+vDHNe03jxsp+8E0rIiBMbDlcX00Ta1kkAWDH7NyTOQ+j65vKP+PhUcIhd53oPbYtusA78c19BElQEYs5bizGSr0eGA4GC8OqsgZdwboStfLai3rF3i+lA9MFiWP1WzA8hMJH/dyX3T/nljcXndVt35HZbWJoUjfUBLTLG/8bWytpY12xo76xMFbAgMBAAECggEAQoEKvxuYJ7C9YVyPRdX2jg/sqFm3cplImaDaS1/HlqIGPM4IYr6zvsDqbimAOd+MMT20ld8KxZEoH5xzhVDsGLJyg7D8j/ddAipLQv4HBNTcPob2C6XHmxvVAtknp0Hzp89kAHwAzCjmU3H7lcn9iTtYH9zs2N4Tf5xF/uNULVhPwGOebR3cEPTW5oDHOtK5vWrLO0I8tE5G/+mcMRspbYVQfWeTFLCUYDTh4rkdr3ANDdUI5FfmbwoWYoyAaSJCHU7agq8p1cX677VW/jczl+WMrFk2aQLJQEfnRvg5ZZ807rVlalGrsg8XTtzzaOiyj1yOseSSbBAboyWaJ7xAoQKBgQDuE46MydTzT0xOOSptIz+pKmCMzqIbcbCWx+NgUaCyOiIZvJ2ukKUNlUAPrH5j1HIZvYMqYvfnF6SkIIeSfoddUjItKLRao4lskHRZBnLwhvbFIVVFNGFIzsRHzN/5jJaO8+WbxYINnmCMa1P7XZNbYw76W00QO2vom6n2zWtx0QKBgQC0VJJRP8vXLfCLeN7QR9XTuKdsyvSX28j3L1QUzuLaKlrYD0MReMNliNS5Q7+6SvyGO1XDV+nrF5RBTTDvkMCYosskTNg8E0hiLu25LAQ4os//R2fiu4etAuEumUJJ1bR6pLVgd3bB5gP0jDMPDqARNA53fJbEy1Nx5c6Jmu3/awKBgDqDDfbmJ/NAmwVioV2/WwcNhjs1Ebxi/b6hE+35QeBrzzO6Gprrq9UhyGPRxiJs9ToETLdfKYRfWDSKVieX91blO7XDmkfoh37qJd43aopY8U9TuOxQSlSllLH81VM0zVAG+3iEW0lTknEQc8yEnBHKz8SSKeeCYywBDg29QNnxAoGATvWY+6qLfrTG1sseBOTvY70+0gwr/TJnVJSMpG/SNOTG0kQcgdH2/MZlHpnjoZaj68KfUuFzs1SnvdAm1T70leDyQfrD08o9r4AM5z+2uXXQzV5expCGX+Ma5mSE+f9w/yKPqKWmglBM+w9IbBdjTyUUEI2lFpZQ+1Y8kVQdUfECgYB9mat/hV9sje470k2yzpRQD7fOspaX8yOQnqVT/B0s94KtAy2t54QjoqOuYiFqP46wvIFb3U36ftt5DweRuFSrGXxIBuir3K0UIB5v6vPerWAyp0Nx/tLx7+GKzPkhVdbn7mC2EgKr3AFzISYaFMOSg6Obuh1sN99FDtrKOfu9iA==";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp7RmuHrsvE67/Aoh45WKpL3SodMjv/royrHMK3rkac7vLIk0/hcD4rEj4Cm/3kUwdiTMkTq+aadiKn/8jNMWK1jF7whnooABMnTRtNC6eZxiaxrE2tVAicffwbpX7ZN1XT5cS9j3SLjRBvebmnBisUt0/AVdlfrwxzXtN48bKfvBNKyIgTGw5XF9NE2tZJAFgx+zckzkPo+ubyj/j4VHCIXed6D22LbrAO/HNfQRJUBGLOW4sxkq9HhgOBgvDqrIGXcG6ErXy2ot6xd4vpQPTBYlj9VswPITCR/3cl90/55Y3F53Vbd+R2W1iaFI31AS0yxv/G1sraWNdsaO+sTBWwIDAQAB";

//        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
//        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());

        System.out.println(privateKey);

        System.out.println("\n");
        System.out.println(publicKey);

        System.out.println("\n");

        try {
//            prvtKeyString = read(context,prvtkeyFile);
//            publicKeyString = read(context, pbKeyFile);
            ChatStore.setPublicKey(genPbkey(publicKey));
            ChatStore.setPrivateKey(genPvtkey(privateKey));
            System.out.println("genPvtkey(privateKey)"+genPvtkey(privateKey));
        }catch (Exception e){
            String[] privatePublicKeyStrings = generateKeyPair();
//            write(context, prvtkeyFile, privatePublicKeyStrings[0]);
//            write(context, pbKeyFile, privatePublicKeyStrings[1]);
        }
    }

    public static void genKeyPairIfNotExist(Context context) throws IOException {

       String prvtkeyFile = "prvtKey.txt";
       String pbKeyFile = "pbKey.txt";
       String prvtKeyString = "";
       String publicKeyString = "";
        try {
            prvtKeyString = read(context,prvtkeyFile);
            publicKeyString = read(context, pbKeyFile);
            ChatStore.setPublicKey(genPbkey(publicKeyString));
            ChatStore.setPrivateKey(genPvtkey(prvtKeyString));
            System.out.println(genPvtkey(prvtKeyString));
        }catch (IOException e){
            String[] privatePublicKeyStrings = generateKeyPair();
            write(context, prvtkeyFile, privatePublicKeyStrings[0]);
            write(context, pbKeyFile, privatePublicKeyStrings[1]);
        }


//        read(context,filename);
//        arr[0]  =   genPbkey().toString();
//        arr[1]  =   genPvtkey().toString();
//
//        if(filename!=null){
//            System.out.println("file");
//        }else {
//            System.out.println("no file");
//            generateKeyPair();
//            write(context, filename, arr);
//        }
    }



    public static void write( Context context,String filename,String content) throws IOException {

//        File f = new File("path/to/dir/or/file");
        File file = new File(context.getFilesDir(), filename);
//        if (!file.exists()) {
////            file.createNewFile();
//            file.mkdir();
//        }

        try {
//            File gpxfile = new File(filename, "sample.txt");
            FileWriter writer = new FileWriter(file);

            writer.append(content);
            writer.flush();
            writer.close();
        } catch (Exception e) { }
    }


//    ------------------


    public static String[] generateKeyPair() {
        String[] arr = new String[2];
        try{
            kpg = KeyPairGenerator.getInstance(CRYPTO_METHOD);
            kpg.initialize(CRYPTO_BITS);
            kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
            ChatStore.setPublicKey(publicKey);
            ChatStore.setPrivateKey(privateKey);
            arr[0] = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            arr[1] = Base64.getEncoder().encodeToString(publicKey.getEncoded());


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return arr;

    }



    public static PublicKey genPbkey() {
//        PublicKey pb = ChatStore.getPublicKey();
        byte[] decodedBytes1 = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes1);
        KeyFactory kf = null;
        PublicKey pb1 = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            pb1 = kf.generatePublic(spec);
            System.out.println(pb1);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return pb1;
    }

    public static PublicKey genPbkey(String publicKeyBase64Encoded) {
//        PublicKey pb = ChatStore.getPublicKey();
        byte[] decodedBytes1 = Base64.getDecoder().decode(publicKeyBase64Encoded);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes1);
        KeyFactory kf = null;
        PublicKey pb1 = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            pb1 = kf.generatePublic(spec);
            System.out.println(pb1);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return pb1;
    }



    public static PrivateKey genPvtkey() {
        byte[] decodedBytes1 = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        X509EncodedKeySpec spec1 = new X509EncodedKeySpec(decodedBytes1);
        PrivateKey pb1 = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pb1 = kf.generatePrivate(spec1);
            System.out.println(pb1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
return pb1;
    }



    public static PrivateKey genPvtkey(String privateKeyBase64Encoded) {
        byte[] decodedBytes1 = Base64.getDecoder().decode(privateKeyBase64Encoded);
        PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(decodedBytes1);
        PrivateKey pb1 = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pb1 = kf.generatePrivate(spec1);
            System.out.println(pb1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return pb1;
    }


}
