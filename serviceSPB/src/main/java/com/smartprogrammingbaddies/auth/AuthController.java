package com.smartprogrammingbaddies.auth;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains the AuthController class.
 */
@RestController
public class AuthController {

  @Autowired
  private ApiKeyRepository apiKeyRepository;

  /**
    * Generates a unique API key for a client.
    *
    * @return A {@code ResponseEntity} containing the generated API key with an
  HTTP 200 status code, or a HTTP 500 status code if API could not be generated.
    */
  @GetMapping("/generateApiKey")
  public ResponseEntity<?> generateApiKey() {
    try {
      String apiKey = UUID.randomUUID().toString();
      while (apiKeyRepository.existsByApiKey(apiKey)) {
        apiKey = UUID.randomUUID().toString();
      }
      apiKeyRepository.save(new ApiKey(apiKey));
      return ResponseEntity.ok(apiKey);
    } catch (Exception e) {
      String message = "Error generating API key: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
  }

  /**
    * Verifies a client's API key is valid.
    *
    * @param apiKey A {@code String} representing the client's API key.
    * @return A {@code ReponseEntity} containing a {@code ResponseEntity}
  with a message if the API key was successfully verified and a HTTP 200 status code,
  or a HTTP 403 status code if the API key was not found.
  */
  @GetMapping("/verifyApiKey")
  public ResponseEntity<?> verifyApiKey(@RequestParam("apiKey") String apiKey) {
    try {
      if (apiKeyRepository.existsByApiKey(apiKey)) {
        return ResponseEntity.ok("API key is valid");
      }

      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("API key is invalid");

    } catch (Exception e) {
      String message = "Error verifying API key: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
  }

  /**
   * Enrolls a client into the database.
   *
   * @param apiKey A {@code String} representing the client's API key.
   * @return A {@code boolean} indicating if the client was successfully enrolled.
   */
  public boolean enrollClient(String apiKey) {
    try {
      if (!apiKeyRepository.existsByApiKey(apiKey)) {
        apiKeyRepository.save(new ApiKey(apiKey));
        System.out.println("API key added successfully!");
      } else {
        System.out.println("API key already exists!");
      }
      return true;
    } catch (Exception e) {
      System.err.println("Failed to add API key.");
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Verifies a client's API key is valid.
   *
   * @param apiKey A {@code String} representing the client's API key.
   * @return A {@code boolean} indicating if the client's API key is valid.
   */
  public boolean verifyClient(String apiKey) {
    try {
      boolean exists = apiKeyRepository.existsByApiKey(apiKey);
      if (exists) {
        System.out.println("API key exists in the database.");
      } else {
        System.out.println("API key does not exist.");
      }
      return exists;
    } catch (Exception e) {
      System.err.println("Failed to verify API key.");
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Removes a client's API key from the database.
   *
   * @param apiKey A {@code String} representing the client's API key.
   * @return A {@code boolean} indicating if the client's API key was successfully removed.
   */
  public boolean removeClient(String apiKey) {
    try {
      Optional<ApiKey> apiKeyEntry = apiKeyRepository.findByApiKey(apiKey);
      if (apiKeyEntry.isPresent()) {
        apiKeyRepository.delete(apiKeyEntry.get());
        System.out.println("API key deleted successfully!");
        return true;
      } else {
        System.out.println("API key not found.");
        return false;
      }
    } catch (Exception e) {
      System.err.println("Failed to delete API key.");
      e.printStackTrace();
      return false;
    }
  }
}