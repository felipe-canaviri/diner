package com.jalasoft.diner.service;

import java.util.List;

import com.jalasoft.diner.models.Insumo;

public interface InsumoService {

	Integer save(Insumo insumo);
	List<Insumo> findAll();
	Insumo findById(Integer id);
	Insumo findByName(String name);
	
	List<Insumo> findByNameLength();
}
