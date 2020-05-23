package com.javawebinar.eatingpoll;

import com.javawebinar.eatingpoll.config.AppConfig;
import com.javawebinar.eatingpoll.config.InitConfig;
import com.javawebinar.eatingpoll.config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringJUnitWebConfig(classes = {AppConfig.class, InitConfig.class, WebConfig.class})
public abstract class AbstractControllerTest {

    public WebApplicationContext context;
    public MockMvc mockMvc;

    @Autowired
    public void setContext(WebApplicationContext context) {
        this.context = context;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
