package br.com.starwars.b2w.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.starwars.b2w.controllers.PlanetController;
import br.com.starwars.b2w.models.FilmsModel;
import br.com.starwars.b2w.models.Planet;
import br.com.starwars.b2w.models.PlanetModel;
import br.com.starwars.b2w.utils.Constants;

@Component
public class PlanetModelAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	
	public PlanetModel toHateoasPlanetModel(Planet planet) {
		
		PlanetModel model = toModel(planet);
		
		model.setFilmsAppearances(model.getFilms().size());
		
		return model
				.add(linkTo(methodOn(PlanetController.class).getPlanetById(planet.getId())).withSelfRel().withType(Constants.GET_UP))
				.add(linkTo(methodOn(PlanetController.class).getFilmsByPlanetId(planet.getId())).withRel(Constants.FILMS).withType(Constants.GET_UP))
				.add(linkTo(methodOn(PlanetController.class).updatePlanet(planet.getId(), planet)).withRel(Constants.UPDATE).withType(Constants.PUT_UP))
				.add(linkTo(methodOn(PlanetController.class).deletePlanet(planet.getId())).withRel(Constants.DELETE).withType(Constants.DELETE_UP));
	}
	
	public List<PlanetModel> toHateoasPlanetModelCollection(List<Planet> allPlanets) {
		
		List<PlanetModel> collectionModel = toCollectionModel(allPlanets);
		
		collectionModel.stream().forEach(films -> films.setFilmsAppearances(films.getFilms().size()));
		
		return collectionModel.stream()
				.map(planet -> planet.add(linkTo(methodOn(PlanetController.class).getPlanetById(planet.getId())).withSelfRel().withType(Constants.GET_UP)))
				.map(planet -> planet.add(linkTo(methodOn(PlanetController.class).getFilmsByPlanetId(planet.getId())).withRel(Constants.FILMS).withType(Constants.GET_UP)))
				.map(planet -> planet.add(linkTo(methodOn(PlanetController.class).updatePlanet(planet.getId(), null)).withRel(Constants.UPDATE).withType(Constants.PUT_UP)))
				.map(planet -> planet.add(linkTo(methodOn(PlanetController.class).deletePlanet(planet.getId())).withRel(Constants.DELETE).withType(Constants.DELETE_UP)))
				.collect(Collectors.toList());
	}
	
	public FilmsModel toHateoasFilmsModel(Planet foundPlanet) {
		
		FilmsModel films = new FilmsModel();
		films.setCount(foundPlanet.getFilms().size());
		films.setFilms(foundPlanet.getFilms());
		
		return films.add(linkTo(methodOn(PlanetController.class).getFilmsByPlanetId(foundPlanet.getId())).withSelfRel().withType(Constants.GET_UP))
				.add(linkTo(methodOn(PlanetController.class).getPlanetById(foundPlanet.getId())).withRel(Constants.PLANET).withType(Constants.GET_UP))
				.add(linkTo(methodOn(PlanetController.class).updatePlanet(foundPlanet.getId(), foundPlanet)).withRel(Constants.UPDATE).withType(Constants.PUT_UP))
				.add(linkTo(methodOn(PlanetController.class).deletePlanet(foundPlanet.getId())).withRel(Constants.DELETE).withType(Constants.DELETE_UP));
	}
	
	private PlanetModel toModel(Planet planet) {
		return modelMapper.map(planet, PlanetModel.class);
	}
	
	private List<PlanetModel> toCollectionModel(List<Planet> planets) {
		return planets.stream().map(planet -> toModel(planet)).collect(Collectors.toList());
	}
}
