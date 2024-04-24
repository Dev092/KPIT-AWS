package com.dev.demo3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo31Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Demo31Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		if(args.length<1)
		{
			System.out.println("Error");
		}
		System.out.println("File to be uploaded to bucket: "+args[1]);
		
	}

}
