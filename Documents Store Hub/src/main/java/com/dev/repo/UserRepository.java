package com.dev.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dev.DTO.DocumentDTO;
import com.dev.entity.Users;

@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {

	@Query(value = "select * from users u where u.email ilike :email", nativeQuery = true)
	Users findByEmail(String email);

	@Query("SELECT NEW com.dev.DTO.DocumentDTO(uid.orignalDocumentName, d.documentType, d.documenCategory,uid.isActive, u.id) from Users u join u.documents d join d.userImageDocuments uid WHERE u.id = :userId")
	List<DocumentDTO> findDocumentsByUserId(@Param("userId") Integer userId);

}