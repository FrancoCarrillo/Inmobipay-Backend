package com.coderly.inmobipay.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "InmobiPay API", version = "1.0.", description = "API documentation for endpoint in InmobiPay")
)
public class OpenApiConfig {

}
