package com.example;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class CologneJugApplication {

	public static void main(String[] args) {
		SpringApplication.run(CologneJugApplication.class, args);
	}
}

@Component
class DataGenerator implements CommandLineRunner {

	@Autowired
	BeverageRepository repo;

	@Override
	public void run(String... arg0) throws Exception {
		Stream.of("Beer", "Cider").map(str -> new Beverage(str, 5.5d)).forEach(repo::save);
	}

}

@RepositoryRestResource(path="beverage")
interface BeverageRepository extends JpaRepository<Beverage, Long> {
}
