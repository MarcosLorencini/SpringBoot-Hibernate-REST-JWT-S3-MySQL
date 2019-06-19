package com.nelioalves.cursomc.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;


//classe expoe o cabeçalho location na resposta.
//nas requis de inserção ex POST na aba Header do post gera cabeçaho  "location" da resposta com o endereço do novo recurso criado
//boas práticas quando inserir um recurso retornar o endereço do novo recurso esta classe expoe o endereço para o backend
//de forma implicita se não o angular não vai conseguir ler o cabeçalho

@Component
public class HeaderExposureFilter implements Filter {

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//o tipo ServletResponse ñ tem este método addHeader, que tem é o HttpServletResponse
		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("access-control-expose-headers", "location");
		//intercepta todas as requisicoes e expoe o header location na resposta
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}
