package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.controller.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.javawebinar.eatingpoll.TestData.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BasicProfilesControllerTest extends AbstractControllerTest {

    @Test
    public void startPage() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("users", hasSize(3)))
                .andExpect(model().attributeExists("userToLogin"));
    }

    @Test
    public void login() throws Exception {

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("user", MOCK_USER1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/home"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", hasProperty("id", is(MOCK_USER1_DTO.getId()))));
    }
}
