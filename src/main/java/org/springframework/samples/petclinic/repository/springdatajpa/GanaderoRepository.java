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

package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Ganadero;

public interface GanaderoRepository extends CrudRepository<Ganadero, Integer> {

	//Devuelve el ganadero según el nombre de usuario
	@Query("select g from Ganadero g where g.user.username = ?1")
	Ganadero findGanaderoByUsername(String username);

	//Devuelve el ganadero según el dni
	@Query("select g from Ganadero g where g.dni = ?1")
	Optional<Ganadero> findGanaderoByDni(String dni);
}
