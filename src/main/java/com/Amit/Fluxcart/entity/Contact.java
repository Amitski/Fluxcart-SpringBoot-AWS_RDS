package com.Amit.Fluxcart.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String phoneNumber;
	
	private String email;
	

	private Integer linkedId;
	
	@Enumerated(EnumType.STRING)
    private LinkPrecedence linkPrecedence;

    public enum LinkPrecedence {
        PRIMARY, SECONDARY
    }
	
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
    
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;
	
	 @PrePersist
	    public void prePersist() {
	        LocalDateTime currentTimestamp = LocalDateTime.now();
	        createdAt = currentTimestamp;
	        updatedAt = currentTimestamp;
	    }
	    
	 @PreUpdate
	    public void preUpdate() {
	        updatedAt = LocalDateTime.now();
	    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLinkedId() {
		return linkedId;
	}

	public void setLinkedId(Integer linkedId) {
		this.linkedId = linkedId;
	}

	public LinkPrecedence getLinkPrecedence() {
		return linkPrecedence;
	}

	public void setLinkPrecedence(LinkPrecedence linkPrecedence) {
		this.linkPrecedence = linkPrecedence;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
	 
	 

}
