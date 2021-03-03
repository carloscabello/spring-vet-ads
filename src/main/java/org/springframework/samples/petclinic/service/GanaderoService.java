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
import org.springframework.samples.petclinic.model.Ganadero;
import org.springframework.samples.petclinic.repository.springdatajpa.GanaderoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GanaderoService {

	@Autowired
	private GanaderoRepository	ganaderoRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@Autowired
	public GanaderoService(final GanaderoRepository ganaderoRepository) {
		this.ganaderoRepository = ganaderoRepository;
	}

	public GanaderoService(final GanaderoRepository ganaderoRepository, final UserService userService, final AuthoritiesService authoritiesService) {

		this.ganaderoRepository = ganaderoRepository;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}

	@Transactional(readOnly = true)
	public Optional<Ganadero> findGanaderoById(final int id) throws DataAccessException {
		return this.ganaderoRepository.findById(id);
	}

	@Transactional
	public Ganadero findGanaderoByLogedUser() {
		String loggedUsername = this.userService.getAuthenticatedUsername();
		Ganadero ganadero = this.ganaderoRepository.findGanaderoByUsername(loggedUsername);
		return ganadero;
	}

	@Transactional
	public void saveGanadero(final Ganadero ganadero) throws DataAccessException {
		//creating owner
		this.ganaderoRepository.save(ganadero);
		//creating user
		this.userService.saveUser(ganadero.getUser());
		//creating authorities
		this.authoritiesService.saveAuthorities(ganadero.getUser().getUsername(), "ganadero");
	}

	public Optional<Ganadero> findGanaderoByDni(final String dni) {
		return this.ganaderoRepository.findGanaderoByDni(dni);
	}
}
