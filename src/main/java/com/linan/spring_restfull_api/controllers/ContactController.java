package com.linan.spring_restfull_api.controllers;

import com.linan.spring_restfull_api.entity.Contact;
import com.linan.spring_restfull_api.entity.User;
import com.linan.spring_restfull_api.model.*;
import com.linan.spring_restfull_api.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public WebResponse<ContactResponse> create (User user, @RequestBody CreateContactRequest request){
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> get (User user, @PathVariable("contactId") String id){
        ContactResponse contactResponse = contactService.get(user, id);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();

    }

    @PutMapping(
            path = "/api/contacts/{contactId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> update (User user,
                                                @RequestBody CreateContactRequest request,
                                                @PathVariable("contactId") String id){
        ContactResponse contactResponse = contactService.update(user, id, request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete (User user, @PathVariable("contactId") String contactId){
        contactService.delete(user, contactId);
        return WebResponse.<String >builder()
                .data("OK")
                .build();
    }

    @GetMapping(
            path = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public WebResponse<List<ContactResponse>> search (User user,
                                                      @RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "email", required = false)String email,
                                                      @RequestParam(value = "phone", required = false)String phone,
                                                      @RequestParam(value = "page", required = false, defaultValue = "0")Integer page,
                                                      @RequestParam(value = "size", required = false, defaultValue = "10")Integer size){
        SearchContactRequest request = SearchContactRequest.builder()
                .page(page)
                .size(size)
                .email(email)
                .phone(phone)
                .name(name)
                .build();

        Page<ContactResponse> contactResponses = contactService.search(user, request);
        return WebResponse.<List<ContactResponse>>builder()
                .data(contactResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(contactResponses.getNumber())
                        .totalPage(contactResponses.getTotalPages())
                        .size(contactResponses.getSize())
                        .build())
                .build();

    }
}
