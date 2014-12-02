package com.jalasoft.diner.service;

import java.util.List;

import com.jalasoft.diner.models.Customer;

/**
 * Defines the business methods for the customer service
 *
 * @author shaines
 */
public interface CustomerService
{
    public Customer findById( long id );
    public List<Customer> findAll();
    public Long save( Customer customer );
    public void update( Customer customer );
    public void delete( Customer customer );
}
