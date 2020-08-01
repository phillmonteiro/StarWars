package br.com.starwars.b2w.models;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class PlanetModel extends RepresentationModel<PlanetModel>{

	private String id;
	private String name;
	private String climate;
	private String terrain;
	private List<Film> films;
	private Integer filmsAppearances;

}