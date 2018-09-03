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
    System.out.print(">>>>>>>>>>>>>>>:" + req.getName());
    return RequestBodyRes.newBuilder()
      .setMessage("hello " + req.getName())
      .build();
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

class NormalReq {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

class NormalRes {
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}