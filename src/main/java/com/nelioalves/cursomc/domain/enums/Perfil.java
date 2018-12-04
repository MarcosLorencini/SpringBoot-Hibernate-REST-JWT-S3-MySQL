package com.nelioalves.cursomc.domain.enums;

public enum Perfil {
	//ROLE_... o spring exige este tipo de nomeclatura
	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int cod;
	private String descricao;
	
	private Perfil(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}
	
	//dados o numero inteiro retorna o EstadoPagamento equivalente
	public static Perfil toEnum(Integer cod) {
		if(cod == null) {
			
		}
		
		for(Perfil x: Perfil.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: "+cod);
		
	}
	

}
