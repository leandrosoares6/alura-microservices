package br.com.alura.microservice.loja.service;

import java.time.LocalDate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.client.TransportadorClient;
import br.com.alura.microservice.loja.dto.CompraDTO;
import br.com.alura.microservice.loja.dto.InfoEntregaDTO;
import br.com.alura.microservice.loja.dto.InfoFornecedorDTO;
import br.com.alura.microservice.loja.dto.InfoPedidoDTO;
import br.com.alura.microservice.loja.dto.VoucherDTO;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.repository.CompraRepository;

@Service
public class CompraService {

  private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);

  @Autowired
  private FornecedorClient fornecedorClient;

  @Autowired
  private TransportadorClient transportadorClient;

  @Autowired
  private CompraRepository compraRepository;

  @HystrixCommand(fallbackMethod = "realizaCompraFallback", threadPoolKey = "realizaCompraThreadPool")
  public Compra realizaCompra(CompraDTO compra) {
    String estado = compra.getEndereco().getEstado();

    LOG.info("buscando informações do fornecedor de {}", estado);
    InfoFornecedorDTO infoFornecedor = fornecedorClient.getInfoPorEstado(estado);

    LOG.info("realizando um pedido");
    InfoPedidoDTO infoPedido = fornecedorClient.realizaPedido(compra.getItens());

    LOG.info("reservando a entrega do pedido no transportador");
    InfoEntregaDTO entregaDTO = new InfoEntregaDTO();
    entregaDTO.setPedidoId(infoPedido.getId());
    entregaDTO.setDataParaEntrega(LocalDate.now().plusDays(infoPedido.getTempoDePreparo()));
    entregaDTO.setEnderecoOrigem(infoFornecedor.getEndereco());
    entregaDTO.setEnderecoDestino(compra.getEndereco().toString());
    VoucherDTO voucher = transportadorClient.reservaEntrega(entregaDTO);

    Compra compraSalva = new Compra();
    compraSalva.setPedidoId(infoPedido.getId());
    compraSalva.setTempoDePreparo(infoPedido.getTempoDePreparo());
    compraSalva.setEnderecoDeDestino(infoFornecedor.getEndereco());
    compraSalva.setDataParaEntrega(voucher.getPrevisaoParaEntrega());
    compraSalva.setVoucher(voucher.getNumero());

    compraRepository.save(compraSalva);
    
    return compraSalva;
  }

  public Compra realizaCompraFallback(CompraDTO compra) {
    Compra compraFallback = new Compra();
    compraFallback.setEnderecoDeDestino(compra.getEndereco().toString());
    return compraFallback;
  }

  @HystrixCommand(threadPoolKey = "detalhesDaCompraThreadPool")
  public Compra detalhesDaCompra(Long id) {
    return compraRepository.findById(id).orElse(new Compra());
  }

}
