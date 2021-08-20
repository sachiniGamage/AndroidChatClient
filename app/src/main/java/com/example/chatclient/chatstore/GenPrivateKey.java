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
//        String prvtKeyString = "MIIEowIBAAKCAQBX6cuHVEt/ItXf//tUSkn2ZtBaDO0rL0aoifZu0CrUFk5RyKiB" +
//                "jmdbJD96EuPhDIkobDKyKhi3td1H0vn4iMyBkoPzrapajzQ4MG3x8LB+GBE1Sy5x" +
//                "dBU0XyX5oagzNAYyMWB+doPvqusnu8Xv5Hwu4BjXTlR2N7Wi/9RRtaAOuaJQJ+9l" +
//                "zmK14Nzkb+8oZAvUq0LEhz6/CRgFMXwBF9PPK281nJVjFRaMeNF857crHTvRnTnk" +
//                "+i//fR8l62SpOrS6tZq8X6EY16yF8uWw/sPKfZFKjRXLMxUIo4E/lExLatf6rCQy" +
//                "SMYmVKn3p881SyjGkv+ewlLAVpbgvaEIPCR3AgMBAAECggEALwvu33086cy6qCVV" +
//                "WsZmznY3Cfng8jtalNdACg1c3iMRxrKberm6lgvP7IQvb0BGJlvAxZfBoo6w3L8L" +
//                "BvYueyjtdHp7r/ry9x7zt4YW7WPz0ZFqSUzWDDDfIbhhSwWMS1AhU4wZDylortrO" +
//                "EpMz4eILaS2CBcHKcnB2L0QDY91TBkW4TJ8HrmC8Vz1139HqFFcVtADHVgXOQdOU" +
//                "N4PjDuRP6mTWF2GKn4Z5MQTkJXpyD4MoZloqnRUVPtQz8t/qsS0fA9Jv7g5xTC1e" +
//                "25Hq+8nCE6vSE6dBz2q342XiNvYAwJMDO3DIHaf6AgSpeLVAfuM5ApA2LyR87dJ+" +
//                "yaPoAQKBgQChE96IPVHqeCWf56w3sncr7bNFJ9Ja6qJa+uUlNpWCftKSyYdFqHBG" +
//                "vHRK2VUxIxq9A+AoX/fr1rHFs1p45iy0zKElJMLcpjh2+QwGxJSNl8/zGiIaMnHQ" +
//                "Onv19CUrS/OGtZIRPGKY+gQg7SP9aOeKtA1m0etBDo4Kr5DOpJaeCwKBgQCLuF5v" +
//                "btsnU2SAyH0UrXWtozkV0rt20QkK/8Ya6hl7OHfvd9gcXqz46g2wRxcKZL/83MQi" +
//                "gFGO977WCDhCIjV978ZTmZZRDqm5j3schcCWVNzQ5N8HR4TZbZiKuXcHj6PB7DLO" +
//                "Lvq0isuw6OO8VBtCITXF1pVA4bSbAeFI1z9SxQKBgQCYzVp674C35oX6rTFoHLqE" +
//                "R5eacO5SJOcjW4LozGEES/7CFL9oSyNHVxsSrhzs4hpJWFXatz9e0GoP29ZlfiTG" +
//                "1p+/tqHL4zsJUbQg73bk4zD3ssDLDTN+jxxs89AWxJQeJDyOcTQvXm1wNsW/bTbJ" +
//                "E4jrExQSwRXp+SZrgSMIhQKBgFjIvpGW4wIZVtsTC5CRLR86gjLg5yj4agOoRo2h" +
//                "xrFrT6NHB3f3nXjK2gnzEqwTG4CmFKhL3Ae6XHULYhx8OnudWuySw1cctzNn/nZA" +
//                "dAHTSUIgvayVgNDdeLpmYkHWv9uhqSMl7T++lvMC+nwZAf1CtzXRRqSlCCwbxLmR" +
//                "tfZ1AoGBAIWu2Oo4fyR3l4oadG3LDbpzsCLUva5MYPlngPirvY+QhMJoMkM71Gd2" +
//                "zizddFxXnEeVW2McyCPSWBvQq39qX+sgWmjb/1xnvn4+AzE9f/iZThMaESjSWQ2O" +
//                "Zo9L6W3VxYADPtr+DwziG1pt77uM0hK7RhqQ3n+Op16svJ0gnTAT";
//        String publicKeyString = "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQBX6cuHVEt/ItXf//tUSkn2" +
//                "ZtBaDO0rL0aoifZu0CrUFk5RyKiBjmdbJD96EuPhDIkobDKyKhi3td1H0vn4iMyB" +
//                "koPzrapajzQ4MG3x8LB+GBE1Sy5xdBU0XyX5oagzNAYyMWB+doPvqusnu8Xv5Hwu" +
//                "4BjXTlR2N7Wi/9RRtaAOuaJQJ+9lzmK14Nzkb+8oZAvUq0LEhz6/CRgFMXwBF9PP" +
//                "K281nJVjFRaMeNF857crHTvRnTnk+i//fR8l62SpOrS6tZq8X6EY16yF8uWw/sPK" +
//                "fZFKjRXLMxUIo4E/lExLatf6rCQySMYmVKn3p881SyjGkv+ewlLAVpbgvaEIPCR3" +
//                "AgMBAAE=";

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();

        String privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());

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
