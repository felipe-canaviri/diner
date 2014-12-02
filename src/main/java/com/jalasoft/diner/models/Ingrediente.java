package com.jalasoft.diner.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ingredientes", indexes = @javax.persistence.Index(name = "unique_name_index", columnList="nombre", unique = true))
public class Ingrediente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8375627707753262114L;
	private static final Map<Serializable, Integer> SAVED_HASHES = Collections
			.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	private volatile Integer hashCode;

	private Integer id = 0;
	private String nombre;
	private String unidadMedida;

	private Set<Receta> recetas = new HashSet<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ingredienteId")
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

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	@Basic(optional = true)
	@Column(length = 10)
	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(final String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	@ManyToMany(mappedBy = "ingredientes")
	public Set<Receta> getRecetas() {
		return recetas;
	}

	public void setRecetas(Set<Receta> recetas) {
		this.recetas = recetas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((unidadMedida == null) ? 0 : unidadMedida.hashCode());
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
		Ingrediente other = (Ingrediente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (unidadMedida == null) {
			if (other.unidadMedida != null)
				return false;
		} else if (!unidadMedida.equals(other.unidadMedida))
			return false;
		return true;
	}
	
	public String toString() {
		return String.format("[Id: %s, Nombre: %s, UnidadMedida: %s]", this.id, this.nombre, this.unidadMedida);
	}

}
