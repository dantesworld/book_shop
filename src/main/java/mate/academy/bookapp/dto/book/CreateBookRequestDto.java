package mate.academy.bookapp.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    @Size(max = 13, message = "ISBN size should not be more than 13")
    @Pattern(regexp = "^[0-9]*$", message = "ISBN must contain only numbers")
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    @Size(min = 0, max = 1234)
    private String description;
    private String coverImage;
    @NotEmpty
    private List<Long> categoryIds;
}
