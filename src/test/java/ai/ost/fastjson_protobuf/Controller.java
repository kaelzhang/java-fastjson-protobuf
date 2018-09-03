package ai.ost.fastjson_protobuf;

import ai.ost.test_vo.Entity.SimpleProtoRes;
import ai.ost.test_vo.Entity.RequestBodyRes;
import ai.ost.test_vo.Entity.RequestBodyReq;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
public class Controller {

  @RequestMapping("/simple")
  public String simple() {
    return "simple";
  }

  @RequestMapping("/simple-proto")
  public SimpleProtoRes simpleProto () {
    return SimpleProtoRes.newBuilder()
     .setCode(200)
     .build();
  }

  @RequestMapping(
    value = "/request-body",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public RequestBodyRes requestBody (@RequestBody RequestBodyReq req) {
    return RequestBodyRes.newBuilder()
      .setMessage("hello " + req.getName())
      .build();
  }
}