package nl.ckarakoc.jellycash.repository;

import java.util.List;
import java.util.Optional;
import nl.ckarakoc.jellycash.model.Category;
import nl.ckarakoc.jellycash.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findAllByUser(User user);

  Optional<Category> findByCategoryIdAndUser(Long id, User user);

  void deleteByCategoryIdAndUser(Long categoryId, User user);

  boolean existsByNameAndUser(String name, User user);
}
