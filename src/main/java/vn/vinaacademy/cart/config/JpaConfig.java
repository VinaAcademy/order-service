package vn.vinaacademy.cart.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {
        "vn.vinaacademy.cart"
})
@EnableMongoRepositories(basePackages = {
        "vn.vinaacademy.cart"
})
@EntityScan(basePackages = {
        "vn.vinaacademy.cart"
})
public class JpaConfig {
}
