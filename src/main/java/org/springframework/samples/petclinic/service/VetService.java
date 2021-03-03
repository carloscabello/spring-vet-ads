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

package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Veterinario;
import org.springframework.samples.petclinic.repository.springdatajpa.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class VetService {

	private VetRepository		vetRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@Autowired
	public VetService(final VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	public VetService(final VetRepository vetRepository, final UserService userService, final AuthoritiesService authoritiesService) {
		this.vetRepository = vetRepository;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}

	@Transactional
	public void saveVet(final Veterinario vet) throws DataAccessException {
		//creating veterinario
		vet.setEsDisponible(true);
		this.vetRepository.save(vet);
		//creating user
		this.userService.saveUser(vet.getUser());
		//creating authorities
		this.authoritiesService.saveAuthorities(vet.getUser().getUsername(), "veterinario");
	}

	@Transactional
	public Veterinario findVeterinarioByLogedUser() {
		Veterinario vet = this.vetRepository.findVeterinarioByUsername(this.userService.getAuthenticatedUsername());
		return vet;
	}

	@Transactional
	public Iterable<Veterinario> encontrarPorEspecialidad(final int especialidad) {
		return this.vetRepository.encontrarPorEspecialidad(especialidad);
	}
	@Transactional
	public Optional<Veterinario> encontrarPorID(final int id) throws DataAccessException {
		return this.vetRepository.findById(id);
	}

	public Optional<Veterinario> findVeterinarioByDni(final String dni) {
		return this.vetRepository.findVeterinarioByDni(dni);
	}

}
