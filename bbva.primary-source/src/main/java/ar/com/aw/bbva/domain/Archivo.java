package ar.com.aw.bbva.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ARCHIVO")
public class Archivo {
	private Long id;
	private String nombreArchivo;
	private Set<Fondo> fondos = new HashSet<>(0);

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence",
			parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "SEQ_BBVA_ARC_ID") })
	@Column(name = "ARCHIVO_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ARC_NOMBRE", nullable = false, length = 50)
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "archivo")
	public Set<Fondo> getFondos() {
		return fondos;
	}

	public void setFondos(Set<Fondo> fondos) {
		this.fondos = fondos;
	}

	public void agregarFondo(Fondo f) {
		this.getFondos().add(f);
	}

	@Override
	public String toString() {
		return "Archivo{" + "id=" + id + ", nombreArchivo=" + nombreArchivo + ", fondos=" + fondos + '}';
	}
}
