package com.jalasoft.diner.service;

import java.util.List;
import java.util.Map;

import com.jalasoft.diner.models.Receta;

public interface RecetaService {
	
	Integer save(Receta receta);
	List<Receta> findAll();
	Receta findById(Integer id);
	Integer save(String nombreReceta, Map<String, Double> ingredientes);
}
