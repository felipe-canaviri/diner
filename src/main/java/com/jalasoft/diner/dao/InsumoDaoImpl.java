package com.jalasoft.diner.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jalasoft.diner.models.Insumo;

@Repository( "insumoDao" )
public class InsumoDaoImpl extends GenericHibernateDaoImpl<Insumo, Integer> implements InsumoDao  {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsumoDaoImpl.class);
	
	public InsumoDaoImpl() {
		super(Insumo.class);
	}
	
	public Insumo findByName(String name) {
		Criterion byName = Restrictions.eq("nombre", name);
		List<Insumo> insumos = findByCriteria(byName);
		
		if (insumos.size() == 0) {
			LOGGER.warn(String.format("Unable to find '%s' as insumo. Return null as default.", name));
			return null;
		}

		if (insumos.size() > 1) {
			LOGGER.warn(String.format("It was expected 1 row with the name '%s' but '%d' were found. Returning the first one.", name, insumos.size()));
		}
		
		return insumos.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Insumo> findByNameLength() {
		
		Criteria criteria = this.getSession().createCriteria(Insumo.class, "ii")
				.add(Restrictions.sqlRestriction("CHAR_LENGTH(nombre) <= 8"))
				.add(Restrictions.sqlRestriction("CHAR_LENGTH(nombre) >= 6"));
		
		return criteria.list();
	}
}
