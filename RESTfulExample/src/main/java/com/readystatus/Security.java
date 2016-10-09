/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.readystatus;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Torjus
 */
public class Security {

    final static SecureRandom RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    public Security() {

    }
    
    public byte[] generateNewSalt() {
        byte[] salt = getNextSalt();
        return salt;        
    }
    
    public String generatePasswordNewUser(String password, byte[] salt) throws InvalidKeySpecException {
        
        byte[] hashedPassword = hash(password.toCharArray(), salt);
        return convertToHex(hashedPassword);
    }

    public byte[] getNextSalt() {
        byte[] bytes = new byte[20];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    /**
     * Returns a salted and hashed password using the provided hash.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with
     * zeros)
     *
     * @param password the password to be hashed
     * @param salt a 16 bytes salt, ideally obtained with the getNextSalt method
     *
     * @return the hashed password with a pinch of salt
     */
    public byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Returns true if the given password and salt match the hashed value, false
     * otherwise.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with
     * zeros)
     *
     * @param password the password to check
     * @param salt the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     *
     * @return true if the given password and salt match the hashed value, false
     * otherwise
     */
    public boolean isExpectedPassword(char[] password, byte[] salt, String expectedHash) throws InvalidKeySpecException {
        byte[] pwdHash = hash(password, salt);
        String pwdHashConverted = convertToHex(pwdHash);
        Arrays.fill(password, Character.MIN_VALUE);
        if (pwdHashConverted.length() != expectedHash.length()) {
            return false;
        }
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHashConverted == null ? expectedHash != null : !pwdHashConverted.equals(expectedHash)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a random password of a given length, using letters and digits.
     *
     * @param length the length of the password
     *
     * @return a random password
     */
    public static String generateRandomPassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int c = RANDOM.nextInt(62);
            if (c <= 9) {
                sb.append(String.valueOf(c));
            } else if (c < 36) {
                sb.append((char) ('a' + c - 10));
            } else {
                sb.append((char) ('A' + c - 36));
            }
        }
        return sb.toString();
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
