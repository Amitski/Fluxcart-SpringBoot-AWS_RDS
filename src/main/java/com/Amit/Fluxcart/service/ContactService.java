package com.Amit.Fluxcart.service;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Amit.Fluxcart.entity.Contact;
import com.Amit.Fluxcart.repository.ContactRepository;

@Service
public class ContactService {
	
	@Autowired
    private ContactRepository contactRepository;
	
	
	public Contact consolidateContact(String email, String phoneNumber) {

        List<Contact> existingContacts = contactRepository.findPrimaryAndSecondaryContactsByEmailOrPhoneNumber(email, phoneNumber);
        
        if(existingContacts.isEmpty()) return addPrimaryContact(email, phoneNumber);
        
        List<Contact> primaryContacts = existingContacts.stream()
                .filter(contact -> contact.getLinkPrecedence() == Contact.LinkPrecedence.PRIMARY)
                .collect(Collectors.toList());

        List<Contact> secondaryContacts = existingContacts.stream()
                .filter(contact -> contact.getLinkPrecedence() == Contact.LinkPrecedence.SECONDARY)
                .collect(Collectors.toList());
        
        List<Integer> uniqueLinkedIds = secondaryContacts.stream()
        	    .map(Contact::getLinkedId) 
        	    .distinct()  
        	    .collect(Collectors.toList()); 

        
        int sizeOfPrimaryContacts = primaryContacts.size();
        int sizeOfuniqueLinkedIds = uniqueLinkedIds.size();
        
		
		if(email==null) {
			if(sizeOfPrimaryContacts>0) return primaryContacts.get(0);
			else return contactRepository.findById(uniqueLinkedIds.get(0)).orElse(null);
		}
		if(phoneNumber==null) {
			if(sizeOfPrimaryContacts>0) return primaryContacts.get(0);
			else return contactRepository.findById(uniqueLinkedIds.get(0)).orElse(null);
		}
		if(sizeOfPrimaryContacts==1){
        	
        	
        	if(sizeOfuniqueLinkedIds==0) {
        		if(!(primaryContacts.get(0).getEmail().equals(email) && primaryContacts.get(0).getPhoneNumber().equals(phoneNumber))) {
            		addSecondaryContact(email, phoneNumber, primaryContacts.get(0).getId());
            		return primaryContacts.get(0);
            	}
            	else {
            		primaryContacts.get(0).setUpdatedAt(LocalDateTime.now());
            		return contactRepository.save(primaryContacts.get(0));
            	}
        	}
		}
        
        return new Contact();
	}
        
        
        
        
        public Contact addPrimaryContact(String email, String phoneNumber) {
        	Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence(Contact.LinkPrecedence.PRIMARY);
            newContact.setLinkedId(null);
            newContact.setCreatedAt(LocalDateTime.now());
            return contactRepository.save(newContact);
        }
        
        public Contact addSecondaryContact(String email, String phoneNumber, Integer linkedId) {
        	Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence(Contact.LinkPrecedence.SECONDARY);
            newContact.setLinkedId(linkedId);
            newContact.setCreatedAt(LocalDateTime.now());
            return contactRepository.save(newContact);
        }

	
	public Map<String, Object> identifyContact(Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String phoneNumber = (String) requestBody.get("phoneNumber");

        Contact primaryContact = consolidateContact(email, phoneNumber);

        List<Contact> secondaryContacts = contactRepository
                .findContactsByLinkedIdAndLinkPrecedence(primaryContact.getId(), Contact.LinkPrecedence.SECONDARY);
        

        List<Integer> secondaryContactIds = secondaryContacts.stream()
                .map(Contact::getId)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("contact", Map.of(
                "primaryContactId", primaryContact.getId(),
                "emails", getAllEmails(primaryContact, secondaryContacts),
                "phoneNumbers", getAllPhoneNumbers(primaryContact, secondaryContacts),
                "secondaryContactIds", secondaryContactIds
        ));
        
        

        return response;
    }
	
	private List<String> getAllEmails(Contact primaryContact, List<Contact> secondaryContacts) {

        HashSet<String> uniqueEmails = new HashSet<>();

        uniqueEmails.addAll(secondaryContacts.stream()
                .map(Contact::getEmail)
                .filter(email -> email != null)
                .collect(Collectors.toList()));


        List<String> uniqueEmailList = new ArrayList<>(uniqueEmails);
        if(primaryContact!=null) {
	        if(primaryContact.getEmail() != null && uniqueEmailList.contains(primaryContact.getEmail())) {
	        	uniqueEmailList.remove(primaryContact.getEmail());
	        	uniqueEmailList.add(0, primaryContact.getEmail());
	        }
	        else if(primaryContact.getEmail() != null && !uniqueEmailList.contains(primaryContact.getEmail())) {
	        	uniqueEmailList.add(0, primaryContact.getEmail());
	        }
        }
        
        return uniqueEmailList;
    }

    private List<String> getAllPhoneNumbers(Contact primaryContact, List<Contact> secondaryContacts) {

        HashSet<String> uniquePhoneNumbers = new HashSet<>();

        uniquePhoneNumbers.addAll(secondaryContacts.stream()
                .map(Contact::getPhoneNumber)
                .filter(phoneNumber -> phoneNumber != null)
                .collect(Collectors.toList()));

        List<String> uniquePhoneNumberList = new ArrayList<>(uniquePhoneNumbers);
        if(primaryContact!=null) {
	        if(uniquePhoneNumberList.contains(primaryContact.getPhoneNumber()) && primaryContact.getPhoneNumber() != null) {
	        	uniquePhoneNumberList.remove(primaryContact.getPhoneNumber());
	        	uniquePhoneNumberList.add(0, primaryContact.getPhoneNumber());
	        }
	        else if(primaryContact.getPhoneNumber() != null && !uniquePhoneNumberList.contains(primaryContact.getPhoneNumber())) {
	        	uniquePhoneNumberList.add(0, primaryContact.getPhoneNumber());
	        }
        }
        
        return uniquePhoneNumberList;
    }
    
}