package com.jalasoft.diner.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ingredientes")
public class Ingrediente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8375627707753262114L;
	
	@Id
	private IngredientePK id;

	private Double cantidadUsada;

	
	public IngredientePK getId() {
		return id;
	}

	public void setId(IngredientePK id) {
		this.id = id;
	}

	@Column(nullable = false)
	public Double getCantidadUsada() {
		return cantidadUsada;
	}

	public void setCantidadUsada(Double cantidadUsada) {
		this.cantidadUsada = cantidadUsada;
	}

	public String toString() {
		return String.format("[Nombre: %s, CantidadUsada: %s%s]", 
				this.id.getInsumoId().getNombre(), this.cantidadUsada, this.id.getInsumoId().getUnidadDeMedida());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cantidadUsada == null) ? 0 : cantidadUsada.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (cantidadUsada == null) {
			if (other.cantidadUsada != null)
				return false;
		} else if (!cantidadUsada.equals(other.cantidadUsada))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
