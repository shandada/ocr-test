package com.example.demo.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *  http://localhost:10010/doc.html
 * Create on 2020/10/21 9:30
 * @author Yang Shuolin
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class ApplicationConfiguration {
    @Bean
    public Docket customDocket() {
        Contact contact = new Contact("shan", "", "shanpengkun@wisdomopen.com");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("springboot-test")
                                .description("springboot-测试项目")
                                .version("v1")
                                .contact(contact)
                                .build()
                )
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.example.demo"))
                .build();
    }
}
