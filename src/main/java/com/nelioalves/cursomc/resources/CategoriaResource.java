package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = service.find(id);
		//caso retorne null lanca uma exception que é interceptada pelo ResourceExceptionHandler
		return ResponseEntity.ok().body(obj); 
	}
	
	//metodo que recebe uma categoria no formato json e insere a categoria no banco de dados
	//retorno com sucesso retorna vazio
	//quando ocorre a inserção no banco por padrão o http retorna 201, tbm tem que retornar a uri do novo recurso criado(http://localhost:8080/categorias/) 
	//@RequestBody converte o json no objeto Categotira
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
		Categoria obj = service.fromDto(objDto);
		//insere no banco e retorna o id do banco
		obj = service.insert(obj);
		//devolve http://localhost:8080/categorias/ e acrecenta o id
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	//recebe o value="/{id} da categoria para ser atualizada
	//recebe o obj para ser atualizado
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
		Categoria obj = service.fromDto(objDto);
		obj.setId(id);//garante que existe o id
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
		
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Categoria> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
		
	}
	
	//lista as categorias com DTO
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<Categoria> list = service.findAll();
		//percorre a lista e converte para outra lista
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		//caso retorne null lanca uma exception que é interceptada pelo ResourceExceptionHandler
		return ResponseEntity.ok().body(listDto); 
	}
	
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listDto); 
	}
	
	
	
	
	

}