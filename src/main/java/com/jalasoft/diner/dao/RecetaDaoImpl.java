package com.jalasoft.diner.dao;

import org.springframework.stereotype.Repository;

import com.jalasoft.diner.models.Receta;

@Repository( "recetaDao" )
public class RecetaDaoImpl extends GenericHibernateDaoImpl<Receta, Integer> implements RecetaDao {

	public RecetaDaoImpl() {
		super(Receta.class);
	}
}
