package bg.softuni.computerStore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesControllers {

  @GetMapping("/pages/all")
  public String all() {
    return "all";
  }

  @GetMapping("/pages/admins")
  public String admins() {
    return "admins";
  }
}
