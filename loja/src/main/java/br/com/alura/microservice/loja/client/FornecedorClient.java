package br.com.alura.microservice.loja.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.alura.microservice.loja.dto.InfoFornecedorDTO;
import br.com.alura.microservice.loja.dto.InfoPedidoDTO;
import br.com.alura.microservice.loja.dto.ItemDaCompraDTO;

// id da aplicacao q vc esta acessando

// essa interface realiza as chamadas ao microsservico de fornecedor como se fosse um repository
@FeignClient( "fornecedor" )
public interface FornecedorClient {
	
	@RequestMapping( "/info/{estado}" )
	InfoFornecedorDTO getInfoPorEstado( @PathVariable ( name = "estado" , required = true ) String estado);

	@RequestMapping( method = RequestMethod.POST, value = "/pedido" )
	InfoPedidoDTO realizaPedido(List<ItemDaCompraDTO> itens);
}
