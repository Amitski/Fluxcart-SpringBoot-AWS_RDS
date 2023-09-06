package com.Amit.Fluxcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.Amit.Fluxcart.entity.Contact;
import com.Amit.Fluxcart.entity.Contact.LinkPrecedence;


public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	Contact findByEmailOrPhoneNumber(String email, String phoneNumber);
	
	List<Contact> findContactsByLinkedIdAndLinkPrecedence(Integer linkedId, LinkPrecedence linkPrecedence);
	
	List<Contact> findContactsByLinkedId(Integer linkedId);

	@Query("SELECT c FROM Contact c WHERE (c.email = :email OR c.phoneNumber = :phoneNumber) " +
	           "AND c.linkPrecedence = :linkPrecedence AND c.linkPrecedence <> 'PRIMARY'")
	    List<Contact> findSecondaryContactsByEmailOrPhoneNumberAndLinkPrecedence(
	        @Param("email") String email,
	        @Param("phoneNumber") String phoneNumber,
	        @Param("linkPrecedence") LinkPrecedence linkPrecedence
	    );

    @Query("SELECT c FROM Contact c WHERE (c.email = :email OR c.phoneNumber = :phoneNumber) AND (c.linkPrecedence = 'PRIMARY' OR c.linkPrecedence = 'SECONDARY')")
    List<Contact> findPrimaryAndSecondaryContactsByEmailOrPhoneNumber(
    		@Param("email") String email, 
    		@Param("phoneNumber") String phoneNumber);

}
