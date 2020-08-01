package br.com.starwars.b2w.services;

import java.util.List;

import br.com.starwars.b2w.models.FilmsModel;
import br.com.starwars.b2w.models.Planet;
import br.com.starwars.b2w.models.PlanetModel;

public interface PlanetService {

	List<PlanetModel> findAllPlanets();
	
	PlanetModel findPlanetById(String id);
	
	List<PlanetModel> findAllPlanetsById(List<String> ids);
	
	List<PlanetModel> findPlanetByName(String name);

	List<PlanetModel> findAllPlanetsByName(List<String> names);
	
	FilmsModel findFilmsByPlanetId(String id);

	PlanetModel savePlanet(Planet planet);
	
	PlanetModel updatePlanet(String id, Planet planet);
	
	void deletePlanet(String id);
	
}
