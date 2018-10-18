package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.service.exception.DataIntegrityException;
import com.nelioalves.cursomc.service.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		
		Optional<Cliente> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id+" Tipo: "+Cliente.class.getName()));
		
		
	}
	
	    //o metodo save serve para inserir e tbm atualizar o obj a única diferença é o metodo obj.setId(null);
		public Cliente update(Cliente obj) {
			//deve buscar o cliente no banco de dados para conseguir atualizar todas as informações da classe cliente, se atualizar somente
			//os campos do clienteDTO os outros campos na classe Cliente irão ficar vazio
			
			Cliente newObj = find(obj.getId());//busca o id no banco e caso não exista lanca uma exception
			//atualiza os dados que buscou no banco, novo objeto(newObj) com base no objeto que veio como argumento(obj)
			upDateDate(newObj, obj);
			return repo.save(newObj);
		}
		
		

		public void delete(Integer id) {
			find(id);//caso o id não exista dispara uma exception
			try {
				repo.deleteById(id);
			}catch(DataIntegrityViolationException e) {
				//tenho que lancar uma execpeiton minha quando lancar uma exception DataIntegrityViolationException
				throw new DataIntegrityException("Não é possivel excluir porque há endtidades relacionadas");
				
			}
		}
		
		public List<Cliente> findAll(){
			return repo.findAll();
		}
		
		//paginação dos clientes
		//page, tamanhodapagina, direcao e camposparaordenar
		public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
			PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
			return repo.findAll(pageRequest);
		}
		
		//instancia um cliente a partir de um dto
		public Cliente fromDto(ClienteDTO objDto) {
			return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
		}
		
		
		private void upDateDate(Cliente newObj, Cliente obj) {
			newObj.setNome(obj.getNome());
			newObj.setEmail(obj.getEmail());
			
		}

}
