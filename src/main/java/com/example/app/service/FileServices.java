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
public class FileServices {

	@Autowired
	CustomerRepository customerRepository;
	
	// Store File Data to Database
	public void store(MultipartFile file) {
		try {
			List<Customer> customers = ExcelUtils.parseExcelFile(file.getInputStream());
			// Save Customers to DataBase
			customerRepository.saveAll(customers);
		} catch (IOException e) {
			throw new RuntimeException("Fail! -> message = " + e.getMessage());
		}
	}
	
	// Load Data to Excel File
	public ByteArrayInputStream loadFile() {
		List<Customer> customers = customerRepository.findAll();
		
		try {
			ByteArrayInputStream in = ExcelUtils.customersToExcel(customers);
			return in;
			
		} catch (IOException e) {
			return null;
		}
	}
}
