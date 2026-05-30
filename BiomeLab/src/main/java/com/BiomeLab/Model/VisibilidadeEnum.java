package com.BiomeLab.Model;

public enum VisibilidadeEnum {
	P("Público"),R("Restrito");
	
	private String visibilidade;
	
	private VisibilidadeEnum(String visibilidade) {
		this.setVisibilidade(visibilidade);
	}
	
	public String getVisibilidade() {
		return visibilidade;
	}

	private void setVisibilidade(String visibilidade) {
		this.visibilidade = visibilidade;
	}

}
