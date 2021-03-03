
package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	@Query("Select u from User u where u.username = ?1")
	Optional<User> findUserByUsername(String username);

}
