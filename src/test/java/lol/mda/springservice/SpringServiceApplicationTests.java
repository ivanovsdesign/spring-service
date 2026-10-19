package lol.mda.springservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SpringServiceApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
        void booksApiSupportsCrudMethods() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Clean Code"));

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.author").value("Robert C. Martin"));

        mockMvc.perform(post("/api/books")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"title":"Domain-Driven Design","author":"Eric Evans"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/books/3"))
            .andExpect(jsonPath("$.title").value("Domain-Driven Design"));

        mockMvc.perform(patch("/api/books/1")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"title":"Clean Code: Updated"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Clean Code: Updated"))
            .andExpect(jsonPath("$.author").value("Robert C. Martin"));

        mockMvc.perform(delete("/api/books/1"))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isNotFound());
    }

}
