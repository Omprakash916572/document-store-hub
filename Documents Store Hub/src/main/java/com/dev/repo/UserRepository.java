package com.dev.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.dev.entity.Users;

@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {

	@Query(value = "select * from users u where u.email ilike :email", nativeQuery = true)
	Users findByEmail(String email);

}