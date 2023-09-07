package com.dev.DTO;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserImageDocumentsDTO {

	@JsonProperty("name")
	private String name;

	@JsonProperty("document")
	private MultipartFile image;

	@JsonProperty("document_category")
	private String document_category;

	@JsonProperty("file_extension")
	private String file_extension;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getDocument_category() {
		return document_category;
	}

	public void setDocument_category(String document_category) {
		this.document_category = document_category;
	}

	public String getFile_extension() {
		return file_extension;
	}

	public void setFile_extension(String file_extension) {
		this.file_extension = file_extension;
	}

	@Override
	public String toString() {
		return "UserDocumentsDTO [name=" + name + ", image=" + image + ", document_category=" + document_category
				+ ", file_extension=" + file_extension + "]";
	}

}
