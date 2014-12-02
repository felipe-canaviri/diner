package com.jalasoft.diner.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jalasoft.diner.dao.IngredienteDao;
import com.jalasoft.diner.models.Ingrediente;

@Service( "ingredienteService" )
public class IngredienteServiceImpl implements IngredienteService {

	@Autowired
	private IngredienteDao ingredienteDao;
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(IngredienteServiceImpl.class);
	
	@Override
	@Transactional
	public Integer save(Ingrediente obj) {
		return ingredienteDao.save(obj);
	}

	@Override
	@Transactional
	public List<Ingrediente> findAll() {
		return ingredienteDao.findAll();
	}

	@Override
	@Transactional
	public Ingrediente findById(Integer id) {
		return ingredienteDao.get(id);
	}

	@Override
	@Transactional
	public Ingrediente findByName(String name) {
		return ingredienteDao.findByName(name);
	}
}
