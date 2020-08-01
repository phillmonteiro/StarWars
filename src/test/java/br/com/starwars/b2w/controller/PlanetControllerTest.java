package br.com.starwars.b2w.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.starwars.b2w.controllers.PlanetController;
import br.com.starwars.b2w.exceptions.PlanetNotFoundException;
import br.com.starwars.b2w.models.Film;
import br.com.starwars.b2w.models.FilmsModel;
import br.com.starwars.b2w.models.Planet;
import br.com.starwars.b2w.models.PlanetModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetControllerTest {

	@Autowired
	private PlanetController planetController;
	
	private static final String NOT_FOUND = "Not found";
	private static final String WEIRD = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
	
	@Test
	public void getAllPlanets() {
		
		ResponseEntity<List<PlanetModel>> allPlanets = planetController.getAllPlanets();
		
		assertThat(allPlanets.getBody().size()).isNotEqualTo(0);
		assertThat(allPlanets.getStatusCode()).isEqualTo(HttpStatus.OK);
		
	}
	
	@Test
	public void getPlanetById() {
		
		PlanetModel planetModel = getRandomPlanet();
		
		ResponseEntity<PlanetModel> planetById = planetController.getPlanetById(planetModel.getId());
		
		assertThat(planetById.getBody().getId()).isEqualTo(planetModel.getId());
		assertThat(planetById.getStatusCode()).isEqualTo(HttpStatus.OK);
		
	}
	
	@Test
	public void getPlanetByIdNotFound() {
		
		try {
			planetController.getPlanetById(NOT_FOUND);
			assertTrue("Exceção falhou ao ser lançada.", false);
		}catch (PlanetNotFoundException e) {
			assertTrue("Exceção lançada com sucesso.", true);
		}

	}
	
	@Test
	public void getPlanetsByName() {
		
		PlanetModel randomPlanet = getRandomPlanet();
		
		ResponseEntity<List<PlanetModel>> planetsByName = planetController.getPlanetsByName(randomPlanet.getName());
		
		assertThat(planetsByName.getBody().size()).isGreaterThan(0);
		assertThat(planetsByName.getStatusCode()).isEqualTo(HttpStatus.OK);
		
	}
	
	@Test
	public void getPlanetsByNameContainingIgnoreCase() {
		
		PlanetModel randomPlanet = getRandomPlanet();
		
		String halfNamePlanetLower = randomPlanet.getName().substring(1, 2).toLowerCase();
		String halfNamePlanetUpper = randomPlanet.getName().substring(2, 4).toUpperCase();
		
		String weird = halfNamePlanetLower.concat(halfNamePlanetUpper);
		
		ResponseEntity<List<PlanetModel>> planetsByName = planetController.getPlanetsByName(weird);
		
		assertThat(planetsByName.getBody().size()).isGreaterThan(0);
		assertThat(planetsByName.getStatusCode()).isEqualTo(HttpStatus.OK);
		
	}
	
	@Test
	public void getPlanetsByNameNotFound() {
		
		try {
			planetController.getPlanetsByName(NOT_FOUND);
			assertTrue("Exceção falhou ao ser lançada.", false);
		}catch (PlanetNotFoundException e) {
			assertTrue("Exceção lançada com sucesso.", true);
		}
		
	}

	@Test
	public void getAllPlanetsById() {
		
		String randomPlanetId1 = getRandomPlanet().getId();
		String randomPlanetId2 = getRandomPlanet().getId();
		String randomPlanetId3 = getRandomPlanet().getId();
		
		List<String> ids = Stream.of(randomPlanetId1, randomPlanetId2, randomPlanetId3).collect(Collectors.toList());
		
		ResponseEntity<List<PlanetModel>> allPlanetsById = planetController.getAllPlanetsById(ids);
		
		assertThat(allPlanetsById.getBody().size()).isGreaterThanOrEqualTo(1);
		assertThat(allPlanetsById.getStatusCode()).isEqualTo(HttpStatus.OK);
	
	}
	
	@Test
	public void getAllPlanetsByIdNotFound() {
		
		List<String> ids = Stream.of(NOT_FOUND, NOT_FOUND.concat("2")).collect(Collectors.toList());

		try {
			planetController.getAllPlanetsById(ids);
			assertTrue("Exceção falhou ao ser lançada.", false);
		}catch (PlanetNotFoundException e) {
			assertTrue("Exceção lançada com sucesso.", true);
		}
	
	}
	
	@Test
	public void getAllPlanetsByName() {
	
		String randomPlanetName1 = getRandomPlanet().getName();
		String randomPlanetName2 = getRandomPlanet().getName();
		String randomPlanetName3 = getRandomPlanet().getName();
		
		List<String> names = Stream.of(randomPlanetName1, randomPlanetName2, randomPlanetName3).collect(Collectors.toList());
		
		ResponseEntity<List<PlanetModel>> allPlanetsById = planetController.getAllPlanetsByName(names);
		
		assertThat(allPlanetsById.getBody().size()).isGreaterThanOrEqualTo(1);
		assertThat(allPlanetsById.getStatusCode()).isEqualTo(HttpStatus.OK);
		
	}
	
	@Test
	public void getAllPlanetsByNameNotFound() {
		
		List<String> ids = Stream.of(NOT_FOUND, NOT_FOUND.concat("2")).collect(Collectors.toList());

		try {
			planetController.getAllPlanetsByName(ids);
			assertTrue("Exceção falhou ao ser lançada.", false);
		}catch (PlanetNotFoundException e) {
			assertTrue("Exceção lançada com sucesso.", true);
		}
	
	}
	
	@Test
	public void getFilmsByPlanetId() {
		
		String planetId = getRandomPlanet().getId();
		
		ResponseEntity<FilmsModel> filmsByPlanetId = planetController.getFilmsByPlanetId(planetId);
		
		assertThat(filmsByPlanetId.getStatusCode()).isEqualTo(HttpStatus.OK);

	}
	
	@Test
	public void createPlanet() {
	
		Planet planet = new Planet();
		
		planet.setId(WEIRD);
		planet.setName("Novo Sol Criado");
		planet.setClimate("Verão 8050 graus");
		planet.setTerrain("Vulcões, Saara, Hell");
		
		var film1 = new Film();
		var film2 = new Film();
		
		film1.setTitle("Em busca de um copo de água");
		film2.setTitle("Copo d'água e Eu");
		
		List<Film> films = Stream.of(film1, film2).collect(Collectors.toList());
		
		planet.setFilms(films);
		
		ResponseEntity<PlanetModel> createPlanet = planetController.createPlanet(planet);
		
		assertThat(createPlanet.getBody().getId()).isNotEqualTo(WEIRD);
		assertThat(createPlanet.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		ResponseEntity<PlanetModel> savedPlanet = planetController.getPlanetById(createPlanet.getBody().getId());
		
		assertThat(savedPlanet.getBody().getId()).isNotEqualTo(planet.getId());
		assertThat(savedPlanet.getBody().getName()).isEqualTo(planet.getName());
		assertThat(savedPlanet.getBody().getClimate()).isEqualTo(planet.getClimate());
		assertThat(savedPlanet.getBody().getTerrain()).isEqualTo(planet.getTerrain());
		assertThat(savedPlanet.getBody().getFilms().size()).isEqualTo(planet.getFilms().size());
		
	}
		
	@Test
	public void updatePlanet() {
		
		PlanetModel lastPlanetCreated = getLastPlanetCreated();
		
		Planet updatePlanet = new Planet();
		
		updatePlanet.setId(WEIRD);
		updatePlanet.setName("Nova Lua Criada");
		updatePlanet.setClimate("Inverno -3400 graus");
		updatePlanet.setTerrain("Tundra, Alaska, Polo Sul");
		
		var film1 = new Film();
		var film2 = new Film();
		
		film1.setTitle("Em busca de um cobertor");
		film2.setTitle("Fogo e Eu");
		
		List<Film> films = Stream.of(film1, film2).collect(Collectors.toList());
		
		updatePlanet.setFilms(films);
		
		ResponseEntity<PlanetModel> updatedPlanet = planetController.updatePlanet(lastPlanetCreated.getId(), updatePlanet);
		
		assertThat(updatedPlanet.getBody().getId()).isNotEqualTo(WEIRD);
		assertThat(updatedPlanet.getStatusCode()).isEqualTo(HttpStatus.OK);

		ResponseEntity<PlanetModel> savedPlanet = planetController.getPlanetById(updatedPlanet.getBody().getId());
		
		assertThat(savedPlanet.getBody().getId()).isNotEqualTo(updatePlanet.getId());
		assertThat(savedPlanet.getBody().getName()).isEqualTo(updatePlanet.getName());
		assertThat(savedPlanet.getBody().getClimate()).isEqualTo(updatePlanet.getClimate());
		assertThat(savedPlanet.getBody().getTerrain()).isEqualTo(updatePlanet.getTerrain());
		assertThat(savedPlanet.getBody().getFilms().size()).isEqualTo(updatePlanet.getFilms().size());
		
	}

	
	
	@Test
	public void deletePlanet() {
	
		PlanetModel lastPlanetCreated = getLastPlanetCreated();
		
		ResponseEntity<?> deletePlanet = planetController.deletePlanet(lastPlanetCreated.getId());
		
		assertThat(deletePlanet.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		try {
			planetController.getPlanetById(lastPlanetCreated.getId());
			assertTrue("Exceção falhou ao ser lançada.", false);
		}catch (PlanetNotFoundException e) {
			assertTrue("Exceção lançada com sucesso.", true);
		}
		
	}
	
	@Test
	public void deleteNotFoundPlanet() {
		
		try {
			planetController.deletePlanet(NOT_FOUND);
			assertTrue("Exceção falhou ao ser lançada.", false);
		}catch (PlanetNotFoundException e) {
			assertTrue("Exceção lançada com sucesso.", true);
		}
		
	}
	
	private PlanetModel getRandomPlanet() {
		ResponseEntity<List<PlanetModel>> allPlanets = planetController.getAllPlanets();
		
		Random r = new Random();
		int result = r.nextInt(61-1) + 1;
		
		return allPlanets.getBody().get(result);
	}
	
	private PlanetModel getLastPlanetCreated() {
		ResponseEntity<List<PlanetModel>> allPlanets = planetController.getAllPlanets();
		
		int size = allPlanets.getBody().size();
		
		return allPlanets.getBody().get(size-1);
	}
	
	
}
