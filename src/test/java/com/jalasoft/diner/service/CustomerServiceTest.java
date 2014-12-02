package com.jalasoft.diner.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jalasoft.diner.models.Customer;

public class CustomerServiceTest {

	private CustomerService service;
	private ApplicationContext applicationContext;
	@Before
	public void setup(){
		applicationContext = new ClassPathXmlApplicationContext( "classpath:applicationTestContext.xml" );
		service = (CustomerService)applicationContext.getBean("customerService");
	}
	
	@After
	public void tearDown() {
		((ConfigurableApplicationContext)applicationContext).close();
	}
	
	@Test
	public void testFindAll() {

		List<Customer> customers = service.findAll();
		
		assertNotNull(customers);
		assertTrue(customers.size() > 0); 
	}
	
	@Test
	public void testSave() {

		Customer customer = new Customer("AA", "AAA", "aa@aa.com", "AAA");
		Long id = service.save(customer);
		
		assertNotNull(id);
		assertTrue(id > -1);
	}
}
