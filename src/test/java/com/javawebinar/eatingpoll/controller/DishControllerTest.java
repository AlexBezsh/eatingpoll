package com.javawebinar.eatingpoll.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javawebinar.eatingpoll.TestData.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

public class DishControllerTest extends AbstractControllerTest {

    @Test
    public void deleteDish_ShouldBeOneInsteadOfTwo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin/dish/delete?dishId=" + MOCK_DISH1.getId())
                .sessionAttr("user", MOCK_ADMIN1_DTO))
                .andExpect(redirectedUrl("/admin/home"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/home")
                .sessionAttr("user", MOCK_USER1_DTO))
                .andExpect(model().attribute("restaurants", hasItem(
                        allOf(hasProperty("id", is(MOCK_RESTAURANT1.getId())),
                                hasProperty("dishes", hasSize(1))))));
    }

    @Test
    public void createNewDish_ShouldBeThreeInsteadOfTwo() throws Exception {
        mockMvc.perform(post("/admin/dish/save?restaurantId=" + MOCK_RESTAURANT1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("dish", ADDITIONAL_MOCK_DISH))
                .andExpect(redirectedUrl("/admin/home"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/home")
                .sessionAttr("user", MOCK_USER1_DTO))
                .andExpect(model().attribute("restaurants", hasItem(
                        allOf(hasProperty("id", is(MOCK_RESTAURANT1.getId())),
                                hasProperty("dishes", hasSize(3)),
                                hasProperty("dishes", hasItem(hasProperty("name", is(ADDITIONAL_MOCK_DISH.getName()))))))));
    }
}
