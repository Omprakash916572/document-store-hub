package com.dev.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "document")
public class Documents {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "document_category")
	private String documenCategory;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "create_ts")
	private Timestamp createTs;

	@Column(name = "update_ts")
	private Timestamp updateTs;

	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
	private Set<UserImageDocuments> userImageDocuments;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private Users users;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocumenCategory() {
		return documenCategory;
	}

	public void setDocumenCategory(String documenCategory) {
		this.documenCategory = documenCategory;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
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

	public Set<UserImageDocuments> getUserImageDocuments() {
		return userImageDocuments;
	}

	public void setUserImageDocuments(Set<UserImageDocuments> userImageDocuments) {
		this.userImageDocuments = userImageDocuments;
	}

}
