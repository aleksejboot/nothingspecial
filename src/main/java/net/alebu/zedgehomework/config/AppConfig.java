package net.alebu.zedgehomework.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

import javax.sql.DataSource;

@Configuration
public class AppConfig {
    @Bean
    @Profile("!test")
    public RestClient itunesRestClient(RestClient.Builder builder) {
        builder.baseUrl("https://itunes.apple.com");
        return builder.build();
    }

    @Bean
    public DataSource dataSource(DatabaseProperties databaseProperties) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(databaseProperties.getDriverClassName());
        dataSourceBuilder.url(databaseProperties.getUrl());
        dataSourceBuilder.username(databaseProperties.getUsername());
        dataSourceBuilder.password(databaseProperties.getPassword());

        return dataSourceBuilder.build();
    }
}
