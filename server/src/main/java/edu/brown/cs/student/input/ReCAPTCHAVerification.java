package edu.brown.cs.student.input;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * ReCAPTCHA Verification utility class. Code gracefully yoinked from https://stackoverflow
 * .com/questions/47622506/how-to-validate-recaptcha-v2-java-servlet.
 */
public class ReCAPTCHAVerification {

  /**
   * Validates Google reCAPTCHA V2 or Invisible reCAPTCHA.
   *
   * @param secretKey Secret key (key given for communication between your
   * site and Google)
   * @param response reCAPTCHA response from client side.
   * (g-recaptcha-response)
   * @return true if validation successful, false otherwise.
   */
  public static boolean isCaptchaValid(String secretKey, String response) {
    try {
      String url = "https://www.google.com/recaptcha/api/siteverify",
          params = "secret=" + secretKey + "&response=" + response;
      System.out.println("RESPONSE: " + response);

      HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
      http.setDoOutput(true);
      http.setRequestMethod("POST");
      http.setRequestProperty("Content-Type",
          "application/x-www-form-urlencoded; charset=UTF-8");
      OutputStream out = http.getOutputStream();
      out.write(params.getBytes(StandardCharsets.UTF_8));
      out.flush();
      out.close();

      InputStream res = http.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(res, StandardCharsets.UTF_8));

      StringBuilder sb = new StringBuilder();
      int cp;
      while ((cp = rd.read()) != -1) {
        sb.append((char) cp);
      }
      JSONObject json = new JSONObject(sb.toString());
      res.close();

      return json.getBoolean("success");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
