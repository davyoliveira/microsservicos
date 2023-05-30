package br.com.alura.microservice.loja.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.alura.microservice.loja.dto.InfoEntregaDTO;
import br.com.alura.microservice.loja.dto.VoucherDTO;

//id da aplicacao q vc esta acessando
//essa interface realiza as chamadas ao microsservico de fornecedor como se fosse um repository
@FeignClient( "transportador" )
public interface TransportadorClient {

	@RequestMapping( path = "/entrega", method = RequestMethod.POST)
	public VoucherDTO reservaEntrega(@RequestBody InfoEntregaDTO entregaDTO);

}
