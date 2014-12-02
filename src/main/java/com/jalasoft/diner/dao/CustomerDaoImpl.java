package com.jalasoft.diner.dao;

import org.springframework.stereotype.Repository;

import com.jalasoft.diner.models.Customer;

@Repository( "customerDao" )
public class CustomerDaoImpl extends GenericHibernateDaoImpl<Customer, Long> implements CustomerDao {

	public CustomerDaoImpl() {
		super(Customer.class);
	}

}
