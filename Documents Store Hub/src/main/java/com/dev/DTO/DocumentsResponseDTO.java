package com.dev.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DocumentsResponseDTO {

	private Integer totalDocuments;
	private List<DocumentDTO> documents;

	public Integer getTotalDocuments() {
		return totalDocuments;
	}

	public void setTotalDocuments(Integer totalDocuments) {
		this.totalDocuments = totalDocuments;
	}

	public List<DocumentDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentDTO> documents) {
		this.documents = documents;
	}

}