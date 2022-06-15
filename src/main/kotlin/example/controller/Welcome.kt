package example.controller;

import javax.inject.Named;

@Named
class Welcome {

  var firstName: String = ""
  var lastName: String = ""

  fun showGreeting(): String {
      return "Hello " + firstName + " " + lastName + "!";
    }
  }
