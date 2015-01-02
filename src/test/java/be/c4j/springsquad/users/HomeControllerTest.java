package be.c4j.springsquad.users;

import be.c4j.springsquad.AppConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = AppConfig.class)
public class HomeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void showHomeDefaultJsp() throws Exception {
        ResultActions result = mockMvc.perform(get("/"));

        assertView(result, "jsp");
    }

    @Test
    public void showHomeJspResolver() throws Exception {
        ResultActions result = mockMvc.perform(get("/").param("viewResolver", "jsp"));

        assertView(result, "jsp");
    }

    @Test
    public void showHomeThymeleafResolver() throws Exception {
        ResultActions result = mockMvc.perform(get("/").param("viewResolver", "thymeleaf"));

        assertView(result, "thymeleaf");
    }


    private void assertView(ResultActions result, String viewResolver) throws Exception {
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("name", "Spring Squad"))
                .andExpect(view().name(viewResolver + "/home"));
    }
}