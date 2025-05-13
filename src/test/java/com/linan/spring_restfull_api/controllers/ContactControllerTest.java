package com.linan.spring_restfull_api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linan.spring_restfull_api.entity.Contact;
import com.linan.spring_restfull_api.entity.User;
import com.linan.spring_restfull_api.model.ContactResponse;
import com.linan.spring_restfull_api.model.CreateContactRequest;
import com.linan.spring_restfull_api.model.WebResponse;
import com.linan.spring_restfull_api.repository.ContactRepository;
import com.linan.spring_restfull_api.repository.UserRepository;
import com.linan.spring_restfull_api.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp (){
        contactRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("Test");
        user.setName("Test");
        user.setPassword(BCrypt.hashpw("Test", BCrypt.gensalt()));
        user.setToken("123");
        user.setTokenExpiredAt(System.currentTimeMillis() + 360000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequestFirstNameBlank () throws  Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setLastName("example");
        request.setEmail("myemail@example.com");
        request.setPhone("+905550587733");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "123")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<String>>() {
                                    });
                    assertNotNull(response.getErrors());
                }
        );

    }

    @Test
    void createContactBadRequestInvalidEmail () throws  Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Example");
        request.setLastName("example");
        request.setEmail("MyEmail");
        request.setPhone("+905550587733");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "123")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(
                result -> {
                    WebResponse<ContactResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<ContactResponse>>() {
                                    });
                    assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void createContactSuccess () throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Example");
        request.setLastName("example");
        request.setEmail("MyEmail@example.com");
        request.setPhone("+905550587733");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "123")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<ContactResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<ContactResponse>>() {
                                    });
                    assertNotNull(response);
                    assertEquals("Example", response.getData().getFirstName());
                    assertEquals("example", response.getData().getLastName());
                }
        );
    }

    @Test
    void contactNotFound () throws Exception{
        User user = userRepository.findFirstByToken("123")
                .orElseThrow();

        mockMvc.perform(
                get("/api/contacts/12387340")
                        .header("X-API-TOKEN", "123")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<String>>() {
                                    });
                    assertEquals("contact not found", response.getErrors());
                }
        );
    }

    @Test
    void contactFound () throws Exception{
        User user = userRepository.findById("Test")
                .orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("adi");
        contact.setLastName("Dayat");
        contact.setEmail("myemail@exmpl.com");
        contact.setPhone("1234433231");
        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/" + contact.getId())
                        .header("X-API-TOKEN", "123")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<ContactResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<ContactResponse>>() {
                                    });
                    assertNotNull(response);
                }
        );
    }

    @Test
    void updateFailed () throws Exception {
        User user = userRepository.findById("Test")
                .orElseThrow();
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Adi");
        request.setLastName("Hidayat");
        request.setEmail("adiexample@email.com");
        request.setPhone("019834");
        mockMvc.perform(
                put("/api/contacts/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "123")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<String>>() {
                                    });
                    assertEquals("contact not found", response.getErrors());
                }
        );
    }

    @Test
    void updateSuccess () throws Exception{
        User user = userRepository.findById("Test")
                .orElseThrow();
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Adi");
        contact.setLastName("Dayat");
        contact.setEmail("adi@email.com");
        contact.setPhone("0982380");

        contactRepository.save(contact);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Adiii");
        request.setLastName("Nseva");
        request.setEmail("adiexample@email.com");
        request.setPhone("019834");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "123")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<ContactResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<ContactResponse>>() {
                                    });
                    assertNotNull(response);
                    assertEquals("Adiii", response.getData().getFirstName());
                    assertEquals("Nseva", response.getData().getLastName());
                }
        );
    }

    @Test
    void deleteFailed () throws Exception{
        User user = userRepository.findById("Test")
                .orElseThrow();

        mockMvc.perform(
                delete("/api/contacts/12344")
                        .header("X-API-TOKEN", "123")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(
                result -> {
                    WebResponse<ContactResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<ContactResponse>>() {
                                    });
                    assertEquals("contact not found", response.getErrors());
                }
        );
    }

    @Test
    void deleteContactSuccess () throws Exception{
        User user = userRepository.findById("Test")
                .orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Nseva");
        contact.setLastName("Nseva");
        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "123")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<String>>() {
                                    });
                    assertEquals("OK", response.getData());
                }
        );
    }

    @Test
    void searchNotFound () throws Exception {
        mockMvc.perform(
                get("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "123")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<List<ContactResponse>>>() {
                                    });
                    assertNull(response.getErrors());
                    assertEquals(0, response.getData().size());
                    assertEquals(0, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                }
        );
    }

    @Test
    void searchByName () throws Exception {
        User user = userRepository.findById("Test")
                        .orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setUser(user);
            contact.setId(UUID.randomUUID().toString());
            contact.setFirstName("Nseva" + i);
            contact.setLastName("Nseva" + i);
            contact.setEmail("contact@gmail.com");
            contact.setPhone("012343" + i);
            contactRepository.save(contact);
        }
        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Nseva")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "123")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<List<ContactResponse>>>() {
                                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                }
        );
    }

}