package ar.com.aw.bbva.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FONDO")
public class Fondo {
	private Long id;
	private String especie;
	private Double cantidadDisponible;
	private Double precioMercado;
	private Date fecha;
	private Archivo archivo;
	private Date fechaDatos;

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence",
			parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "SEQ_BBVA_FON_ID") })
	@Column(name = "FONDO_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FON_ESPECIE", nullable = false, length = 50)
	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	@Column(name = "FON_CANT_DISP", precision = 10, scale = 6)
	public Double getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(Double cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	@Column(name = "FON_PRECIO_MERCADO", precision = 10, scale = 6)
	public Double getPrecioMercado() {
		return precioMercado;
	}

	public void setPrecioMercado(Double precioMercado) {
		this.precioMercado = precioMercado;
	}

	@Temporal(TemporalType.DATE)
    @Column(name = "FON_FECHA", length = 7)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARCHIVO_ID", nullable = false)
	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}
	
	@Temporal(TemporalType.DATE)
    @Column(name = "FON_FECHA_DATOS", length = 7)
	public Date getFechaDatos() {
		return fechaDatos;
	}

	public void setFechaDatos(Date fechaDatos) {
		this.fechaDatos = fechaDatos;
	}

	@Override
	public String toString() {
		return "Fondo{" + "id=" + id + ", especie=" + especie + ", cantidadDisponible=" + cantidadDisponible
				+ ", precioMercado=" + precioMercado + ", fecha=" + fecha + '}';
	}
}
