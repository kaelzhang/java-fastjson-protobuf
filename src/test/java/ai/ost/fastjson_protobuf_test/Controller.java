package ai.ost.fastjson_protobuf_test;

import ai.ost.test_vo.Entity.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

  @RequestMapping(
    value = "/mixed-res",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public MixedRes mixedRes (@RequestBody NormalReq req) {
    MixedRes res = new MixedRes();
    String message = req.getName();

    res.setRes(
      RequestBodyRes.newBuilder()
      .setMessage(message)
      .build()
    );

    return res;
  }

  @RequestMapping(
    value = "/mixed-req",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public NormalRes mixedReq (@RequestBody MixedReq req) {
    NormalRes res = new NormalRes();
    res.setMessage("hello " + req.getReq().getName());
    return res;
  }

  @RequestMapping(
    value = "/mixed",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public MixedRes mixed (@RequestBody MixedReq req) {
    MixedRes res = new MixedRes();
    res.setRes(
      RequestBodyRes.newBuilder()
        .setMessage("hello " + req.getReq().getName())
        .build()
    );
    return res;
  }

  @RequestMapping(
    value = "/deep",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public DeepRes deep (@RequestBody DeepReq req) {
    return DeepRes.newBuilder()
      .setRes(
        RequestBodyRes.newBuilder()
        .setMessage(
          "hello " + req.getReq().getName()
        )
      )
      .build();
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

class MixedReq {
  private RequestBodyReq req;

  public RequestBodyReq getReq() {
    return req;
  }

  public void setReq(RequestBodyReq req) {
    this.req = req;
  }
}

class MixedRes {
  private RequestBodyRes res;

  public void setRes(RequestBodyRes res) {
    this.res = res;
  }

  public RequestBodyRes getRes() {
    return res;
  }
}