package kg.erudit.db.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Map;

@Data
@Configuration
@PropertySource(value = "classpath:mysql-sql.properties", encoding = "UTF-8")
@ConfigurationProperties
public class MySQLDatabaseProperties {
    Map<String,String> queries;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurerMySQL(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
