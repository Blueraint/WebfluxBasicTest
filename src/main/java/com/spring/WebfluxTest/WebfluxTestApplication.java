package com.spring.WebfluxTest;

import com.spring.WebfluxTest.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication
public class WebfluxTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebfluxTestApplication.class, args);
	}

	/*
	@Autowired
	MongoOperations mongoOperations;

	// Spring Webflux DataSource Connection시 non-blocking 으로 connection fail이 나는 현상을 막기 위해 connection을 맺어준다(?)
	@EventListener(ApplicationReadyEvent.class)
	public void doSomething() {
		Item sampleItem = new Item("sample", "sample", 10L);
		mongoOperations.save(sampleItem);
	}
	*/
}
