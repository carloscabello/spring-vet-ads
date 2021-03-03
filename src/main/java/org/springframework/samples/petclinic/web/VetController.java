/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.TiposGanado;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.TiposGanadoRepository;
import org.springframework.samples.petclinic.repository.springdatajpa.VetRepository;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.VeterinarioValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	@Autowired
	private final VetService		vetService;

	@Autowired
	private final UserService		userService;

	@Autowired
	private TiposGanadoRepository	tiposGanadoRepository;


	@Autowired
	public VetController(final VetService vetService, final UserService userService, final TiposGanadoRepository tipoGanadoRepository) {
		this.vetService = vetService;
		this.userService = userService;
		this.tiposGanadoRepository = tiposGanadoRepository;
	}

	public VetController(final VetService vetService, final UserService userService, final AuthoritiesService authoritiesService, final TiposGanadoRepository tipoGanadoRepository, final VetRepository vetRepository) {
		this.vetService = vetService;
		this.userService = userService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("veterinario")
	public void initBinder(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new VeterinarioValidator(this.vetService, this.userService));
	}

	@GetMapping(value = "/veterinario/new")
	public String crearVeterinario(final ModelMap modelMap) {
		String view = "veterinario/editVeterinario";
		modelMap.addAttribute("veterinario", new Veterinario());
		modelMap.addAttribute("todosTiposG", this.tiposGanadoRepository.findAll());
		return view;
	}

	@PostMapping(value = "/veterinario/new")
	public String salvarVeterinario(@Valid final Veterinario veterinario, final BindingResult result, final ModelMap modelMap) {
		String view = "welcome";
		if (result.hasErrors()) {
			modelMap.addAttribute("veterinario", veterinario);
			modelMap.addAttribute("todosTiposG", this.tiposGanadoRepository.findAll());
			return "veterinario/editVeterinario";
		} else {
			//creating veterinario, user and authorities
			vetService.saveVet(veterinario);
			modelMap.addAttribute("message", "¡Tu cuenta como veterinario ha sido creada con éxito!");
		}
		return view;
	}

	@GetMapping(value = "/veterinario/encontrar")
	public String listarVeterinario(final ModelMap modelMap) {
		String view = "veterinario/encontrarVeterinario";
		modelMap.addAttribute("todosTiposG", this.tiposGanadoRepository.findAll());
		return view;
	}

	@PostMapping(value = "/veterinario/encontrar")
	public String buscarVeterinario(@Valid final TiposGanado tiposganado, final BindingResult result, final ModelMap modelMap) {
		String view = "veterinario/listarVeterinario";
		if (result.hasErrors()) {
			modelMap.addAttribute("todosTiposG", this.tiposGanadoRepository.findAll());
			return "veterinario/encontrarVeterinario";
		} else {
			//creating veterinario, user and authorities
			Iterable<Veterinario> results = this.vetService.encontrarPorEspecialidad(tiposganado.getId());

			modelMap.addAttribute("Veterinarios", results);
		}

		return view;
	}

	@GetMapping(value = "/veterinario/{vetId}/show")
	public String showVeterinario(final ModelMap modelMap, @PathVariable("vetId") final int vetId) {
		String view = "veterinario/mostrarVeterinario";
		Optional<Veterinario> vets = this.vetService.encontrarPorID(vetId);
		modelMap.addAttribute("vets", vets.get());
		modelMap.addAttribute("tiposGanado", vets.get().getTiposGanado());
		return view;
	}
}
