package com.dev.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.entity.Documents;

@Repository
public interface DocumentsRepo extends CrudRepository<Documents, Integer> {
	
//	@Query(value = "", nativeQuery = true)
//	UserDocuments findByProjectUUIDAndEmailAndIRN();


}