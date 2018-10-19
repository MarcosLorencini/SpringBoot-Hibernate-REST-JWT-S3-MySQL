package com.nelioalves.cursomc.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	//busca por email
	//usando o recurso do SpringDate: "padrão de nomes"
	//o retorno deve ser cliente e o método deve começar com findBy e em seguida o nome do campo da entidade Email
	//o SpringDate detecta que queremos realizar uma busca por email e implementa o método
	//o ClienteRepository possui um metodo que busca um email no banco de dados 
	
	@Transactional(readOnly=true)//não é envolvida em uma transaçao de banco de dados, fica mais rápida, elimina o lock do gerenciamento do bd 
	Cliente findByEmail(String email);

}
