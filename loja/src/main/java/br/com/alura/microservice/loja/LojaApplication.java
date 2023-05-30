package br.com.alura.microservice.loja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@ComponentScan ( "br.com.alura.microservice.loja" )
@EntityScan ( "br.com.alura.microservice.loja" )
@EnableJpaRepositories ( "br.com.alura.microservice.loja" )
@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker // corta a execução se passar de determinado tempo
@EnableResourceServer
public class LojaApplication {
	
	@Bean
	public RequestInterceptor getInterceptorDeAutenticacao() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate template) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				
				if( auth == null ) {
					return;
				}
				
				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
				details.getTokenValue();
				
				template.header("Authorization", "Bearer" + details.getTokenValue() );
			}
		};
	}

	@Bean
	@LoadBalanced // vai ajudar a resolver o nome //fornecedor com o eureka
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LojaApplication.class, args);
	}

}
