package edu.brown.cs.student.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Class with SHA-512 Password Encryption utilities. Many thanks to [this link]
 * (https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha
 * -pbkdf2-bcrypt-examples/) for code and examples.
 */
public class SHAEncryption {
  /**
   * Default empty constructor.
   */
  private SHAEncryption() {
  }

  /**
   * Generates a salt using the SHA1 PRNG (Pseudo-Random Number Generator) algorithm. See
   * [javadocs](https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names
   * .html#securerandom-number-generation-algorithms) for more information on SHA1PRNG.
   *
   * @return a SHA1PRNG-generated salt.
   * @throws NoSuchAlgorithmException if the SHA1PRNG algorithm is not loaded
   */
  private static byte[] getSalt() throws NoSuchAlgorithmException {
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return salt;
  }

  /**
   * Generates a hashed and salted password using SHA-512 + a 16 byte SHA1PRNG-generated salt.
   *
   * @param rawPassword password to salt
   * @return the hashed and salted password
   * @throws NoSuchAlgorithmException if the SHA-512 algorithm is not loaded
   */
  private static String getSHA512SecurePassword(String rawPassword)
      throws NoSuchAlgorithmException {
    byte[] salt = getSalt();
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    md.update(salt);
    byte[] bytes = md.digest(rawPassword.getBytes());
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append((Integer.toString((b & 0xff) + 0x100, 16).substring(1)));
    }

    System.out.println(sb.toString());

    return sb.toString();
  }
}
