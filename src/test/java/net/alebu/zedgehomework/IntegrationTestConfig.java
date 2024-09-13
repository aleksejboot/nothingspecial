package net.alebu.zedgehomework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@Configuration
@Profile("test")
public class IntegrationTestConfig {

    @Bean
    public RestClient itunesRestClient(
            RestClient.Builder builder,
            MockServer mockServer
    ) {
        builder.baseUrl(mockServer.baseUrl());
        return builder.build();
    }

}
