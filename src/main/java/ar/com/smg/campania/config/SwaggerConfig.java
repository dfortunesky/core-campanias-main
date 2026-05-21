package ar.com.smg.campania.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        final Contact contact = new Contact();
        contact.setEmail("sistemas@swissmedical.com.ar");
        contact.setName("Swiss Medical");
        contact.setUrl("https://www.swissmedical.com.ar");

        final Info info = new Info()
                .title("Java Springboot seed")
                .version("1.0")
                .contact(contact)
                .description("This API exposes some example methods");

        return new OpenAPI().info(info);
    }
}
