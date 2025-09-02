package nl.ckarakoc.jellycash.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ckarakoc.jellycash.service.PotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PotController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PotControllerTests extends BaseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private PotService potService;

}
