
package com.ganesh.portfolio.service;

import java.util.List;

import com.ganesh.portfolio.entity.Contact;

public interface ContactService {

    Contact saveContact(Contact contact);
    List<Contact> getAllContacts();

}
