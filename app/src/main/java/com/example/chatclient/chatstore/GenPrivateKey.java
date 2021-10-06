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

// read files
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

    //check private public keys exists or not
    public static void genKeyPairIfNotExist1(Context context) throws IOException, NoSuchAlgorithmException {
        String prvtkeyFile = "prvtKey1.txt";
        String pbKeyFile = "pbKey1.txt";

        String[] pvtPbKey = new String[2];

        try {
            String privateKey = read(context,prvtkeyFile);
            String publicKey = read(context, pbKeyFile);

            ChatStore.setPublicKey(genPbkey(removeSuffix(publicKey,"\n")));
            ChatStore.setPrivateKey(genPvtkey(removeSuffix(privateKey,"\n")));
                    System.out.println("genPvtkey(privateKey)"+genPvtkey(privateKey));
                    pvtPbKey[0] = privateKey;
                    pvtPbKey[1] = publicKey;


        }catch (FileNotFoundException e){
            String[] privatePublicKeyStrings = generateKeyPair();
            write(context, prvtkeyFile, privatePublicKeyStrings[0]);
            write(context, pbKeyFile, privatePublicKeyStrings[1]);

        }

    }

    // remove suffix
    public static String removeSuffix(final String s, final String suffix)
    {
        if (s != null && suffix != null && s.endsWith(suffix)) {
            return s.substring(0, s.length() - suffix.length());
        }
        return s;
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
    }



    //create new file
    public static void write( Context context,String filename,String content) throws IOException {

        File file = new File(context.getFilesDir(), filename);

        try {
            FileWriter writer = new FileWriter(file);

            writer.append(content);
            writer.flush();
            writer.close();
        } catch (Exception e) { }
    }


    //generate key pair
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

    //generate public key
    public static PublicKey genPbkey(String publicKeyBase64Encoded) {
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


//generate private key
    public static PrivateKey genPvtkey(String privateKeyBase64Encoded) {
        byte[] decodedBytes1 = Base64.getDecoder().decode(privateKeyBase64Encoded.trim());
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
