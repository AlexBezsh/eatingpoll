package com.javawebinar.eatingpoll.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;

import static com.javawebinar.eatingpoll.TestData.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    @Transactional
    public void deleteRestaurant() throws Exception {
        mockMvc.perform(get("/admin/restaurant/delete?restaurantId=" + MOCK_RESTAURANT2.getId() + "&userEmail=" + MOCK_ADMIN1.getEmail() + "&userPassword=" + MOCK_ADMIN1.getPassword()))
                .andExpect(redirectedUrl("/admin/home?userEmail=" + MOCK_ADMIN1.getEmail() + "&userPassword=" + MOCK_ADMIN1.getPassword()));
    }

    @Test
    public void saveNewRestaurant_ShouldBeThreeInsteadOfTwo() throws Exception {
        mockMvc.perform(post("/admin/restaurant/save?userEmail=" + MOCK_ADMIN1.getEmail() + "&userPassword=" + MOCK_ADMIN1.getPassword())
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("restaurant", ADDITIONAL_MOCK_RESTAURANT))
                .andExpect(redirectedUrl("/admin/home?userEmail=" + MOCK_ADMIN1.getEmail() + "&userPassword=" + MOCK_ADMIN1.getPassword()));

        mockMvc.perform(get("/user/home?userEmail=" + MOCK_USER2.getEmail() + "&userPassword=" + MOCK_USER2.getPassword()))
                .andExpect(model().attribute("restaurants", hasSize(3)))
                .andExpect(model().attribute("restaurants", hasItem(hasProperty("name", is(ADDITIONAL_MOCK_RESTAURANT.getName())))));
    }
}
