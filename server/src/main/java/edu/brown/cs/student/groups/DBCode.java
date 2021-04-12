package edu.brown.cs.student.groups;

/**
 * Database Codes.
 * KEY: {
 * 0: Success! (registration, login, delete, classes)
 * 1: User error (not found, not logged in)
 * 2: Email error (email taken, invalid email)
 * 3: Password error (invalid password, passwords don't match)
 * 4: Class formation/join error (owner create multiple classes, already joined, invalid class code)
 * 5: ReCAPTCHA error
 * 6: Class info error (person not in class, preferences not there, ...)
 * }
 */
public enum DBCode {
  REGISTRATION_SUCCESS(0, "User successfully registered!"),
  LOGIN_SUCCESS(0, "User successfully logged in!"),
  RETRIEVE_USER_SUCCESS(0, "Successfully retrieved user info!"),
  DELETE_SUCCESS(0, "User info successfully deleted!"),
  CLASS_CREATION_SUCCESS(0, "Successfully created class!"),
  CLASS_JOIN_SUCCESS(0, "Successfully joined class!"),
  RETRIEVE_PREFERENCES_SUCCESS(0, "Successfully retrieved preferences!"),
  USER_NOT_LOGGED_IN(1, "User not logged in!"),
  USER_NOT_FOUND(1, "User not found!"),
  EMAIL_TAKEN(2, "Email was taken!"),
  INVALID_EMAIL(2, "Invalid email format!"),
  INVALID_PASSWORD(3, "Invalid password!"),
  PASSWORD_MISMATCH(3, "Passwords do not match!"),
  CLASS_ALREADY_CREATED(4, "Class with that number already exists!"),
  CLASS_OWNER_DOES_NOT_EXIST(4, "Owner ID not created! Ensure the owner actually exists."),
  INVALID_CLASS_CODE(4, "Invalid class code!"),
  ALREADY_JOINED_CLASS(4, "Already joined class!"),
  RECAPTCHA_FAILED(5, "Confirm that you're not a robot!"),
  PERSON_NOT_IN_CLASS(6, "Person not in class!"),
  PREFERENCES_NOT_THERE(6, "Preferences not in database!");

  private final int code;
  private final String message;

  DBCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
