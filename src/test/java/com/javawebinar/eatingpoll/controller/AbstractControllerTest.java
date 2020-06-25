package com.javawebinar.eatingpoll.controller;

import com.javawebinar.eatingpoll.config.AppConfig;
import com.javawebinar.eatingpoll.config.InitConfig;
import com.javawebinar.eatingpoll.config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitWebConfig(classes = {AppConfig.class, InitConfig.class, WebConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // reinitializing database before every test
public abstract class AbstractControllerTest {

    protected static final String PROP_VOTING_FINISH_HOUR = "voting.finish.hour";
    protected static final String PROP_VOTING_FINISH_MINUTE = "voting.finish.minute";

    protected Environment environment;

    public WebApplicationContext context;
    public MockMvc mockMvc;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Autowired
    public void setContext(WebApplicationContext context) {
        this.context = context;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}