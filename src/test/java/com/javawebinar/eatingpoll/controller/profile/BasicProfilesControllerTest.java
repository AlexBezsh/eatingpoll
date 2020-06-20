package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.controller.AbstractControllerTest;
import com.javawebinar.eatingpoll.model.user.User;
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
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void login() throws Exception {
        User user = MOCK_USER1;
        user.setPassword(MOCK_USER1.getPassword());

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("userPage"))
                .andExpect(model().attribute("restaurants", hasSize(2)))
                .andExpect(model().attributeDoesNotExist("restaurant"))
                .andExpect(model().attribute("user", hasProperty("id", is(MOCK_USER1.getId()))));
    }
}
