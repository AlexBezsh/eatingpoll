package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.controller.AbstractControllerTest;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.Objects;

import static com.javawebinar.eatingpoll.TestData.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends AbstractControllerTest {

    @Test
    public void userHomePage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/home")
                .sessionAttr("user", MOCK_USER1_DTO))
                .andExpect(status().isOk())
                .andExpect(view().name("userPage"))
                .andExpect(model().attribute("restaurants", hasSize(2)))
                .andExpect(model().attributeDoesNotExist("restaurant"))
                .andExpect(model().attribute("user", hasProperty("id", is(MOCK_USER1.getId()))));
    }

    @Test
    public void userVote_RestaurantShouldHaveOneVote() throws Exception {

        LocalTime votingFinish = LocalTime.of(
                Integer.parseInt(Objects.requireNonNull(environment.getProperty(PROP_VOTING_FINISH_HOUR))),
                Integer.parseInt(Objects.requireNonNull(environment.getProperty(PROP_VOTING_FINISH_MINUTE))));
        Assumptions.assumeTrue(LocalTime.now().isBefore(votingFinish));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/vote?restaurantId=" + MOCK_RESTAURANT1.getId())
                .sessionAttr("user", MOCK_USER2_DTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/home"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/home")
                .sessionAttr("user", MOCK_USER2_DTO))
                .andExpect(status().isOk())
                .andExpect(view().name("userPage"))
                .andExpect(model().attribute("restaurants", hasItem(
                        allOf(hasProperty("id", is(MOCK_RESTAURANT1.getId())),
                                hasProperty("votesCount", is(1))))));
    }
}
