package net.alebu.homework.config;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.datasource")
@Configuration
@Getter
@Setter
@ToString
public class DatabaseProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
