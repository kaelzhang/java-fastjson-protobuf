package ai.ost.fastjson_protobuf;

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
}
