package com.jalasoft.diner.dao;

import java.util.List;

import com.jalasoft.diner.models.Insumo;

public interface InsumoDao extends GenericDao<Insumo, Integer> {

	Insumo findByName(String name);
	
	List<Insumo> findByNameLength();
}
