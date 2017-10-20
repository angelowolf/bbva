package ar.com.aw.bbva.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class PaginaDTO<ENTITY> {

	private List<ENTITY>		contenidoPagina	= new ArrayList<>();
	private Integer				numeroPagina;
	private Integer				tamanioPagina;
	private Integer				cantidadTotal;
	
	public PaginaDTO() {
		super();
	}

	public PaginaDTO(List<ENTITY> contenidoPagina, Integer numeroPagina, Integer tamanioPagina, Integer cantidadTotal) {
		super();
		this.contenidoPagina = contenidoPagina;
		this.numeroPagina = numeroPagina;
		this.tamanioPagina = tamanioPagina;
		this.cantidadTotal = cantidadTotal;
	}

	public List<ENTITY> getContenidoPagina() {
		return contenidoPagina;
	}

	public void setContenidoPagina(List<ENTITY> contenidoPagina) {
		this.contenidoPagina = contenidoPagina;
	}

	public Integer getNumeroPagina() {
		return numeroPagina;
	}

	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	public Integer getTamanioPagina() {
		return tamanioPagina;
	}

	public void setTamanioPagina(Integer cantidadPagina) {
		this.tamanioPagina = cantidadPagina;
	}

	public Integer getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(Integer cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}
	
	public Integer getCantidadPaginas() {
		return (int) Math.ceil(cantidadTotal / tamanioPagina.doubleValue());
	}
	
	@Override
	public String toString() {
		return "PaginaDTO [contenidoPagina=" + contenidoPagina + ", numeroPagina=" + numeroPagina + ", tamanioPagina="
				+ tamanioPagina + ", cantidadTotal=" + cantidadTotal + "]";
	}

}
