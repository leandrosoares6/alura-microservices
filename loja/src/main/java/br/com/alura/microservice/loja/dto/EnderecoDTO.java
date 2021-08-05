package br.com.alura.microservice.loja.dto;

public class EnderecoDTO {

  private String rua;

  private int numero;

  public String getRua() {
    return rua;
  }

  public void setRua(String rua) {
    this.rua = rua;
  }

  public int getNumero() {
    return numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  @Override
	public String toString() {
		return rua + ", numero " + numero + " - " + estado;
	}

	private String estado;
}
