package com.jalasoft.diner.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "insumos", indexes = @javax.persistence.Index(name = "unique_name_index", columnList="nombre", unique = true))
public class Insumo implements Comparable<Insumo>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4711315716075433428L;
	private static final Map<Serializable, Integer> SAVED_HASHES = Collections
			.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	private volatile Integer hashCode;
	
	private Integer id = 0;
	private Set<Ingrediente> ingredientes;
	private String nombre;
	
	
	public Insumo() {
		
	}
	
	public Insumo(String nombre, String unidadDeMedida, Double precioUnitarioCompra) {
		super();
		this.nombre = nombre;
		this.unidadDeMedida = unidadDeMedida;
		this.precioUnitarioCompra = precioUnitarioCompra;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "insumoId")
	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		// If we've just been persisted and hashCode has been
		// returned then make sure other entities with this
		// ID return the already returned hash code
		if ((this.id == null || this.id == 0) && (id != null) && (this.hashCode != null)) {
			SAVED_HASHES.put(id, this.hashCode);
		}
		this.id = id;
	}
	
	@Basic(optional = true)
	@Column(length = 255)
	public String getNombre() {
		return nombre;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "id.insumoId")
	@Basic(optional = true)
	@Column(nullable = true)
	public Set<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(final Set<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Basic(optional = true)
	@Column(length = 10)
	public String getUnidadDeMedida() {
		return unidadDeMedida;
	}

	public void setUnidadDeMedida(String unidadDeMedida) {
		this.unidadDeMedida = unidadDeMedida;
	}

	public Double getPrecioUnitarioCompra() {
		return precioUnitarioCompra;
	}

	public void setPrecioUnitarioCompra(Double precioUnitarioCompra) {
		this.precioUnitarioCompra = precioUnitarioCompra;
	}

	private String unidadDeMedida;
	
	private Double precioUnitarioCompra;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime
				* result
				+ ((precioUnitarioCompra == null) ? 0 : precioUnitarioCompra
						.hashCode());
		result = prime * result
				+ ((unidadDeMedida == null) ? 0 : unidadDeMedida.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Insumo other = (Insumo) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (precioUnitarioCompra == null) {
			if (other.precioUnitarioCompra != null)
				return false;
		} else if (!precioUnitarioCompra.equals(other.precioUnitarioCompra))
			return false;
		if (unidadDeMedida == null) {
			if (other.unidadDeMedida != null)
				return false;
		} else if (!unidadDeMedida.equals(other.unidadDeMedida))
			return false;
		return true;
	}

	@Override
	public int compareTo(Insumo arg0) {
		if(arg0.getPrecioUnitarioCompra() < this.getPrecioUnitarioCompra()) {
			return 1;
		} else {
			if (arg0.getPrecioUnitarioCompra() > this.getPrecioUnitarioCompra()) {
				return -1;
			}
		}
		
		return 0;
	}
	
	public String toString(){
		return nombre;
	}
}
