package com.nelioalves.cursomc.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	
	//padrao do spring procura por todos e ordena pelo campo
	//devolve todos os estado ordenados por nome
	@Transactional(readOnly=true)//não é envolvida em uma transaçao de banco de dados, fica mais rápida, elimina o lock do gerenciamento do bd 
	public List<Estado> findAllByOrderByNome();

}
