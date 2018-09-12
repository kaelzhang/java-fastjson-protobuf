package ai.ost.fastjson_protobuf_test_common;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController {
  @RequestMapping("/simple")
  public String simple() {
    return "simple";
  }

  @RequestMapping(
    value = "/normal-json",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public NormalRes normalJson (@RequestBody NormalReq req) {
    NormalRes res = new NormalRes();
    res.setMessage("hello " + req.getName());
    return res;
  }
}

