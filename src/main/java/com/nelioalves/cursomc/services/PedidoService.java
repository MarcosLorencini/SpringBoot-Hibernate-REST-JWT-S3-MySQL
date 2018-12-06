package com.nelioalves.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.ItemPedido;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.domain.enums.EstadoPagamento;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.ItemPedidoRepository;
import com.nelioalves.cursomc.repositories.PagamentoRepository;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.repositories.ProdutoRepository;
import com.nelioalves.cursomc.security.UserSS;
import com.nelioalves.cursomc.service.exception.AuthorizationException;
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
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EmailService emailService;
	
	
	
	public Pedido find(Integer id) {
		
		Optional<Pedido> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id+" Tipo: "+Pedido.class.getName()));
	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		//nova data com o instate atual
		obj.setInstante(new Date());
		//usa o id para buscar o cliente inteiro
		obj.setCliente(clienteRepository.findById(obj.getCliente().getId()).get());
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
			//seta como o produto do ItemPedido
			ip.setProduto(produtoRepository.findById(ip.getProduto().getId()).get());
			//pega o preço do item pedido
			ip.setPreco(ip.getProduto().getPreco());
			//o preço tem que pegar do produto para setar no preço de ItemPedido
			ip.setPreco(produtoRepository.findById(ip.getProduto().getId()).get().getPreco());
			
			//associa o ItemPedido com o pedido que está sendo inserido
			ip.setPedido(obj);
		}
		//salva os ItemPedido no banco
		itemPedidoRepository.saveAll(obj.getItens());
		//chama o envio de email
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
		
	}
	
		//paginação das pedidos
		//page, tamanhodapagina, direcao e camposparaordenar
		//busca paginada
		//pega o usuario logado e buscar os pedidos somente deste usuario
		public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
			UserSS user = UserService.authenticated();
			if(user == null) {
				throw new AuthorizationException("Acesso Negado");
			}
			
			PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
			//retorna os pedidos do cliente logado
			Cliente cliente = clienteRepository.findById(user.getId()).get();
			//busca o cliente paginado
			return repo.findByCliente(cliente, pageRequest);
		}
	
	

}
