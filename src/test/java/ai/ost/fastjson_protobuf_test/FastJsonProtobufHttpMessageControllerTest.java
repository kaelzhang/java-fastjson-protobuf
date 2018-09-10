package ai.ost.fastjson_protobuf_test;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration("src/test/java/ai/ost/fastjson_protobuf")
@ContextConfiguration(classes = TestApplication.class)
public class FastJsonProtobufHttpMessageControllerTest {

  @Autowired
  private WebApplicationContext webAppContext;
  private MockMvc mvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
  }

  @Test
  public void simple() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/simple").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("\"simple\"")));
  }

  @Test
  public void normalJson() throws Exception {
    mvc.perform(
      MockMvcRequestBuilders
        .post("/normal-json")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"world\"}".getBytes())
    )
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("{\"message\":\"hello world\"}")));
  }

  @Test
  public void simpleProto() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/simple-proto").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("{\"code\":200}")));
  }

  @Test
  public void requestBody() throws Exception {
    mvc.perform(
      MockMvcRequestBuilders
        .post("/request-body")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"world\"}".getBytes())
    )
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("{\"message\":\"hello world\"}")));
  }

  @Test
  public void mixedRes() throws Exception {
    mvc.perform(
      MockMvcRequestBuilders
        .post("/mixed-res")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"name\":\"world\"}".getBytes())
    )
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("{\"res\":{\"message\":\"world\"}}")));
  }

  @Test
  public void mixedReq() throws Exception {
    mvc.perform(
      MockMvcRequestBuilders
        .post("/mixed-req")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"req\":{\"name\":\"world\"}}".getBytes())
    )
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("{\"message\":\"hello world\"}")));
  }

  @Test
  public void mixedReqUnexpectedJson() throws Exception {
    mvc.perform(
      MockMvcRequestBuilders
        .post("/mixed-req")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"req\":\"wrong\"".getBytes())
    )
      .andExpect(status().isBadRequest());
      // .andExpect(content().string(equalTo("{\"message\":\"hello world\"}")));
  }

  @Test
  public void mixed() throws Exception {
    mvc.perform(
      MockMvcRequestBuilders
        .post("/mixed")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"req\":{\"name\":\"world\"}}".getBytes())
    )
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("{\"res\":{\"message\":\"hello world\"}}")));
  }

  @Test
  public void deep() throws Exception {
    mvc.perform(
      MockMvcRequestBuilders
        .post("/deep")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"req\":{\"name\":\"world\"}}".getBytes())
    )
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("{\"res\":{\"message\":\"hello world\"}}")));
  }

  @Test
  public void deepWithWrongRequestJson() throws Exception {
    mvc.perform(
      MockMvcRequestBuilders
        .post("/deep")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\"req\":{\"name\":\"world".getBytes())
    )
      .andExpect(status().isBadRequest());
  }
}
