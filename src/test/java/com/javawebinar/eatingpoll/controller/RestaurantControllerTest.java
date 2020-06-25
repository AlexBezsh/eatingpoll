package com.javawebinar.eatingpoll.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static com.javawebinar.eatingpoll.TestData.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    @Transactional
    public void deleteRestaurant_ShouldBeOneInsteadOfTwo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin/restaurant/delete?restaurantId=" + MOCK_RESTAURANT2.getId())
                .sessionAttr("user", MOCK_ADMIN1_DTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/home"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin/home")
                .sessionAttr("user", MOCK_ADMIN1_DTO))
                .andExpect(status().isOk())
                .andExpect(model().attribute("restaurants", hasSize(1)));
    }

    @Test
    public void saveNewRestaurant_ShouldBeThreeInsteadOfTwo() throws Exception {
        mockMvc.perform(post("/admin/restaurant/save")
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("restaurant", ADDITIONAL_MOCK_RESTAURANT))
                .andExpect(redirectedUrl("/admin/home"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/home")
                .sessionAttr("user", MOCK_USER1_DTO))
                .andExpect(model().attribute("restaurants", hasSize(3)))
                .andExpect(model().attribute("restaurants", hasItem(hasProperty("name", is(ADDITIONAL_MOCK_RESTAURANT.getName())))));
    }
}
