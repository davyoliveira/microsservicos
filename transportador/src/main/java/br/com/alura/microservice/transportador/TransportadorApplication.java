package br.com.alura.microservice.transportador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan ( "br.com.alura.microservice.transportador" )
@EntityScan ( "br.com.alura.microservice.transportador" )
@EnableJpaRepositories ( "br.com.alura.microservice.transportador" )
@SpringBootApplication
public class TransportadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransportadorApplication.class, args);
	}

}
