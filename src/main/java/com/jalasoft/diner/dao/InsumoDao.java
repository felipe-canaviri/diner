package com.jalasoft.diner.dao;

import com.jalasoft.diner.models.Insumo;

public interface InsumoDao extends GenericDao<Insumo, Integer> {

	Insumo findByName(String name);
}
