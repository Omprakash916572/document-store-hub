package com.dev.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserImageDocumentsResponseDTO {

	private Integer id;
	private String document_name;
	private String document_category;
	private String imageData;
	private String document_status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocument_name() {
		return document_name;
	}

	public void setDocument_name(String document_name) {
		this.document_name = document_name;
	}

	public String getDocument_category() {
		return document_category;
	}

	public void setDocument_category(String document_category) {
		this.document_category = document_category;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public String getDocument_status() {
		return document_status;
	}

	public void setDocument_status(String document_status) {
		this.document_status = document_status;
	}

}
