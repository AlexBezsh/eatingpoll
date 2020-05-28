package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.controller.AbstractControllerTest;
import org.junit.jupiter.api.Test;

import static com.javawebinar.eatingpoll.TestData.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class AdminControllerTest extends AbstractControllerTest {

    @Test
    public void adminHomePage() throws Exception {
        mockMvc.perform(get("/admin/home?userEmail=" + MOCK_ADMIN1.getEmail() + "&userPassword=" + MOCK_ADMIN1.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPage"))
                .andExpect(model().attribute("restaurants", hasSize(2)))
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attribute("user", hasProperty("name", is(MOCK_ADMIN1.getName()))));
    }

    @Test
    public void getUsers() throws Exception {
        mockMvc.perform(get("/admin/users?userEmail=" + MOCK_ADMIN1.getEmail() + "&userPassword=" + MOCK_ADMIN1.getPassword()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasItem(
                        allOf( hasProperty("id", is(MOCK_USER1.getId())),
                                hasProperty("name", is(MOCK_USER1.getName())),
                                hasProperty("email", is(MOCK_USER1.getEmail())),
                                hasProperty("role", is(MOCK_USER1.getRole()))))))
                .andExpect(model().attribute("users", hasItem(
                        allOf( hasProperty("id", is(MOCK_USER2.getId())),
                                hasProperty("name", is(MOCK_USER2.getName())),
                                hasProperty("email", is(MOCK_USER2.getEmail())),
                                hasProperty("role", is(MOCK_USER2.getRole()))))));
    }

}
