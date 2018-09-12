package ai.ost.protobuf_disabled_converter_test;

import ai.ost.fastjson_protobuf_test_common.BaseController;
import ai.ost.test_vo.Entity.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProtobufDisabledController extends BaseController {
  @RequestMapping("/simple-proto")
  public SimpleProtoRes simpleProto () {
    return SimpleProtoRes.newBuilder()
      .setCode(200)
      .build();
  }
}
