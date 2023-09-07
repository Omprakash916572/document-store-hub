package com.dev.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.entity.UserImageDocuments;

@Repository
public interface UserImageDocumentsRepo extends CrudRepository<UserImageDocuments, Integer> {
	
//	@Query(value = "", nativeQuery = true)
//	UserDocuments findByProjectUUIDAndEmailAndIRN();


}