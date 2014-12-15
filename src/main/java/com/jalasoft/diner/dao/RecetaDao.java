package com.jalasoft.diner.dao;

import java.util.Set;

import com.jalasoft.diner.models.Receta;

public interface RecetaDao extends GenericDao<Receta, Integer>{

	Set<Receta> findByName(String name);

}
