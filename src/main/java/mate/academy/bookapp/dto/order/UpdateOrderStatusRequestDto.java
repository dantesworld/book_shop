package mate.academy.bookapp.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.bookapp.model.Order;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private Order.Status status;
}
