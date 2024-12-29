package pl.selfcloud.security.web.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

//  @Autowired
//  private MessageProducer messageProducer;

  @GetMapping("/home")
  public String getHome(){
    return "Hello world!";
  }

  @Secured({"ROLE_USER","ROLE_ADMIN"})
  @GetMapping("/home/user")
  public String getHomeForUSer(){
    return "Hello world User!";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/home/restricted")
  public String getHomeRestricted(){
    return "Hello world!";
  }
}
