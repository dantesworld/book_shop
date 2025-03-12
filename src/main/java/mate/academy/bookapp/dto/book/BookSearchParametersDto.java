package mate.academy.bookapp.dto.book;

public record BookSearchParametersDto(String[] title, String[] author, String[] isbn) {
}
