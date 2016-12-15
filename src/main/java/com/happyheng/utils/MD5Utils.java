package com.happyheng.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Created by happyheng on 16/11/27.
 */
public class MD5Utils {

    public static String getMD5(String text) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(text.getBytes());

            return String.valueOf(Hex.encodeHex(md5Bytes));
    }

}
