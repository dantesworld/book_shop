package mate.academy.bookapp.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank
    private String name;
    private String description;
}
