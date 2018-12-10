package com.nelioalves.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelioalves.cursomc.domain.enums.Perfil;
import com.nelioalves.cursomc.domain.enums.TipoCliente;

@Entity
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	
	@Column(unique=true)
	private String email;
	private String cpfOuCnpj;
	
	//private TipoCliente tipo;
	//vamos armazenar o tipo cliente como codigo, o TipoCliente vai ser armazenado como inteiro
	private Integer tipo;
	
	@JsonIgnore // para não mostrar quando recuperar o cliente
	private String senha;
	
	//cliente tem varios enderecos
	
	//o cliente pode serializar os endereços, porém o endereco não pode serializar os clientes
	//responsável por buscar os objetos
	//cascade=CascadeType.ALL caso modifique o cliente vai ser refletido no endereco
	@OneToMany(mappedBy="cliente", cascade=CascadeType.ALL)// do outro lado foi mapeado pelo campo cliente
	private List<Endereco> endereco = new ArrayList<>();
	
	
	//cliente tem varios telefones diferentes
	@ElementCollection //o jpa mapeia o campo telefone como uma entidade fraca
	//tabela auxiliar para guardar os telefones
	@CollectionTable(name="TELEFONE")
	private Set<String> telefones = new HashSet<>();
	
	//quando recuperar o cliente obrigatoriamente recupera os perfis
	 @ElementCollection(fetch=FetchType.EAGER)
	 @CollectionTable(name="PERFIS")
	 private Set<Integer> perfis = new HashSet<>();
	
	
	@JsonIgnore//os pedidos do cliente não vão ser serializados
	@OneToMany(mappedBy="cliente")//o cliente tem varios pedidos
	private List<Pedido> pedidos = new ArrayList<>();
	
	//salvando a url da imagem no cliente
	private String imageUrl;
	
	
	//todo o usuario que for criado por padrão sera um cliente
	public Cliente() {
		//todo o usuario que for criado por padrão sera um cliente
		addPerfil(Perfil.CLIENTE);
	}
	
	//porém para o mundo extorno o dado vai continar sendo TipoCliente
	//contrutor serve para facilitar a criação do cliente
	public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipo, String senha) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpfOuCnpj = cpfOuCnpj;
		this.tipo = (tipo==null) ? null : tipo.getCod();
		this.senha = senha;
		addPerfil(Perfil.CLIENTE);//todo o usuario que for criado por padrão sera um cliente
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}
	//dado o número inteiro retorna o tipo do cliente equivalente
	public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}

	//armazena o numero inteiro
	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();
	}

	public List<Endereco> getEndereco() {
		return endereco;
	}

	public void setEndereco(List<Endereco> endereco) {
		this.endereco = endereco;
	}

	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}
	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	//recupera os perfil do cliente
	public Set<Perfil> getPerfis() {
		//converte o numero inteiro para o Enum Perfil
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	
	//add o código do perfil
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
	

}
