package br.com.starwars.b2w.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.starwars.b2w.models.Film;
import br.com.starwars.b2w.models.Planet;
import br.com.starwars.b2w.repositories.PlanetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetControllerIntegrationTest {

	@Autowired
	private PlanetRepository planetRepository;
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mock;
	
	private static final String NOT_FOUND = "Not found";
	private static final String WEIRD = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
	
	@Before
	public void setup() {
		mock = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void getAllPlanets() throws Exception {
		
		String url = "/api/planets/";
		
		mock.perform(get(url))
			.andExpect(status().isOk());
		
	}
	
	@Test
	public void getPlanetById() throws Exception {

		Planet randomPlanet = getRandomPlanet();
		
		String url = "/api/planets/".concat(randomPlanet.getId());
		
		mock.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(randomPlanet.getId()))))
			.andExpect(jsonPath("$.name", is(equalTo(randomPlanet.getName()))))
			.andExpect(jsonPath("$.climate", is(equalTo(randomPlanet.getClimate()))))
			.andExpect(jsonPath("$.terrain", is(equalTo(randomPlanet.getTerrain()))));
	}
	
	@Test
	public void getPlanetByIdNotFound() throws Exception {
		
		String url = "/api/planets/".concat(NOT_FOUND);
		
		mock.perform(get(url))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", containsString("não pode ser encontrado")))
			.andExpect(jsonPath("$.code", is(equalTo(404))));

	}
	
	@Test
	public void getPlanetsByName() throws Exception {
		
		Planet randomPlanet = getRandomPlanet();
		
		String url = "/api/planets/name/".concat(randomPlanet.getName());
		
		mock.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(randomPlanet.getName())));
		
	}
	
	@Test
	public void getPlanetsByNameContainingIgnoreCase() throws Exception {
		
		Planet randomPlanet = getRandomPlanet();
		
		String halfNamePlanetLower = randomPlanet.getName().substring(1, 2).toLowerCase();
		String halfNamePlanetUpper = randomPlanet.getName().substring(2, 4).toUpperCase();
		
		String weirdo = halfNamePlanetLower.concat(halfNamePlanetUpper);
		
		String url = "/api/planets/name/".concat(weirdo);
		
		mock.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(randomPlanet.getName())));
		
	}
	
	@Test
	public void getPlanetsByNameNotFound() throws Exception {
		
		String url = "/api/planets/name/".concat(NOT_FOUND);
		
		mock.perform(get(url))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", containsString("não pode ser encontrado")))
			.andExpect(jsonPath("$.code", is(equalTo(404))));
		
	}

	@Test
	public void getAllPlanetsById() throws Exception {
		
		Planet randomPlanet1 = getRandomPlanet();
		Planet randomPlanet2 = getRandomPlanet();
		Planet randomPlanet3 = getRandomPlanet();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("?id=".concat(randomPlanet1.getId()));
		sb.append("&id=".concat(randomPlanet2.getId()));
		sb.append("&id=".concat(randomPlanet3.getId()));
		
		String url = "/api/planets/id/".concat(sb.toString());
		
		mock.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(randomPlanet1.getName())))
			.andExpect(content().string(containsString(randomPlanet2.getName())))
			.andExpect(content().string(containsString(randomPlanet3.getName())));
	
	}
	
	@Test
	public void getAllPlanetsByIdNotFound() throws Exception {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("?id=".concat(NOT_FOUND));
		sb.append("&id=".concat(WEIRD));
		
		String url = "/api/planets/id/".concat(sb.toString());
		
		mock.perform(get(url))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", containsString("não pode ser encontrado")))
			.andExpect(jsonPath("$.code", is(equalTo(404))));
		
	}
	
	@Test
	public void getAllPlanetsByName() throws Exception {
	
		Planet randomPlanet1 = getRandomPlanet();
		Planet randomPlanet2 = getRandomPlanet();
		Planet randomPlanet3 = getRandomPlanet();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("?name=".concat(randomPlanet1.getName()));
		sb.append("&name=".concat(randomPlanet2.getName()));
		sb.append("&name=".concat(randomPlanet3.getName()));
		
		String url = "/api/planets/name/".concat(sb.toString());
		
		mock.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(randomPlanet1.getName())))
			.andExpect(content().string(containsString(randomPlanet2.getName())))
			.andExpect(content().string(containsString(randomPlanet3.getName())));
		
	}
	
	@Test
	public void getAllPlanetsByNameNotFound() throws Exception {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("?name=".concat(NOT_FOUND));
		sb.append("&name=".concat(WEIRD));
		
		String url = "/api/planets/name/".concat(sb.toString());
		
		mock.perform(get(url))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", containsString("não pode ser encontrado")))
			.andExpect(jsonPath("$.code", is(equalTo(404))));
		
	}
	
	@Test
	public void getFilmsByPlanetId() throws Exception {
		
		Planet randomPlanet = getRandomPlanet();
		
		String url = "/api/planets/".concat(randomPlanet.getId()).concat("/films/");
		
		mock.perform(get(url))
			.andExpect(status().isOk());
	}
	
	@Test
	public void createPlanet() throws Exception {
	
		String url = "/api/planets/";
		
		var film1 = new Film();
		var film2 = new Film();
		
		film1.setTitle("Correndo por aí");
		film2.setTitle("Plano como uma folha de papel");
		
		List<Film> films = Stream.of(film1, film2).collect(Collectors.toList());
		
		mock.perform(post(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(asJsonString(new Planet(null, "Novo Planeta Plano Criado", "Normal", "Plano, muito plano mesmo", films))))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(notNullValue())));
		
	}
	
	@Test
	public void createPlanetFailNullFields() throws Exception {
	
		String url = "/api/planets/";
		
		String jsonFail = "{\"name\":\"PostFail\"}";
		
		mock.perform(post(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(jsonFail))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error", containsString("O clima do planeta não pode ser nulo ou vazio")))
			.andExpect(jsonPath("$.error", containsString("O terreno do planeta não pode ser nulo ou vazio")));
		
	}
	
	@Test
	public void createPlanetFailNotReadable() throws Exception {
	
		String url = "/api/planets/";
		
		String jsonFail = "{\"n123456ame\":\"PostFail\"}";
		
		mock.perform(post(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(jsonFail))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message", containsString("A requisição possui formato inválido")));
		
	}
		
	@Test
	public void updatePlanet() throws Exception {
		
		Planet lastPlanetCreated = getLastPlanetCreated();
		
		String url = "/api/planets/".concat(lastPlanetCreated.getId());
		
		mock.perform(put(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(asJsonString(new Planet(null, "Planeta Quadrado Criado", "Normal", "Cubo, de 38 faces", null))))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(lastPlanetCreated.getId())))
			.andExpect(jsonPath("$.name", is("Planeta Quadrado Criado")));
		
	}
	
	@Test
	public void updatePlanetFailNullFields() throws Exception {
	
		Planet lastPlanetCreated = getLastPlanetCreated();
		
		String url = "/api/planets/".concat(lastPlanetCreated.getId());
		
		String jsonFail = "{\"name\":\"PostFail\"}";
		
		mock.perform(put(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(jsonFail))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error", containsString("O clima do planeta não pode ser nulo ou vazio")))
			.andExpect(jsonPath("$.error", containsString("O terreno do planeta não pode ser nulo ou vazio")));
		
	}

	@Test
	public void updatePlanetFailNotReadable() throws Exception {
	
		Planet lastPlanetCreated = getLastPlanetCreated();
		
		String url = "/api/planets/".concat(lastPlanetCreated.getId());
		
		String jsonFail = "{\"n123456ame\":\"PostFail\"}";
		
		mock.perform(put(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(jsonFail))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message", containsString("A requisição possui formato inválido")));
		
	}
	
	@Test
	public void deletePlanet() throws Exception {
	
		Planet lastPlanetCreated = getLastPlanetCreated();
		
		String url = "/api/planets/".concat(lastPlanetCreated.getId());
	
		mock.perform(delete(url))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteNotFoundPlanet() throws Exception {
		
		String url = "/api/planets/".concat(NOT_FOUND);
	
		mock.perform(delete(url))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", containsString("O planeta Not found não pode ser encontrado")));
		
	}
	
	private Planet getRandomPlanet() {
		List<Planet> allPlanets = planetRepository.findAll();
		
		Random r = new Random();
		int result = r.nextInt(allPlanets.size()-1) + 1;
		
		return allPlanets.get(result);
	}
	
	private Planet getLastPlanetCreated() {
		List<Planet> allPlanets = planetRepository.findAll();
		
		int size = allPlanets.size();
		
		return allPlanets.get(size-1);
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
}
