package br.com.starwars.b2w.models;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FilmsModel extends RepresentationModel<FilmsModel>{

	private Integer count;
	private List<Film> films;
	
}
