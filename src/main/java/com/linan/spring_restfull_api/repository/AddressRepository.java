package com.linan.spring_restfull_api.repository;

import com.linan.spring_restfull_api.entity.Address;
import com.linan.spring_restfull_api.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String> {
    Optional<Address> findFirstByContactAndId (Contact contact, String id);
    List<Address> findAllByContact (Contact contact);
}
