package com.nelioalves.cursomc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	
	//Restrição de conteúdo: cliente só recupera seus pedidos
	//Criar a consulta na camada de acesso a dados
	
	//busca pedidos por cliente
	@Transactional(readOnly=true)//não é envolvida em uma transaçao de banco de dados, fica mais rápida, elimina o lock do gerenciamento do bd 
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);

}
