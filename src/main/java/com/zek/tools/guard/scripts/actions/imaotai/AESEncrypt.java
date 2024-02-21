package com.zek.tools.guard.scripts.actions.imaotai;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author chenp
 * @date 2024/2/21
 */
public class AESEncrypt {

    private byte[] key;
    private byte[] iv;
    private String coding;

    public AESEncrypt(String key, String iv) {
        this.key = key.getBytes();
        this.iv = iv.getBytes();
    }

    public String pkcs7padding(String text) {
        int bs = 16;
        int length = text.length();
        int bytes_length = text.getBytes().length;
        int padding_size = (bytes_length == length) ? bytes_length : length;
        int padding = bs - (padding_size % bs);
        StringBuilder padding_text = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            padding_text.append((char) padding);
        }
        this.coding = String.valueOf(padding);
        return text + padding_text.toString();
    }

    public String aes_encrypt(String content) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        String contentPadding = pkcs7padding(content);
        byte[] encryptBytes = cipher.doFinal(contentPadding.getBytes());
        return new String(Base64.getEncoder().encode(encryptBytes));
    }

    public String aes_decrypt(String content) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptBytes = cipher.doFinal(Base64.getDecoder().decode(content));
        String text = new String(decryptBytes);
        return text.replaceAll(this.coding + "$", "");
    }
}
