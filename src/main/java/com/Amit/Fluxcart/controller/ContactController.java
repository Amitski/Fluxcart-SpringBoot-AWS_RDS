package com.Amit.Fluxcart.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Amit.Fluxcart.service.ContactService;

@RestController
public class ContactController {
	
	@Autowired
    private ContactService contactService;

    @PostMapping("/identify")
    public ResponseEntity<Map<String, Object>> identifyContact(@RequestBody Map<String, Object> requestBody) {
            Map<String, Object> response = contactService.identifyContact(requestBody);
            return ResponseEntity.ok(response);
    }
}
