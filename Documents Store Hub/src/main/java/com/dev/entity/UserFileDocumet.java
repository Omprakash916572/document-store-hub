package com.dev.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

//@Entity(name = "user_file_documents")
public class UserFileDocumet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "document_category")
	private String documenCategory;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "orignal_file_document_name")
	private String orignalFileDocumentName;

	@Column(name = "local_storage_file_document_name")
	private String localStorageFileDocumentName;

	@Column(name = "image_document_data")
	@Type(type = "text")
	private String fileDocumentData;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "create_ts")
	private Timestamp createTs;

	@Column(name = "update_ts")
	private Timestamp updateTs;
	
	@ManyToOne
	@JoinColumn(name = "document_id")
	private Documents document;
	
	

}
