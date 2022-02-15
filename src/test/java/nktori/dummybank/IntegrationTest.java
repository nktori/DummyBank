package nktori.dummybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    public int port;

    @Autowired
    public TestRestTemplate restTemplate;

}
