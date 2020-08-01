package br.com.starwars.b2w.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Document(collection = "planets")
public class Planet {

	@Id
	private String id;
	@NotBlank(message = "{msg.planet.name.notblank}")
	private String name;
	@NotBlank(message = "{msg.planet.climate.notblank}")
	private String climate;
	@NotBlank(message = "{msg.planet.terrain.notblank}")
	private String terrain;
	@Valid
	private List<Film> films;
	
}
