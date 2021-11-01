package com.devsuperior.dscatalog;

import com.devsuperior.dscatalog.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DscatalogApplication {


	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}

}
