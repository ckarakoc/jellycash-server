package nl.ckarakoc.jellycash.service;

import java.util.List;
import nl.ckarakoc.jellycash.dto.api.v1.category.CreateCategoryRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.category.PartialUpdateCategoryRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.category.UpdateCategoryRequestDto;
import nl.ckarakoc.jellycash.model.Category;
import nl.ckarakoc.jellycash.model.User;

public interface CategoryService {

  List<Category> getAllCategories(User user);

  Category getCategory(Long id, User user);

  Category createCategory(CreateCategoryRequestDto dto, User user);

  Category updateCategory(Long id, UpdateCategoryRequestDto dto, User user);

  Category partialUpdateCategory(Long id, PartialUpdateCategoryRequestDto dto, User user);

  void deleteCategory(Long id, User user);
}
