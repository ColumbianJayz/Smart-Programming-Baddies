package com.smartprogrammingbaddies;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class contains all the API routes for the system.
 */
@RestController
public class RouteController {
  /**
   * Redirects to the homepage.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping({ "/", "/index", "/home" })
  public String index() {
    return "Welcome to the donation and volunteer management system service. The best one in town!";
  }
}