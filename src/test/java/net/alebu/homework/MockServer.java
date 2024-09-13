package net.alebu.homework;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("test")
public class MockServer {

    private MockWebServer mockWebServer;

    @PostConstruct
    public void init() {
        mockWebServer = new MockWebServer();
    }

    public String baseUrl() {
        return this.mockWebServer.url("").toString();
    }

    public MockServer enqueue(MockResponse mockResponse) {
        this.mockWebServer.enqueue(mockResponse);
        return this;
    }

    public RecordedRequest takeRequest() throws InterruptedException {
        return this.mockWebServer.takeRequest();
    }

    @PreDestroy
    public void shutdown() throws IOException {
        this.mockWebServer.shutdown();
    }
}
