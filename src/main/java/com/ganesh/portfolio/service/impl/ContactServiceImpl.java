
package com.ganesh.portfolio.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import com.ganesh.portfolio.entity.Contact;
import com.ganesh.portfolio.repository.ContactRepository;
import com.ganesh.portfolio.service.ContactService;
import com.ganesh.portfolio.service.EmailService;

@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger logger =
            LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;
    private final EmailService emailService;

    public ContactServiceImpl(
            ContactRepository contactRepository,
            EmailService emailService) {

        this.contactRepository = contactRepository;
        this.emailService = emailService;
    }

    @Override
    public Contact saveContact(Contact contact) {

        // 1. Save contact in MySQL
        Contact savedContact = contactRepository.save(contact);

        // 2. Attempt email notification
        try {
            emailService.sendContactNotification(savedContact);
            logger.info("Contact notification email sent successfully. ID: {}",
                    savedContact.getId());

        } catch (MailException ex) {
            logger.error("Contact saved, but email notification failed. ID: {}",
                    savedContact.getId(), ex);
        }

        return savedContact;
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAllByOrderByCreatedAtDesc();
    }
}
