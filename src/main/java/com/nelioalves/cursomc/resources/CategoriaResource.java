package com.nelioalves.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {
		
		Categoria obj = service.buscar(id);
		//caso retorne null lanca uma exception que é interceptada pelo ResourceExceptionHandler
		return ResponseEntity.ok().body(obj); 
	}
	
	//metodo que recebe uma categoria no formato json e insere a categoria no banco de dados
	//retorno com sucesso retorna vazio
	//quando ocorre a inserção no banco por padrão o http retorna 201, tbm tem que retornar a uri do novo recurso criado(http://localhost:8080/categorias/) 
	//@RequestBody converte o json no objeto Categotira
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Categoria obj){
		//insere no banco e retorna o id do banco
		obj = service.insert(obj);
		//devolve http://localhost:8080/categorias/ e acrecenta o id
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	

}