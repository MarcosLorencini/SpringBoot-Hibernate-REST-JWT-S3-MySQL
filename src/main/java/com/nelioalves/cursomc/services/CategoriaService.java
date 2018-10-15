package com.nelioalves.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.service.exception.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id+" Tipo: "+Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);//o metodo save identidica se o id vier vazio é um objeto novo o id no banco será null, se o id vier preenchido é um update. 
		return repo.save(obj);
	}
	
	//o metodo save serve para inserir e tbm atualizar o obj a única diferença é o metodo obj.setId(null);
	public Categoria update(Categoria obj) {
		find(obj.getId());//busca o id no banco e caso não exista lanca uma exception
		return repo.save(obj);
	}

}
