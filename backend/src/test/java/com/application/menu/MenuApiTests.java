package com.application.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MenuApiTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void publicMenuList_shouldReturn200() throws Exception {
        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void createMenu_withoutLogin_shouldReturn403() throws Exception {
        String body = """
            {
              "nameTr":"Karides Güveç",
              "nameEn":"Shrimp Casserole",
              "nameDe":"Garnelenauflauf",
              "nameFr":"Cocotte de crevettes",
              "price":250,
              "category":"Ara Sıcak"
            }
            """;
        mockMvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void createMenu_withAdminLogin_shouldReturn201() throws Exception {
        // 1) login
        var loginResult = mockMvc.perform(formLogin("/login")
                        .user("admin")
                        .password("admin"))
                .andExpect(authenticated().withUsername("admin"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        // 2) authorized POST
        String body = """
            {
              "nameTr":"Levrek Izgara",
              "nameEn":"Grilled Sea Bass",
              "nameDe":"Gegrillter Wolfsbarsch",
              "nameFr":"Bar grillé",
              "price":320,
              "category":"Ana Yemek"
            }
            """;

        mockMvc.perform(post("/menu")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nameTr").value("Levrek Izgara"));
    }

    @Test
    void cors_preflight_shouldReturnAllowHeaders() throws Exception {
        mockMvc.perform(options("/menu")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Vary", org.hamcrest.Matchers.containsString("Origin")));
    }
}
