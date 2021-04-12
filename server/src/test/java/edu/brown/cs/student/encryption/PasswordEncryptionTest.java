package edu.brown.cs.student.encryption;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.*;

public class PasswordEncryptionTest {

  @Test
  public void testSHAEncryption() throws NoSuchAlgorithmException {
    String pass1 = "password";
    byte[] pass1Salt = PasswordEncryption.getSalt();
    String encryptedPass1 = PasswordEncryption.getSHA512SecurePassword(pass1, pass1Salt);
    assertEquals(encryptedPass1, PasswordEncryption.getSHA512SecurePassword("password", pass1Salt));

    String pass2 = "12345";
    byte[] pass2Salt = PasswordEncryption.getSalt();
    String encryptedPass2 = PasswordEncryption.getSHA512SecurePassword(pass2, pass2Salt);
    assertEquals(encryptedPass2, PasswordEncryption.getSHA512SecurePassword("12345", pass2Salt));

    String pass3 = "abcE12-FeP0zY-cdcl-ALB";
    byte[] pass3Salt = PasswordEncryption.getSalt();
    String encryptedPass3 = PasswordEncryption.getSHA512SecurePassword(pass3, pass3Salt);
    assertEquals(encryptedPass3, PasswordEncryption.getSHA512SecurePassword(pass3, pass3Salt));
  }

  @Test
  public void testPBKDF2Encryption() throws NoSuchAlgorithmException, InvalidKeySpecException {
    String pass1 = "password";
    String encryptedPass1 = PasswordEncryption.getPBKDF2SecurePassword(pass1);
    assertTrue(PasswordEncryption.validatePBKDF2Password("password", encryptedPass1));

    String pass2 = "12345";
    String encryptedPass2 = PasswordEncryption.getPBKDF2SecurePassword(pass2);
    assertTrue(PasswordEncryption.validatePBKDF2Password("12345", encryptedPass2));

    String pass3 = "abcE12-FeP0zY-cdcl-ALB";
    String encryptedPass3 = PasswordEncryption.getPBKDF2SecurePassword(pass3);
    assertTrue(PasswordEncryption.validatePBKDF2Password("abcE12-FeP0zY-cdcl-ALB", encryptedPass3));
  }
}