package com.example.chatclient.messageutil;

import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static org.junit.jupiter.api.Assertions.*;

class ChatClientTest {
    ChatClient c =new ChatClient();
    String msg = "hi";
    String password = "baeldung";
    String salt="12345678";
    String encryptedMsg;

    @Test
    void givenPassword_whenEncrypt_thenSuccess() {
        try {
            encryptedMsg = c.givenPassword_whenEncrypt_thenSuccess(msg,password,salt);
            c.givenPassword_whenDecrpt_thenSuccess(encryptedMsg,password,salt);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}