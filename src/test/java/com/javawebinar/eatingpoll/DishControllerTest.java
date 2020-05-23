package com.javawebinar.eatingpoll;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DishControllerTest extends AbstractControllerTest {

    @Test
    public void deleteDish() throws Exception {
        mockMvc.perform(get("/dish/delete?dishId=6&userId=3"))
                .andExpect(redirectedUrl("/voting?userId=3"));
    }
}
