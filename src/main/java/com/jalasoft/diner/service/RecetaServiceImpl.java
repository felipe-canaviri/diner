package com.jalasoft.diner.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jalasoft.diner.dao.RecetaDao;
import com.jalasoft.diner.models.Receta;

@Service( "recetaService" )
public class RecetaServiceImpl implements RecetaService {

	@Autowired
	private RecetaDao recetaDao; 
	
	@Override
	@Transactional
	public Integer save(Receta receta) {
		return recetaDao.save(receta);
	}

	@Override
	@Transactional
	public List<Receta> findAll() {
		return recetaDao.findAll();
	}

	@Override
	@Transactional
	public Receta findById(Integer id) {
		return recetaDao.get(id);
	}

}
