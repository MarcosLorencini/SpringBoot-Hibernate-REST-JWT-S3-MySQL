package com.nelioalves.cursomc.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemPedido  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore//não vai ser serializado, nem o pedido nem o produto
	@EmbeddedId//atributo composto colocar @Embeddable na classe ItemPedidoPk
	private ItemPedidoPK id = new ItemPedidoPK();
	
	private Double desconto;
	private Integer quantidade;
	private Double preco;
	
	public ItemPedido() {}

	public ItemPedido(Pedido pedido, Produto produto, Double desconto, Integer quantidade, Double preco) {
		super();
		id.setPedido(pedido);
		id.setProduto(produto);
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.preco = preco;
	}
	
	//calcula o subtotal dos pedidos
	//começa com get para que ser reconhecido pelo json e ser serializado 
	public double getSubTotal() {
		return (preco - desconto) * quantidade;
		
	}
	
	//ter acesso ao Pedido e Produto fora da classe ItemPedido sem acessar primeiro a variável id e depois o pedido ou produto
	@JsonIgnore//está fazendo a referencia ciclica, tudo q comeca com get entende-se que deve serializar
	public Pedido getPedido() {
		return id.getPedido();
	}
	
	//intancia o ItemPedido e associa com o Pedido
	//o framework usa o set não usa o construtor
	public void setPedido(Pedido pedido) {
		id.setPedido(pedido);
	}
	
	//instacia o ItemPedido e associa com o Produto
	//o framework usa o set não usa o construtor
	public void setProduto(Produto produto) {
		id.setProduto(produto);
	}
	
	//@JsonIgnore//o produto tem que aparecer no postman
	public Produto getProduto() {
		return id.getProduto();
	}

	public ItemPedidoPK getId() {
		return id;
	}

	public void setId(ItemPedidoPK id) {
		this.id = id;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		StringBuilder builder = new StringBuilder();
		builder.append(getProduto().getNome());
		builder.append(", Qte: ");
		builder.append(getQuantidade());
		builder.append(", Preço unitário: ");
		builder.append(nf.format(getPreco()));
		builder.append(", Subtotal: ");
		builder.append(nf.format(getSubTotal()));
		builder.append("\n");
		return builder.toString();
	}
	
	
	
	
	
	
	
	

}
