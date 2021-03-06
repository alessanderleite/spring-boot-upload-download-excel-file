package com.example.app.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.model.Customer;
import com.example.app.repository.CustomerRepository;
import com.example.app.util.ExcelUtils;

@Service
public class ExcelService {

	@Autowired
	CustomerRepository customerRepository;
	
	// Store File Data to Database
	public void save(MultipartFile file) {
		try {
			List<Customer> customers = ExcelUtils.parseExcelFile(file.getInputStream());
			customerRepository.saveAll(customers);
		} catch (IOException e) {
			throw new RuntimeException("Fail! -> message = " + e.getMessage());
		}
	}
	
	// Load Data to Excel File
	public ByteArrayInputStream load() {		
		try {
			List<Customer> customers = customerRepository.findAll();
			ByteArrayInputStream in = ExcelUtils.customersToExcel(customers);
			return in;
			
		} catch (IOException e) {
			throw new RuntimeException("Fail! -> message = " + e.getMessage());
		}
	}
	
	// Retrieve data from database
	public List<Customer> getAllCustomers() {
		try {
			return customerRepository.findAll();
			
		} catch (Exception e) {
			throw new RuntimeException("Fail! -> message = " + e.getMessage());
		}
	}
}
