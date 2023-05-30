package br.com.alura.microservice.loja.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Compra {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	
	private Long pedidoId;
	
	private Integer tempoDePreparo;
	
	private String endereçoDestino;
	
	private LocalDate dataParaEntrega;

	private Long voucher;

	@Enumerated(EnumType.STRING)
	private CompraState state;
	
	public CompraState getCompraState() {
		return this.state;
	}
	
	public LocalDate getDataParaEntrega() {
		return dataParaEntrega;
	}

	public Long getVoucher() {
		return voucher;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public Integer getTempoDePreparo() {
		return tempoDePreparo;
	}

	public void setTempoDePreparo(Integer tempoDePreparo) {
		this.tempoDePreparo = tempoDePreparo;
	}

	public String getEndereçoDestino() {
		return endereçoDestino;
	}

	public void setEndereçoDestino(String endereçoDestino) {
		this.endereçoDestino = endereçoDestino;
	}

	public void setDataParaEntrega(LocalDate previsaoParaEntrega) {
		this.dataParaEntrega = previsaoParaEntrega;
	}

	public void setVoucher(Long numero) {
		this.voucher = numero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setState(CompraState recebido) {
		this.state = recebido;
	}
}
