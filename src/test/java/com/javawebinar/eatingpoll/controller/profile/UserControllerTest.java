package com.javawebinar.eatingpoll.controller.profile;

import com.javawebinar.eatingpoll.controller.AbstractControllerTest;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static com.javawebinar.eatingpoll.TestData.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends AbstractControllerTest {

    @Test
    public void userHomePage() throws Exception {
        mockMvc.perform(get("/user/home?userEmail=" + MOCK_USER1.getEmail() + "&userPassword=" + MOCK_USER1.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("userPage"))
                .andExpect(model().attribute("restaurants", hasSize(2)))
                .andExpect(model().attributeDoesNotExist("restaurant"))
                .andExpect(model().attribute("user", hasProperty("name", is(MOCK_USER1.getName()))));
    }

    @Test
    public void userVote_RestaurantShouldHaveOneVote() throws Exception {
        LocalTime votingFinish = LocalTime.of(Integer.parseInt(env.getProperty(PROP_VOTING_FINISH_HOUR)), Integer.parseInt(env.getProperty(PROP_VOTING_FINISH_MINUTE)));
        Assumptions.assumeTrue(LocalTime.now().isBefore(votingFinish));

        mockMvc.perform(get("/user/vote?restaurantId=" + MOCK_RESTAURANT1.getId() + "&userEmail=" + MOCK_USER2.getEmail() + "&userPassword=" + MOCK_USER2.getPassword()))
                .andExpect(redirectedUrl("/user/home?userEmail=" + MOCK_USER2.getEmail() + "&userPassword=" + MOCK_USER2.getPassword()));

        mockMvc.perform(get("/user/home?userEmail=" + MOCK_USER2.getEmail() + "&userPassword=" + MOCK_USER2.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("userPage"))
                .andExpect(model().attribute("restaurants", hasItem(
                        allOf(hasProperty("id", is(MOCK_RESTAURANT1.getId())),
                                hasProperty("votesCount", is(1))))));
    }
}
