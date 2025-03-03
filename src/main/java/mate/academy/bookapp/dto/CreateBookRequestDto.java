package mate.academy.bookapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String author;
    @NotEmpty
    @Size(max = 13, message = "ISBN size should not be more than 13")
    @Pattern(regexp = "^[0-9]*$", message = "ISBN must contain only numbers")
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @Size(min = 0, max = 1234)
    private String description;
    private String coverImage;
}
