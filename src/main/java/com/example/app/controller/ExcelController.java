package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.app.model.Customer;
import com.example.app.service.ExcelService;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

	@Autowired
	ExcelService excelService;
	
	/*
	 * Upload Files
	 */
	@PostMapping("/upload")
	public String uploadMultipartFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			excelService.save(file);
			message = "Upload the file successfully: " + file.getOriginalFilename();
			return message;
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return message;
		}
	}
	
	/*
	 * Get all data from database
	 */
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		try {
			List<Customer> customers = excelService.getAllCustomers();
			
			if (customers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(customers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/*
	 * Download Files
	 */
	@GetMapping("/download")
	public ResponseEntity<InputStreamResource> downloadFile() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=customers.xlsx");
		
		return ResponseEntity
				.ok()
				.headers(headers)
				.body(new InputStreamResource(excelService.load()));
	}
}
