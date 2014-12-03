package com.jalasoft.diner.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jalasoft.diner.dao.InsumoDao;
import com.jalasoft.diner.models.Insumo;

@Service( "insumoService" )
public class InsumoServiceImpl implements InsumoService {

	@Autowired
	private InsumoDao insumoDao;
	
	@Override
	@Transactional
	public Integer save(Insumo insumo) {
		return insumoDao.save(insumo);
	}

	@Override
	public List<Insumo> findAll() {
		return insumoDao.findAll();
	}

	@Override
	public Insumo findById(Integer id) {
		return insumoDao.get(id);
	}

	@Override
	@Transactional
	public Insumo findByName(String name) {
		return insumoDao.findByName(name);
	}
}
