
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.service.GanaderoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.util.GanaderoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GanaderoController {

	@Autowired
	private GanaderoService	ganaderoService;

	@Autowired
	private UserService		userService;

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("ganadero")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new GanaderoValidator(this.ganaderoService, this.userService));
	}

	@GetMapping(value = "/ganadero/new")
	public String crearGanadero(final ModelMap modelMap) {
		String view = "ganadero/editGanadero";
		modelMap.addAttribute("ganadero", new Ganadero());
		return view;
	}

	@PostMapping(value = "/ganadero/new")
	public String salvarGanadero(@Valid Ganadero ganadero, final BindingResult result, final ModelMap modelMap) {
		String view = "welcome";
		if (result.hasErrors()) {
			modelMap.addAttribute("ganadero", ganadero);
			return "ganadero/editGanadero";
		} else {
			//creating ganadero, user and authorities
			this.ganaderoService.saveGanadero(ganadero);
			modelMap.addAttribute("message", "¡Tu cuenta como ganadero ha sido creada con éxito!");
		}
		return view;
	}
}
