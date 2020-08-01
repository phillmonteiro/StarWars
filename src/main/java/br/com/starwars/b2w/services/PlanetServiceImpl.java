package br.com.starwars.b2w.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.starwars.b2w.assembler.PlanetModelAssembler;
import br.com.starwars.b2w.exceptions.PlanetNotFoundException;
import br.com.starwars.b2w.models.FilmsModel;
import br.com.starwars.b2w.models.Planet;
import br.com.starwars.b2w.models.PlanetModel;
import br.com.starwars.b2w.repositories.PlanetRepository;
import br.com.starwars.b2w.utils.Constants;

@Service
public class PlanetServiceImpl implements PlanetService {

	@Autowired
	private PlanetRepository planetRepository;
	
	@Autowired
	private PlanetModelAssembler planetAssembler;
	
	@Override
	public List<PlanetModel> findAllPlanets() {
		 
		List<Planet> allPlanets = planetRepository.findAll();
		
		return planetAssembler.toHateoasPlanetModelCollection(allPlanets);
	}

	
	@Override
	public PlanetModel findPlanetById(String id) {
		
		Planet foundPlanet = planetRepository.findById(id).orElseThrow(() -> new PlanetNotFoundException(id));
		
		return planetAssembler.toHateoasPlanetModel(foundPlanet);
	}


	
	@Override
	public List<PlanetModel> findAllPlanetsById(List<String> ids) {
		
		List<Planet> allPlanetsById = StreamSupport.stream(planetRepository.findAllById(ids).spliterator(), false).collect(Collectors.toList());
		
		if(allPlanetsById.isEmpty()) {
			throw new PlanetNotFoundException(ids.toString());
		}
		
		return planetAssembler.toHateoasPlanetModelCollection(allPlanetsById);
	}
	
	@Override
	public List<PlanetModel> findPlanetByName(String name) {
		
		List<Planet> allPlanetsByName = planetRepository.findByNameContainingIgnoreCase(name);
		
		if(allPlanetsByName.isEmpty()) {
			throw new PlanetNotFoundException(name);
		}
		
		return planetAssembler.toHateoasPlanetModelCollection(allPlanetsByName);
	}
	
	@Override
	public List<PlanetModel> findAllPlanetsByName(List<String> names) {
		
		List<Planet> allPlanetsByName = new ArrayList<Planet>();
				
		names.stream().forEach(name -> planetRepository.findByNameContainingIgnoreCase(name).forEach(allPlanetsByName::add));
	
		if(allPlanetsByName.isEmpty()) {
			throw new PlanetNotFoundException(names.toString());
		}
		
		return planetAssembler.toHateoasPlanetModelCollection(allPlanetsByName);
	}

	@Override
	public FilmsModel findFilmsByPlanetId(String id) {
		
		Planet foundPlanet = planetRepository.findById(id).orElseThrow(() -> new PlanetNotFoundException(id));
		
		return planetAssembler.toHateoasFilmsModel(foundPlanet);
	}


	
	
	@Override
	public PlanetModel savePlanet(Planet planet) {
		
		Planet newPlanet = new Planet();
		BeanUtils.copyProperties(planet, newPlanet, Constants.FIELD_ID);
		
		if(newPlanet.getFilms() == null) {	newPlanet.setFilms(Arrays.asList()); }
		
		Planet savePlanet = planetRepository.save(newPlanet);
		
		return planetAssembler.toHateoasPlanetModel(savePlanet);
	}


	@Override
	public PlanetModel updatePlanet(String id, Planet planet) {
		
		Planet foundPlanet = planetRepository.findById(id).orElseThrow(() -> new PlanetNotFoundException(id));
		
		BeanUtils.copyProperties(planet, foundPlanet, Constants.FIELD_ID);
		
		if(foundPlanet.getFilms() == null) {	foundPlanet.setFilms(Arrays.asList()); }
		
		Planet savePlanet = planetRepository.save(foundPlanet);
		
		return planetAssembler.toHateoasPlanetModel(savePlanet);
	}

	@Override
	public void deletePlanet(String id) {
		
		planetRepository.findById(id).orElseThrow(() -> new PlanetNotFoundException(id));
		planetRepository.deleteById(id);
	}

}
