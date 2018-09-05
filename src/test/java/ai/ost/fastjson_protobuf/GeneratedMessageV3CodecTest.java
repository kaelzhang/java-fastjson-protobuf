package ai.ost.fastjson_protobuf;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import ai.ost.test_vo.Entity.*;

import java.io.IOException;

public class GeneratedMessageV3CodecTest {
  private ParserConfig parserConfig = new ParserConfig();
  private SerializeConfig serializeConfig = new SerializeConfig();

  @Test
  public void jsonUnexpectedEOF () {
    try {
      JSON.parseObject("{\"req\":{\"name\":\"world", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertThat(e.getMessage(), CoreMatchers.containsString("unclosed string"));
      return;
    }

    Assert.fail("should fail");
  }

  @Test
  public void jsonUnexpectedString () {
    try {
      JSON.parseObject("{\"req\":\"boooom!\"}", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertEquals(e.getMessage(), "Expect message object but got: \"boooom!\"");
      return;
    }

    Assert.fail("should fail");
  }

  @Test
  public void jsonUnexpected () {
    try {
      JSON.parseObject("\"boooom!\"", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertEquals(e.getMessage(), "Expect message object but got: \"boooom!\"");
      return;
    }

    Assert.fail("should fail");
  }

  @Test
  public void jsonUnexpectedTrue () {
    try {
      JSON.parseObject("true", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertEquals(e.getMessage(), "Expect message object but got: true");
      return;
    }

    Assert.fail("should fail");
  }

  @Test
  public void jsonUnexpectedFalse () {
    try {
      JSON.parseObject("false", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertEquals(e.getMessage(), "Expect message object but got: false");
      return;
    }

    Assert.fail("should fail");
  }

  @Test
  public void jsonUnexpectedInt () {
    try {
      JSON.parseObject("1", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertEquals(e.getMessage(), "Expect message object but got: an integer");
      return;
    }

    Assert.fail("should fail");
  }

  @Test
  public void jsonUnexpectedFloat () {
    try {
      JSON.parseObject("1.1", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertEquals(e.getMessage(), "Expect message object but got: a float number");
      return;
    }

    Assert.fail("should fail");
  }

  @Test
  public void jsonUnexpectedArray () {
    try {
      JSON.parseObject("[1, 2]", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertEquals(e.getMessage(), "Expect message object but got: [1,2]");
      return;
    }

    Assert.fail("should fail");
  }

  @Test
  public void jsonUnexpectedDate () {
    try {
      JSON.parseObject(":", DeepReq.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    } catch (JSONException e) {
      Assert.assertEquals(e.getMessage(), "Expect message object");
      return;
    }

    Assert.fail("should fail");
  }
}
