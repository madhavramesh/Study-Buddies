package edu.brown.cs.student.encryption;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Class with Password Encryption utilities. Many thanks to [this article]
 * (https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha
 * -pbkdf2-bcrypt-examples/) for code and examples.
 */
public class PasswordEncryption {
  /**
   * Default empty constructor.
   */
  private PasswordEncryption() {
  }

  /**
   * Generates a hashed and salted password of 512 length using PBKDF2, a 16 byte SHA1PRNG-generated
   * salt, and 1000 iterations.
   *
   * @param password the password to encrypt
   * @return a PBKDF2 encrypted password
   * @throws NoSuchAlgorithmException if the SHA1PRNG algorithm is not loaded
   * @throws InvalidKeySpecException  if the PBKDF2WithHmacSHA1 algorithm is not loaded
   */
  public static String getPBKDF2SecurePassword(String password)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    int iterations = 1000;
    char[] chars = password.toCharArray();
    byte[] salt = getSalt();

    PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hash = skf.generateSecret(spec).getEncoded();
    return iterations + ":" + toHex(salt) + ":" + toHex(hash);
  }

  /**
   * Validates a password against the PBKDF2-encrypted password.
   *
   * @param originalPass  the password to validate
   * @param encryptedPass the encrypted password
   * @return true only if the original and encrypted passwords match
   * @throws NoSuchAlgorithmException if the SHA1PRNG algorithm is not loaded
   * @throws InvalidKeySpecException  if the PBKDF2WithHmacSHA1 algorithm is not loaded
   */
  public static boolean validatePBKDF2Password(String originalPass, String encryptedPass)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    String[] parts = encryptedPass.split(":");
    int iterations = Integer.parseInt(parts[0]);
    byte[] salt = fromHex(parts[1]);
    byte[] hash = fromHex(parts[2]);

    PBEKeySpec spec = new PBEKeySpec(originalPass.toCharArray(), salt, iterations, hash.length * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] testHash = skf.generateSecret(spec).getEncoded();
    int diff = hash.length ^ testHash.length;
    for (int i = 0; i < hash.length && i < testHash.length; i++) {
      diff |= hash[i] ^ testHash[i];
    }
    return diff == 0;
  }

  /**
   * Generates a hashed and salted password using SHA-512 + a 16 byte SHA1PRNG-generated salt.
   *
   * @param rawPassword password to salt
   * @return the hashed and salted password
   * @throws NoSuchAlgorithmException if the SHA-512 algorithm is not loaded
   */
  public static String getSHA512SecurePassword(String rawPassword, byte[] salt)
      throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    md.update(salt);
    byte[] bytes = md.digest(rawPassword.getBytes());
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append((Integer.toString((b & 0xff) + 0x100, 16).substring(1)));
    }
    return sb.toString();
  }

  /**
   * Generates a salt using the SHA1 PRNG (Pseudo-Random Number Generator) algorithm. See
   * [javadocs](https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names
   * .html#securerandom-number-generation-algorithms) for more information on SHA1PRNG.
   *
   * @return a SHA1PRNG-generated salt.
   * @throws NoSuchAlgorithmException if the SHA1PRNG algorithm is not loaded
   */
  public static byte[] getSalt() throws NoSuchAlgorithmException {
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return salt;
  }

  /**
   * Converts a byte array (e.g. a salt) into a string, with bytes in hex.
   *
   * @param array the byte array to convert
   * @return a string with the byte array's contents in hex
   */
  private static String toHex(byte[] array) {
    BigInteger bi = new BigInteger(1, array);
    String hex = bi.toString(16);
    int paddingLength = (array.length * 2) - hex.length();
    if (paddingLength > 0) {
      return String.format("%0" + paddingLength + "d", 0) + hex;
    } else {
      return hex;
    }
  }

  /**
   * Converts a hex string into a byte array
   *
   * @param hex the hex string to convert
   * @return the byte array
   */
  private static byte[] fromHex(String hex) {
    byte[] bytes = new byte[hex.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    }
    return bytes;
  }
}
