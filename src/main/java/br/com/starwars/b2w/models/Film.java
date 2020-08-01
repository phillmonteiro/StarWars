package br.com.starwars.b2w.models;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Film {

	@NotBlank(message = "{msg.film.title.notblank}")
	private String title;
	
}
