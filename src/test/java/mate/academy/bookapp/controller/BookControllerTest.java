package mate.academy.bookapp.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookapp.dto.book.BookDto;
import mate.academy.bookapp.dto.book.CreateBookRequestDto;
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
public class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                    new ClassPathResource("database/books/add-three-default-books.sql")
            );
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
                    new ClassPathResource("database/books/remove-all-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/remove-all-categories.sql")
            );
        }
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Find all books: returns all books successfully")
    public void findAll_GivenBooks_SuccessAndReturnAllBooks() throws Exception {
        List<BookDto> expectedBookDto = new ArrayList<>();

        expectedBookDto.add(new BookDto()
                .setId(1L)
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9780743273565")
                .setPrice(new BigDecimal("12.99"))
                .setDescription("A story of wealth, love, and the American Dream in the 1920s.")
                .setCoverImage("https://example.com/image1.jpg")
                .setCategoryIds(List.of()));

        expectedBookDto.add(new BookDto()
                .setId(2L)
                .setTitle("To Kill a Mockingbird")
                .setAuthor("Harper Lee")
                .setIsbn("9780061120084")
                .setPrice(new BigDecimal("10.50"))
                .setDescription("A powerful story of racial injustice and moral growth in the American South.")
                .setCoverImage("https://example.com/image2.jpg")
                .setCategoryIds(List.of()));

        expectedBookDto.add(new BookDto()
                .setId(3L)
                .setTitle("1984")
                .setAuthor("George Orwell")
                .setIsbn("9780451524935")
                .setPrice(new BigDecimal("9.99"))
                .setDescription("A dystopian novel about totalitarianism and surveillance society.")
                .setCoverImage("https://example.com/image3.jpg")
                .setCategoryIds(List.of()));

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("content");

        BookDto[] actual = objectMapper.treeToValue(contentNode, BookDto[].class);

        assertEquals(3, actual.length);
        for (int i = 0; i < expectedBookDto.size(); i++) {
            BookDto expected = expectedBookDto.get(i);
            BookDto actualDto = actual[i];

            assertEquals(expected.getId(), actualDto.getId());
            assertEquals(expected.getTitle(), actualDto.getTitle());
            assertEquals(expected.getAuthor(), actualDto.getAuthor());
            assertEquals(expected.getIsbn(), actualDto.getIsbn());

            BigDecimal expectedPrice = expected.getPrice().stripTrailingZeros();
            BigDecimal actualPrice = actualDto.getPrice().stripTrailingZeros();
            assertEquals(expectedPrice, actualPrice);

            assertEquals(expected.getDescription(), actualDto.getDescription());
            assertEquals(expected.getCoverImage(), actualDto.getCoverImage());
            assertEquals(expected.getCategoryIds(), actualDto.getCategoryIds());
        }
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Find book by ID: returns book successfully for valid ID")
    public void findById_ValidBookId_SuccessAndReturnBookDto() throws Exception {
        Long validBookId = 1L;
        BookDto expectedBookDto = new BookDto()
                .setId(1L)
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9780743273565")
                .setPrice(new BigDecimal("12.99"))
                .setDescription("A story of wealth, love, and the American Dream in the 1920s.")
                .setCoverImage("https://example.com/image1.jpg")
                .setCategoryIds(List.of());

        MvcResult result = mockMvc.perform(get("/books/{id}", validBookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualDto = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertEquals(expectedBookDto, actualDto);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books/remove-dune-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Create book: creates and returns book successfully for valid request")
    public void create_ValidRequestDto_SuccessAndReturnBookDto() throws Exception {

        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Dune")
                .setAuthor("Frank Herbert")
                .setIsbn("9780441172719")
                .setPrice(new BigDecimal("49.49"))
                .setDescription("A book about a young heir leads a rebellion "
                        + "on a spice-rich desert planet.")
                .setCoverImage("https://example.com/dune-cover.jpg")
                .setCategoryIds(List.of(1L));

        BookDto expectedDto = new BookDto()
                .setTitle("Dune")
                .setAuthor("Frank Herbert")
                .setIsbn("9780441172719")
                .setPrice(new BigDecimal("49.49"))
                .setDescription("A book about a young heir leads a rebellion "
                        + "on a spice-rich desert planet.")
                .setCoverImage("https://example.com/dune-cover.jpg")
                .setCategoryIds(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualDto = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        reflectionEquals(expectedDto, actualDto, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books/add-dune-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-dune-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Delete book: deletes book successfully for valid ID")
    public void delete_ValidBookId_Success() throws Exception {

        Long validBookId = 4L;
        int expectedStatusCode = 204;

        MvcResult mvcResult = mockMvc.perform(delete("/books/{id}", validBookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        int actualStatusCode = mvcResult.getResponse().getStatus();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/add-dune-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-dune-and-relationship.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update book: updates and returns book successfully for valid ID and request")
    public void update_ValidBookIdAndBookRequestDto_SuccessAndReturnBookDto() throws Exception {

        Long testBookId = 4L;

        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Updated Title")
                .setAuthor("Updated Author")
                .setIsbn("9781234567890")
                .setPrice(new BigDecimal("29.99"))
                .setDescription("Updated description")
                .setCoverImage("https://example.com/updated.jpg")
                .setCategoryIds(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(put("/books/{id}", testBookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualDto = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(requestDto.getTitle(), actualDto.getTitle());
        assertEquals(requestDto.getAuthor(), actualDto.getAuthor());
        assertEquals(requestDto.getPrice(), actualDto.getPrice());
        assertEquals(requestDto.getDescription(), actualDto.getDescription());
        assertEquals(requestDto.getCoverImage(), actualDto.getCoverImage());
        assertEquals(requestDto.getCategoryIds(), actualDto.getCategoryIds());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Find book by ID: throws not found exception for invalid ID")
    public void findById_InvalidBookId_ThrowNotFound() throws Exception {

        Long invalidId = 999L;
        int expected = 404;

        MvcResult mvcResult = mockMvc.perform(get("/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        int actual = mvcResult.getResponse().getStatus();
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create book: throws bad request exception for invalid request")
    public void create_InvalidRequestDto_ThrowException() throws Exception {

        int expected = 400;

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Non-existent Book")
                .setAuthor("Unknown Author")
                .setPrice(new BigDecimal("100.00"))
                .setDescription("This book does not exist.")
                .setCoverImage("https://example.com/non-existent.jpg")
                .setCategoryIds(List.of());

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        int actual = mvcResult.getResponse().getStatus();
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Search books by title: returns matching books")
    public void search_ByTitle_ReturnsMatchingBooks() throws Exception {

        String searchTitle = "The Great Gatsby";
        String expectedTitle = "The Great Gatsby";

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("title", searchTitle)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("content");
        BookDto[] books = objectMapper.treeToValue(contentNode, BookDto[].class);

        assertEquals(1, books.length);
        assertEquals(expectedTitle, books[0].getTitle());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Search books by author: returns matching books")
    public void search_ByAuthor_ReturnsMatchingBooks() throws Exception {

        String expectedBookTitle = "1984";
        String searchAuthor = "George Orwell";

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("author", searchAuthor)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("content");
        BookDto[] books = objectMapper.treeToValue(contentNode, BookDto[].class);

        assertEquals(1, books.length);
        assertEquals(expectedBookTitle, books[0].getTitle());
        assertEquals(searchAuthor, books[0].getAuthor());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Search books with no matches: returns empty list")
    public void search_NoMatches_ReturnsEmptyList() throws Exception {

        String nonExistentTitle = "Nonexistent Book Title";

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("title", nonExistentTitle)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode contentNode = rootNode.path("content");
        BookDto[] books = objectMapper.treeToValue(contentNode, BookDto[].class);

        assertEquals(0, books.length);
    }

}
