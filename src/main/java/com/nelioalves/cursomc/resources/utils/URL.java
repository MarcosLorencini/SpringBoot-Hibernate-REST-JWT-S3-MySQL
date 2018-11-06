package com.nelioalves.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	//caso o usuario entre com o nome com espaço
	//converte a string que possua o nome em branco para uma string que possua o caractere basico
	//método para descodifiar um parametro
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		}catch(UnsupportedEncodingException e) {
			return "";
		}
	}
	
	
	//pega os ids(String) das categoria e transforma em inteiros e add na lista para consulta no banco
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		for(int i=0; i<vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		return list;
	}

}
