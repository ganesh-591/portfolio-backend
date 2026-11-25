
package com.ganesh.portfolio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.ganesh.portfolio.dto.ContactRequestDTO;
import com.ganesh.portfolio.dto.ContactResponseDTO;
import com.ganesh.portfolio.entity.Contact;
import com.ganesh.portfolio.service.ContactService;

@RestController
@RequestMapping("/api/v1/contact")
@CrossOrigin(origins = {
	    "http://localhost:5173",
	    "https://ganeshkopparthi.vercel.app"
	})
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // POST - Submit contact form
    @PostMapping
    public ResponseEntity<ContactResponseDTO> createContact(
            @Valid @RequestBody ContactRequestDTO request) {

        Contact contact = new Contact();

        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setSubject(request.getSubject());
        contact.setMessage(request.getMessage());

        Contact savedContact = contactService.saveContact(contact);

        ContactResponseDTO response = new ContactResponseDTO(
                savedContact.getId(),
                savedContact.getName(),
                savedContact.getEmail(),
                savedContact.getSubject(),
                savedContact.getMessage(),
                savedContact.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET - Retrieve all contact submissions
    @GetMapping
    public ResponseEntity<List<ContactResponseDTO>> getAllContacts() {

        List<ContactResponseDTO> contacts =
                contactService.getAllContacts()
                        .stream()
                        .map(contact -> new ContactResponseDTO(
                                contact.getId(),
                                contact.getName(),
                                contact.getEmail(),
                                contact.getSubject(),
                                contact.getMessage(),
                                contact.getCreatedAt()
                        ))
                        .toList();

        return ResponseEntity.ok(contacts);
    }
}
