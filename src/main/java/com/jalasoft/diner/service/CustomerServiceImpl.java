package com.jalasoft.diner.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jalasoft.diner.dao.CustomerDao;
import com.jalasoft.diner.models.Customer;

/**
 * Implements the business methods for the customer service
 * @author shaines
 */
@Service( "customerService" )
public class CustomerServiceImpl implements CustomerService
{
    @Autowired
    private CustomerDao customerDao;

    @Override
    @Transactional
    public Customer findById( long id )
    {
        //return customerDao.findById( id );
        return customerDao.get(id);
    }

    @Override
    @Transactional
    public List<Customer> findAll()
    {
        return customerDao.findAll();
    }

    @Override
    @Transactional
    public Long save( Customer customer )
    {
        return customerDao.save( customer );
    }

    @Override
    @Transactional
    public void update( Customer customer )
    {
        customerDao.update( customer );
    }

    @Override
    @Transactional
    public void delete( Customer customer )
    {
        customerDao.delete( customer );
    }
   
}
