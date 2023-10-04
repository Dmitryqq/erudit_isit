package kg.erudit.db.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Bean("mySQLDataSource")
    public DataSource serviceDataSource() {
        return serviceDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("mySQLDataSourceProperties")
    @ConfigurationProperties("spring.mysqldatasource")
    public DataSourceProperties serviceDataSourceProperties() {
        return new DataSourceProperties();
    }
}
