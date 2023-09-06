package com.Amit.Fluxcart.service;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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