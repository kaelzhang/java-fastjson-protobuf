package ai.ost.fastjson_protobuf;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

  @RequestMapping("/simple")
  public String simple() {
    return "simple";
  }
}