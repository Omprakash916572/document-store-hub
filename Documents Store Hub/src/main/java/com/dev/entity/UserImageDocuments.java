package com.dev.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity(name = "user_image_documents")
public class UserImageDocuments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "orignal_document_name")
	private String orignalDocumentName;

	@Column(name = "local_storage_image_document_name")
	private String localStorageImageDocumentName;

	@Column(name = "local_storage_pdf_document_name")
	private String localStoragePdfDocumentName;

	@Column(name = "image_document_data")
	@Type(type = "text")
	private String imageDocumentData;

	@Column(name = "pdf_document_data")
	@Type(type = "text")
	private String pdfDocumentData;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "create_ts")
	private Timestamp createTs;

	@Column(name = "update_ts")
	private Timestamp updateTs;
	
	@ManyToOne
	@JoinColumn(name = "users_id")
	private Users users;

	@ManyToOne
	@JoinColumn(name = "document_id")
	private Documents document;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrignalDocumentName() {
		return orignalDocumentName;
	}

	public void setOrignalDocumentName(String orignalDocumentName) {
		this.orignalDocumentName = orignalDocumentName;
	}

	public String getLocalStorageImageDocumentName() {
		return localStorageImageDocumentName;
	}

	public void setLocalStorageImageDocumentName(String localStorageImageDocumentName) {
		this.localStorageImageDocumentName = localStorageImageDocumentName;
	}

	public String getLocalStoragePdfDocumentName() {
		return localStoragePdfDocumentName;
	}

	public void setLocalStoragePdfDocumentName(String localStoragePdfDocumentName) {
		this.localStoragePdfDocumentName = localStoragePdfDocumentName;
	}

	public String getImageDocumentData() {
		return imageDocumentData;
	}

	public void setImageDocumentData(String imageDocumentData) {
		this.imageDocumentData = imageDocumentData;
	}

	public String getPdfDocumentData() {
		return pdfDocumentData;
	}

	public void setPdfDocumentData(String pdfDocumentData) {
		this.pdfDocumentData = pdfDocumentData;
	}

	public Timestamp getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}

	public Timestamp getUpdateTs() {
		return updateTs;
	}

	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Documents getDocument() {
		return document;
	}

	public void setDocument(Documents document) {
		this.document = document;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
}
