package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.service.exception.DataIntegrityException;
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
		//deve buscar a categoria no banco de dados para conseguir atualizar todas as informações da classe categoria, se atualizar somente
		//os campos do CategoriaDTO os outros campos na classe Categoria irão ficar vazio
		Categoria newObj = find(obj.getId());//busca o id no banco e caso não exista lanca uma exception
		//atualiza os dados que buscou no banco, novo objeto(newObj) com base no objeto que veio como argumento(obj)
		upDateDate(newObj, obj);
		return repo.save(newObj);
	}
	
	private void upDateDate(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
		
	}

	
	public void delete(Integer id) {
		find(id);//caso o id não exista dispara uma exception
		try {
			repo.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			//tenho que lancar uma execpeiton minha quando lancar uma exception DataIntegrityViolationException
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
			
		}
	}
	
	public List<Categoria> findAll(){
		return repo.findAll();
	}
	
	//paginação das categorias
	//page, tamanhodapagina, direcao e camposparaordenar
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	//instancia uma categoria a partir de um dto
	public Categoria fromDto(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}

}
