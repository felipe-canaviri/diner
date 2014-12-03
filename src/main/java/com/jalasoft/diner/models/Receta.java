package com.jalasoft.diner.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
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
@Table(name = "recetas")
public class Receta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1403426461386401033L;
	private static final Map<Serializable, Integer> SAVED_HASHES = Collections
			.synchronizedMap(new WeakHashMap<Serializable, Integer>());
	private volatile Integer hashCode;

	private Integer id = 0;
	private String nombre;

	private Set<Ingrediente> ingredientes = new HashSet<>();

	public Receta(String nombre, Set<Ingrediente> ingredientes) {
		
		if (ingredientes == null || ingredientes.size() <= 0) {
			throw new RuntimeException();
		}
		
		this.nombre = nombre;
		this.ingredientes = ingredientes;
		for (Ingrediente ingrediente : ingredientes) {
			ingrediente.getId().setRecetaId(this);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recetaId")
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

	@Basic(optional = false)
	@Column(length = 100)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	 @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "id.recetaId")
	 @Basic(optional = false)
	 @Column(nullable = false)
	 public Set<Ingrediente> getIngredientes() {
		 return ingredientes;
	 }
	 public void setIngredientes(Set<Ingrediente> ingredientes) {
		 this.ingredientes = ingredientes;
	 }
	 public void addIngrediente(Ingrediente ingrediente) {
		 ingrediente.getId().setRecetaId(this);
		 this.ingredientes.add(ingrediente);
	 }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Receta other = (Receta) obj;
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
		return true;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Ingrediente ing : ingredientes) {
			buffer.append(ing.toString());
			buffer.append(", ");
		}
		
		return nombre + " [" + buffer.toString() + "]";
	}
}
