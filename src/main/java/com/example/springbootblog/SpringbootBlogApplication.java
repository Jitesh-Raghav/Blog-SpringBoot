package com.example.springbootblog;

import com.example.springbootblog.entity.Roles;
import com.example.springbootblog.repository.RoleRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
        info= @Info(
                title="Spring Boot Blog App REST APIs",
                description="Spring Boot Blog App REST APIs Documentation",
                version="v1.0",
                contact= @Contact(
                        name="Jitesh",
                        email="jkr18042001@gmail.com"
                ),
                license = @License(
                        name="Apache 2.0",
                        url="http://www.apache.org/licenses/"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description="Spring Boot Blog App Documentation",
                url="https://github.com/Jitesh-Raghav/Blog-SpringBoot"
        )
)
public class SpringbootBlogApplication implements CommandLineRunner {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringbootBlogApplication.class, args);
    }

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {

        Roles adminRole = new Roles();             //we doing this cause the role data we inserted manually, so who'll do this in aws, so here it will automatically get executed, It can also be done by creating a Data.sql file in resources...
        adminRole.setName("ROLE_ADMIN");
        roleRepository.save(adminRole);

        Roles userRole = new Roles();             //It can also be done by manually adding them in the cloud, and also by data.sql
        userRole.setName("ROLE_USER");
        roleRepository.save(userRole);
    }
}
