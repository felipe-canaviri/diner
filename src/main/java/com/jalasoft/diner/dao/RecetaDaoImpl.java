package com.jalasoft.diner.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.jalasoft.diner.models.Receta;

@Repository( "recetaDao" )
public class RecetaDaoImpl extends GenericHibernateDaoImpl<Receta, Integer> implements RecetaDao {

	public RecetaDaoImpl() {
		super(Receta.class);
	}
	
	@SuppressWarnings("unchecked")
	public Set<Receta> findByName(String name) {
		//String likeValue = String.format("%%", args)
		Criteria criteria = this.getSession().createCriteria(Receta.class, "r")
				.add(Restrictions.ilike("r.nombre", name, MatchMode.ANYWHERE));
		
		return new HashSet<Receta>(criteria.list());
	}
}
