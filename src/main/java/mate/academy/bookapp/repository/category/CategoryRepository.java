package mate.academy.bookapp.repository.category;

import mate.academy.bookapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
