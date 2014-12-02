package com.jalasoft.diner.service;

import java.util.List;

import com.jalasoft.diner.models.Ingrediente;

public interface IngredienteService {

	Integer save(Ingrediente obj);
	List<Ingrediente> findAll();
	Ingrediente findById(Integer id);
	Ingrediente findByName(String name);
}
