package ar.com.aw.bbva.rest.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PaginaOrdenDTO implements Comparable<PaginaOrdenDTO> {

	private Integer	orden;
	private String	propiedad;
	private boolean	asc;

	public PaginaOrdenDTO(Integer orden, String propiedad, boolean asc) {
		super();
		this.orden = orden;
		this.propiedad = propiedad;
		this.asc = asc;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(String propertyPath) {
		this.propiedad = propertyPath;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	@Override
	public String toString() {
		return "PaginaOrdenDTO [orden=" + orden + ", propiedad=" + propiedad + ", asc=" + asc + "]";
	}

	@Override
	public int compareTo(PaginaOrdenDTO o) {
		return this.orden.compareTo(o.orden);
	}

	@SuppressWarnings("unchecked")
	public static Collection<PaginaOrdenDTO> buildPaginaOrdenCollectionBy(String commaSeparatedProperties) {
		if(commaSeparatedProperties == null || commaSeparatedProperties.isEmpty())
			return Collections.EMPTY_LIST;
		
		Collection<PaginaOrdenDTO> paginaOrdenDTOs = new ArrayList<PaginaOrdenDTO>();
		if(!commaSeparatedProperties.contains(",")){
			boolean asc = !commaSeparatedProperties.startsWith("-");
			paginaOrdenDTOs.add(new PaginaOrdenDTO(1, asc?commaSeparatedProperties:commaSeparatedProperties.substring(1), asc));
			return paginaOrdenDTOs;
		} 
		
		String[] split = commaSeparatedProperties.split(",");
		for (int i = 0; i < split.length; i++) {
			String property = split[i];
			boolean asc = !property.startsWith("-");
			paginaOrdenDTOs.add(new PaginaOrdenDTO(i+1, asc?property:property.substring(1), asc));
		}
		return paginaOrdenDTOs;
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<PaginaOrdenDTO> buildPaginaOrdenCollectionBy(Collection<String> orders) {
		if(orders == null || orders.isEmpty())
			return Collections.EMPTY_LIST;

		Collection<PaginaOrdenDTO> paginaOrdenDTOs = new ArrayList<PaginaOrdenDTO>();
		int ordenNro = 1;
		for (String property : orders) {
			boolean asc = !property.startsWith("-");
			paginaOrdenDTOs.add(new PaginaOrdenDTO(ordenNro, asc?property:property.substring(1), asc));
		}
		return paginaOrdenDTOs;
	}
}
