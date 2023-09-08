package com.dev.DTO;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DocumentDTO implements Serializable {

	private static final long serialVersionUID = 1l;

	private String documentName;

	private String documentType;

	private String documentCategory;

	private Boolean isActive;

	private Integer userId;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DocumentDTO(String documentName, String documentType, String documentCategory, Boolean isActive,
			Integer userId) {
		super();
		this.documentName = documentName;
		this.documentType = documentType;
		this.documentCategory = documentCategory;
		this.isActive = isActive;
		this.userId = userId;
	}

	
}
