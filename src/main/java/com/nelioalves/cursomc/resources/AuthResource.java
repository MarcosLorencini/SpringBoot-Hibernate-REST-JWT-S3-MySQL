package com.nelioalves.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.dto.EmailDTO;
import com.nelioalves.cursomc.security.JWTUtil;
import com.nelioalves.cursomc.security.UserSS;
import com.nelioalves.cursomc.services.AuthService;
import com.nelioalves.cursomc.services.UserService;

//sistemas que possuem autenticacao baseado em token, possuem "refreshToken"
//quando o tokne está perto de expirar a app acessa este refreshToken e pega um token novinho
//porém para pegar um novo token o token atua ainda tem que estar válido
//o usuário pode usar a aplicação por muitos dias sem logar
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;
	
	//tem que estar logado para acessar este endpoint
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();//tem que estar logado
		String token = jwtUtil.generateToken(user.getUsername());//pega o novo token
		response.addHeader("Authorization", "Bearer " + token);//add na resp da requisicao
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();//envia para requsicao
	}
	
	//end point esqueci minha senha e envia uma nova senha para o cliente
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDto) {
		service.sendNewPassword(objDto.getEmail());
		return ResponseEntity.noContent().build();//envia para requsicao
	}

}
