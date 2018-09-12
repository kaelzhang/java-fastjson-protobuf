package ai.ost.protobuf_disabled_converter_test;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
class TestApplication {
  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }
}

@RunWith(SpringRunner.class)
@WebAppConfiguration("src/test/java/ai/ost/protobuf_disabled_converter_test")
@ContextConfiguration(classes = TestApplication.class)
public class ProtobufDisabledControllerTest {
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
      // For now we roughly test as isInternalServerError
      //   because MockMvc could not support error messages.
      //   https://github.com/spring-projects/spring-boot/issues/5574#event-1833174620
      .andExpect(status().isInternalServerError());
  }
}
