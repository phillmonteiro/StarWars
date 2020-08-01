package br.com.starwars.b2w.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.starwars.b2w.models.FilmsModel;
import br.com.starwars.b2w.models.Planet;
import br.com.starwars.b2w.models.PlanetModel;
import br.com.starwars.b2w.services.PlanetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("API REST Planetas")
@RestController
@RequestMapping("api/planets")
public class PlanetController {

	@Autowired
	private PlanetService planetService;
	
	@ApiOperation(value = "Busca todos os planetas.")
	@GetMapping("/")
	public ResponseEntity<List<PlanetModel>> getAllPlanets(){
		return ResponseEntity.status(HttpStatus.OK).body(planetService.findAllPlanets());
	}
	
	@ApiOperation(value = "Busca planeta por ID.")
	@GetMapping("/{id}")
	public ResponseEntity<PlanetModel> getPlanetById(@PathVariable String id){
		return ResponseEntity.status(HttpStatus.OK).body(planetService.findPlanetById(id));
	}
	
	@ApiOperation(value = "Busca planetas por multiplos ID's (Params ?id={id}).")
	@GetMapping("/id/")
	public ResponseEntity<List<PlanetModel>> getAllPlanetsById(@RequestParam List<String> id){
		return ResponseEntity.status(HttpStatus.OK).body(planetService.findAllPlanetsById(id));
	}
	
	@ApiOperation(value = "Busca planetas que contenham o nome.")
	@GetMapping("/name/{name}")
	public ResponseEntity<List<PlanetModel>> getPlanetsByName(@PathVariable String name){
		return ResponseEntity.status(HttpStatus.OK).body(planetService.findPlanetByName(name));
	}
	
	@ApiOperation(value = "Busca planetas que contenham multiplos nomes (Params ?name={name}).")
	@GetMapping("/name/")
	public ResponseEntity<List<PlanetModel>> getAllPlanetsByName(@RequestParam List<String> name){
		return ResponseEntity.status(HttpStatus.OK).body(planetService.findAllPlanetsByName(name));
	}
	
	@ApiOperation(value = "Busca filmes do planeta por ID do planeta.")
	@GetMapping("/{id}/films/")
	public ResponseEntity<FilmsModel> getFilmsByPlanetId(@PathVariable String id){
		return ResponseEntity.status(HttpStatus.OK).body(planetService.findFilmsByPlanetId(id));
	}
	
	@ApiOperation(value = "Cria um planeta.")
	@PostMapping("/")
	public ResponseEntity<PlanetModel> createPlanet(@Valid @RequestBody Planet planet){
		return ResponseEntity.status(HttpStatus.CREATED).body(planetService.savePlanet(planet));
	}
	
	@ApiOperation(value = "Atualiza um planeta.")
	@PutMapping("/{id}")
	public ResponseEntity<PlanetModel> updatePlanet(@PathVariable String id, @Valid @RequestBody Planet planet){
		return ResponseEntity.status(HttpStatus.OK).body(planetService.updatePlanet(id, planet));
	}
	
	@ApiOperation(value = "Exclui um planeta.")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePlanet(@PathVariable String id){
		planetService.deletePlanet(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
