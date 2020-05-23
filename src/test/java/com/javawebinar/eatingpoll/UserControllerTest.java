package com.javawebinar.eatingpoll;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends AbstractControllerTest {

    @Test
    public void getMockUsers() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("users", hasSize(3)));
    }

    @Test
    public void mainUserPage() throws Exception {
        mockMvc.perform(get("/voting?userId=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("userPage"))
                .andExpect(model().attribute("restaurants", hasSize(2)));
    }

    @Test
    public void mainAdminPage() throws Exception {
        mockMvc.perform(get("/voting?userId=3"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPage"))
                .andExpect(model().attribute("restaurants", hasSize(2)))
                .andExpect(model().attribute("user", hasProperty("name", is("Admin1"))));
    }
}
