package com.nelioalves.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.ItemPedido;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.domain.enums.EstadoPagamento;
import com.nelioalves.cursomc.repositories.ItemPedidoRepository;
import com.nelioalves.cursomc.repositories.PagamentoRepository;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.repositories.ProdutoRepository;
import com.nelioalves.cursomc.service.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	
	
	public Pedido find(Integer id) {
		
		Optional<Pedido> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id+" Tipo: "+Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		//nova data com o instate atual
		obj.setInstante(new Date());
		//pedido novo pagameto está pendente
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		//o pagamento tem que conhecer o pdeido dele
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			//preenche no pagto a data de vencimento que vai ser uma semana após o pedido
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		//salva o pedido no banco
		obj = repo.save(obj);
		//salva o pagamento no banco
		pagamentoRepository.save(obj.getPagamento());
		//salva os ItemPedido
		for(ItemPedido ip : obj.getItens()){
			//setando o valor 0.0 no campo desconto de ItemPedido
			ip.setDesconto(0.0);
			//o preço tem que pegar do produto para setar no preço de ItemPedido
			ip.setPreco(produtoRepository.findById(ip.getProduto().getId()).get().getPreco());
			//associa o ItemPedido com o pedido que está sendo inserido
			ip.setPedido(obj);
		}
		//salva os ItemPedido no banco
		itemPedidoRepository.saveAll(obj.getItens());
		return obj;
		
	}
	
	

}
