package br.com.starwars.b2w.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.starwars.b2w.utils.Constants;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("/")
public class HomeController {
	
	@GetMapping("/")
    public String index() {
		return Constants.INDEX_VIEW;
    }

}
