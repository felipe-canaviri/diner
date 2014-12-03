package com.jalasoft.diner.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jalasoft.diner.dao.InsumoDao;
import com.jalasoft.diner.dao.RecetaDao;
import com.jalasoft.diner.models.Ingrediente;
import com.jalasoft.diner.models.IngredientePK;
import com.jalasoft.diner.models.Insumo;
import com.jalasoft.diner.models.Receta;

@Service( "recetaService" )
public class RecetaServiceImpl implements RecetaService {

	@Autowired
	private RecetaDao recetaDao;
	
	@Autowired
	private InsumoDao insumoDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RecetaServiceImpl.class);
	
	@Override
	public Integer save(Receta receta) {
		return recetaDao.save(receta);
	}
	
	@Override
	@Transactional
	public Integer save(String nombreReceta, Map<String, Double> insumoCantidades) {
		
		Set<Ingrediente> ingredientes = asIngredientes(insumoCantidades);
		
		Receta receta = new Receta(nombreReceta, ingredientes);
		
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
	
	private Set<Ingrediente> asIngredientes(Map<String, Double> insumoCantidades) {
		if (insumoCantidades == null || insumoCantidades.size() <= 0) {
			return new HashSet<Ingrediente>();
		}
		
		Set<Ingrediente> ingredientes = new HashSet<>();
		for(Map.Entry<String, Double> entry : insumoCantidades.entrySet()) {
			Insumo insumo = insumoDao.findByName(entry.getKey());
			if (insumo == null) {
				LOGGER.warn(String.format("Invalid '%s' as insumo. Ignore this item as ingrediente.", entry.getKey()));
				continue;
			}
			
			IngredientePK pk = new IngredientePK();
			pk.setInsumoId(insumo);
			Ingrediente ingrediente = new Ingrediente();
			ingrediente.setId(pk);
			ingrediente.setCantidadUsada(entry.getValue());
			
			ingredientes.add(ingrediente);
		}
		
		return ingredientes; 
	}
	
}
