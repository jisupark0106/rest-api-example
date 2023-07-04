package me.didoo.whiteshiprestapi;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WhiteshipRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhiteshipRestApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
