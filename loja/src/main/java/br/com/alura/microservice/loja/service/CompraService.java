package br.com.alura.microservice.loja.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.client.TransportadorClient;
import br.com.alura.microservice.loja.client.TransportadorClient;
import br.com.alura.microservice.loja.dto.CompraDTO;
import br.com.alura.microservice.loja.dto.InfoEntregaDTO;
import br.com.alura.microservice.loja.dto.InfoFornecedorDTO;
import br.com.alura.microservice.loja.dto.InfoPedidoDTO;
import br.com.alura.microservice.loja.dto.VoucherDTO;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.model.CompraState;
import br.com.alura.microservice.loja.repository.CompraRepository;

@Service
public class CompraService {
	
	private static final Logger log = LoggerFactory.getLogger(CompraService.class);
	
	@Autowired
	private FornecedorClient fornecedorClient;
	
	@Autowired
	private TransportadorClient transportadorClient;
	
	@Autowired
	private CompraRepository compraRepository;
	
	// hystrix monitora a execução e nao deixa passar de um determinado valor (default 1 segundo)
	// se passar de um segundo chama o fallbackmethod. Se começar a sempre passar de 1 
	// segundo, vai chmar o fallback direto
	// Bulkhead é threadPoolKey, onde cada chamada vai ter o seu pool de threads
	@HystrixCommand( fallbackMethod = "realizaCompraFallback", threadPoolKey = "realizaCompraThreadPool" )
	public Compra realizaCompra(CompraDTO compra) {

		Compra compraSalva = new Compra();
		compraSalva.setState( CompraState.RECEBIDO );
		compraSalva.setEndereçoDestino(compra.getEndereco().toString());
		compraRepository.save(compraSalva);
		
		compra.setCompraId(compraSalva.getId());
		
		log.info("Buscando dados do fornecedor de {}", compra.getEndereco().getEstado());
		InfoFornecedorDTO info = fornecedorClient.getInfoPorEstado( compra.getEndereco().getEstado() );
		log.info("Realizando um pedido");
		InfoPedidoDTO pedido = fornecedorClient.realizaPedido(compra.getItens());
		
		compraSalva.setPedidoId(pedido.getId());
		compraSalva.setTempoDePreparo(pedido.getTempoDePreparo());
		compraSalva.setState( CompraState.PEDIDO_REALIZADO );
		compraRepository.save(compraSalva);
		
		InfoEntregaDTO entregaDTO = new InfoEntregaDTO();
		entregaDTO.setPedidoId(pedido.getId());
		entregaDTO.setDataParaEntrega( LocalDate.now().plusDays(pedido.getTempoDePreparo()));
		entregaDTO.setEnderecoOrigem(info.getEndereco());
		entregaDTO.setEnderecoDestino(compra.getEndereco().toString());
		
		VoucherDTO voucher = transportadorClient.reservaEntrega( entregaDTO );
		compraSalva.setState( CompraState.RESERVA_ENTREGA_REALIZADA );
		compraSalva.setDataParaEntrega( voucher.getPrevisaoParaEntrega() );
		compraSalva.setVoucher( voucher.getNumero() );
		compraRepository.save(compraSalva);
		
		return compraSalva;
	}
	
	public Compra realizaCompraFallback(CompraDTO compra) {

		if( compra.getCompraId() != null ) {
			return compraRepository.findById(compra.getCompraId()).get();
		}
		
		Compra compraFallback = new Compra();
		compraFallback.setEndereçoDestino(compra.getEndereco().toString());
		
		return compraFallback;
	}

	// Bulkhead é threadPoolKey, onde cada chamada vai ter o seu pool de threads
	@HystrixCommand( threadPoolKey = "getByIdThreadPool" )
	public Compra getById(Long id) {
		// TODO Auto-generated method stub
		return compraRepository.findById(id).orElse(new Compra());
	}
	
	// Realizavam a chamada via rest template
	
//	@Autowired
//	private RestTemplate client;
//	
//	@Autowired
//	private DiscoveryClient eurekaClient;
//
//	public void realizaCompra(CompraDTO compra) {
//		
//		
//		// o  endereço fornecedor, vai ser carregado via eureka, com ajuda do loadbalance do RestTemplate
//		ResponseEntity<InfoFornecedorDTO> exchange = client.exchange("http://fornecedor/info/"+compra.getEndereco().getEstado(),
//				HttpMethod.GET, null, InfoFornecedorDTO.class);
//		
//		// printa na tela as instancias disponiveis de fornecedor. o Rest template ja faz um load balance (para cada chamada chama uma instancia diferente)
//		eurekaClient.getInstances("fornecedor").stream().forEach(fornecedor -> {
//			System.out.println("localhost: "+ fornecedor.getPort());
//		});
//		
//		System.out.println( exchange.getBody().getEndereco() );
//	}
	
}
