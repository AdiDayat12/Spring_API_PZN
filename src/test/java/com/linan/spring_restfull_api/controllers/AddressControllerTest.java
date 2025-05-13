package com.linan.spring_restfull_api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linan.spring_restfull_api.entity.Address;
import com.linan.spring_restfull_api.entity.Contact;
import com.linan.spring_restfull_api.entity.User;
import com.linan.spring_restfull_api.model.AddressResponse;
import com.linan.spring_restfull_api.model.CreateAddressRequest;
import com.linan.spring_restfull_api.model.UpdateAddressRequest;
import com.linan.spring_restfull_api.model.WebResponse;
import com.linan.spring_restfull_api.repository.AddressRepository;
import com.linan.spring_restfull_api.repository.ContactRepository;
import com.linan.spring_restfull_api.repository.UserRepository;
import com.linan.spring_restfull_api.security.BCrypt;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp (){
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("Test");
        user.setName("Test");
        user.setPassword(BCrypt.hashpw("Test", BCrypt.gensalt()));
        user.setToken("123");
        user.setTokenExpiredAt(System.currentTimeMillis() + 360000L);
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId("Test");
        contact.setFirstName("adi");
        contact.setLastName("Dayat");
        contact.setEmail("myemail@exmpl.com");
        contact.setPhone("1234433231");
        contactRepository.save(contact);
    }

    @Test
    void createAddressFailed () throws Exception{
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");
        mockMvc.perform(
                post("/api/contacts/test/addresses")
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
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
    void createAddressSuccess () throws Exception{
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("Indonesia");

        mockMvc.perform(
                post("/api/contacts/Test/addresses")
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<AddressResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<AddressResponse>>() {
                                    });
                    assertNull(response.getErrors());
                    assertEquals(request.getCountry(), response.getData().getCountry());
                }
        );
    }

    @Test
    void getAddressNotFound () throws Exception{
        mockMvc.perform(
                get("/api/contacts/test/addresses/123")
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
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
    void getAddressFound () throws Exception{
        Address address = new Address();
        address.setCountry("Indonesia");
        address.setStreet("jln1231");
        address.setCity("Pamekasan");
        address.setContact(contactRepository.findById("Test").orElseThrow());
        address.setId(UUID.randomUUID().toString());
        addressRepository.save(address);

        mockMvc.perform(
                get("/api/contacts/Test/addresses/" + address.getId())
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<AddressResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<AddressResponse>>() {
                                    });
                    assertNull(response.getErrors());
                    assertEquals(address.getId(), response.getData().getId());
                }
        );
    }

    @Test
    void updateAddressFailed () throws Exception{
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");
        mockMvc.perform(
                put("/api/contacts/Test/addresses/123")
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
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
    void updateAddressSuccess () throws Exception{
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("Indonesia");
        request.setProvince("Jatim");
        request.setCity("Waru");
        request.setStreet("Palalang");
        request.setPostalCode("123");

        Address address = new Address();
        address.setCountry("Indonesia");
        address.setStreet("jln1231");
        address.setCity("Pamekasan");
        address.setContact(contactRepository.findById("Test").orElseThrow());
        address.setId(UUID.randomUUID().toString());
        addressRepository.save(address);

        mockMvc.perform(
                put("/api/contacts/Test/addresses/" + address.getId())
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<AddressResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<AddressResponse>>() {
                                    });
                    assertNull(response.getErrors());
                    assertEquals(request.getCountry(), response.getData().getCountry());
                }
        );
    }

    @Test
    void deleteAddressNotFound () throws Exception{
        mockMvc.perform(
                delete("/api/contacts/test/addresses/123")
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
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
    void deleteAddressSuccess () throws Exception{
        Address address = new Address();
        address.setContact(contactRepository.findById("Test").orElseThrow());
        address.setId("Test123");
        address.setCountry("Indonesia");
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/Test/addresses/Test123")
                        .header("X-API-TOKEN", "123")
                        .accept(MediaType.APPLICATION_JSON)

        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<WebResponse<String>>() {
                                    });
                    assertEquals("OK", response.getData());
                    assertFalse(addressRepository.existsById("Test123"));
                }
        );
    }

    @Test
    void listAddressNotFound () throws Exception{
        mockMvc.perform(
                get("/api/contacts/test/addresses")
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
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
    void listAddressFound () throws Exception{
        for (int i = 0; i < 10; i++) {
            Address address = new Address();
            address.setCountry("Indonesia");
            address.setStreet("jln1231");
            address.setCity("Pamekasan");
            address.setContact(contactRepository.findById("Test").orElseThrow());
            address.setId(UUID.randomUUID().toString());
            addressRepository.save(address);
        }

        mockMvc.perform(
                get("/api/contacts/Test/addresses")
                        .header("X-API-TOKEN", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<AddressResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(),
                                    new TypeReference<>() {
                                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                }
        );
    }
}