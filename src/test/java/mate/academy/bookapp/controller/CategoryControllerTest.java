package mate.academy.bookapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookapp.dto.category.CategoryDto;
import mate.academy.bookapp.dto.category.CategoryRequestDto;
import mate.academy.bookapp.repository.category.CategoryRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/add-default-category.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/remove-all-categories.sql")
            );
        }
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get all categories: returns all categories")
    public void getAll_ReturnsCategories_Success() throws Exception {

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("content");
        CategoryDto[] categories = objectMapper.treeToValue(contentNode, CategoryDto[].class);

        assertEquals(1, categories.length);
        assertEquals("Fiction", categories[0].getName());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/reset-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    @DisplayName("Create category: creates new category")
    public void create_ValidRequest_Success() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("Science")
                .setDescription("Science books");

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);

        assertNotNull(response.getId());
        assertEquals("Science", response.getName());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category: updates existing category")
    @Sql(scripts = "classpath:database/category/reset-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void update_ValidId_Success() throws Exception {
        Long categoryId = 2L;
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("Updated Name")
                .setDescription("Updated Description");

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);

        assertEquals(categoryId, response.getId());
        assertEquals("Updated Name", response.getName());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category: deletes existing category")
    @Sql(scripts = "classpath:database/category/reset-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void delete_ValidId_Success() throws Exception {
        Long categoryId = 2L;

        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent());

        assertFalse(categoryRepository.existsById(categoryId));
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get category by ID: returns existing category")
    public void getById_ValidId_Success() throws Exception {
        Long categoryId = 1L;

        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);

        assertEquals(categoryId, response.getId());
        assertEquals("Fiction", response.getName());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get category by ID: returns not found for invalid ID")
    public void getById_InvalidId_NotFound() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(get("/categories/{id}", invalidId))
                .andExpect(status().isNotFound());
    }
}