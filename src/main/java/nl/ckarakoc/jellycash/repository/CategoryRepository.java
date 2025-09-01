package nl.ckarakoc.jellycash.repository;

import nl.ckarakoc.jellycash.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
