package com.jalasoft.diner.service;

import java.util.List;

import com.jalasoft.diner.models.Receta;

public interface RecetaService {
	
	Integer save(Receta receta);
	List<Receta> findAll();
	Receta findById(Integer id);

}
