package net.alebu.zedgehomework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.alebu.zedgehomework.MockServer;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
//@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AbstractIT {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected MockServer mockServer;

    @Autowired
    protected ObjectMapper objectMapper;

    //
    // MOCK METHODS
    //

    protected void mockOkResponseWithJson(String json) {
        final MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
//        mockResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        mockResponse.addHeader(HttpHeaders.CONTENT_TYPE, "text/javascript;charset=utf-8");
        mockResponse.setBody(json);
        mockServer.enqueue(mockResponse);
    }


    //
    // UTILITY METHODS
    //

    protected String loadFileFromResources(String resourceName) throws IOException {
        ClassPathResource res = new ClassPathResource(resourceName);
        return new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
