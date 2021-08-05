package br.com.alura.microservice.loja.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Compra {

	@Id
  private Long pedidoId;

  private Integer tempoDePreparo;

  private String enderecoDeDestino;

	private LocalDate dataParaEntrega;

	public Long getVoucher() {
		return voucher;
	}

	public void setVoucher(Long voucher) {
		this.voucher = voucher;
	}

	private Long voucher;



	public LocalDate getDataParaEntrega() {
		return dataParaEntrega;
	}

	public void setDataParaEntrega(LocalDate dataParaEntrega) {
		this.dataParaEntrega = dataParaEntrega;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public String getEnderecoDeDestino() {
		return enderecoDeDestino;
	}

	public void setEnderecoDeDestino(String enderecoDeDestino) {
		this.enderecoDeDestino = enderecoDeDestino;
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
}
