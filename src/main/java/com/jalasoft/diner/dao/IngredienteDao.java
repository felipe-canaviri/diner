package com.jalasoft.diner.dao;

import com.jalasoft.diner.models.Ingrediente;

public interface IngredienteDao extends GenericDao<Ingrediente, Integer>{

	Ingrediente findByName(String name);
}
