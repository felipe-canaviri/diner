package com.jalasoft.diner.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jalasoft.diner.models.Ingrediente;

@Repository( "ingredienteDao" )
public class IngredienteDaoImpl extends GenericHibernateDaoImpl<Ingrediente, Integer> implements IngredienteDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(IngredienteDaoImpl.class);
	
	public IngredienteDaoImpl() {
		super(Ingrediente.class);
	}
	
	public Ingrediente findByName(String name) {
		Criterion byName = Restrictions.ilike("nombre", name + "%");
		List<Ingrediente> ingredientes = findByCriteria(byName);
		
		if (ingredientes.size() == 0) {
			return null;
		}

		if (ingredientes.size() > 1) {
			LOGGER.warn(String.format("It was expected 1 row with the name '%s' but '%d' were found. Returning the first one.", name, ingredientes.size()));
		}
		
		return ingredientes.get(0);
	}
}
