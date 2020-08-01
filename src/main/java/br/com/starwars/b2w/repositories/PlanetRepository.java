package br.com.starwars.b2w.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.starwars.b2w.models.Planet;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String>{

	List<Planet> findByNameContainingIgnoreCase(String name);
	
}
