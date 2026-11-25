
package com.ganesh.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ganesh.portfolio.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findAllByOrderByCreatedAtDesc();

}
