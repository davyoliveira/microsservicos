package br.com.alura.microservice.fornecedor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;


@ComponentScan ( "br.com.alura.microservice.fornecedor" )
@EntityScan ( "br.com.alura.microservice.fornecedor" )
@EnableJpaRepositories ( "br.com.alura.microservice.fornecedor" )
@Configuration
@SpringBootApplication
@EnableResourceServer
public class FornecedorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FornecedorApplication.class, args);
	}

}
