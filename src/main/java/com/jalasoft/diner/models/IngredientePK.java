package com.jalasoft.diner.models;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class IngredientePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1663940078406799383L;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@Basic(optional = false)
	@JoinColumn(name="recetaId", nullable = false, insertable = false, updatable = false)
	private Receta recetaId;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@Basic(optional = false)
	@JoinColumn(name = "insumoId", nullable = false, insertable = false, updatable = false)
	private Insumo insumoId;
	
	
	public Receta getRecetaId() {
		return recetaId;
	}
	public void setRecetaId(Receta recetaId) {
		this.recetaId = recetaId;
	}
	public Insumo getInsumoId() {
		return insumoId;
	}
	public void setInsumoId(Insumo insumoId) {
		this.insumoId = insumoId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((insumoId == null) ? 0 : insumoId.hashCode());
		result = prime * result
				+ ((recetaId == null) ? 0 : recetaId.hashCode());
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
		IngredientePK other = (IngredientePK) obj;
		if (insumoId == null) {
			if (other.insumoId != null)
				return false;
		} else if (!insumoId.equals(other.insumoId))
			return false;
		if (recetaId == null) {
			if (other.recetaId != null)
				return false;
		} else if (!recetaId.equals(other.recetaId))
			return false;
		return true;
	}
	
	
}
