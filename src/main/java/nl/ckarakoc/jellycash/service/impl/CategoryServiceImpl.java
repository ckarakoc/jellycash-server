package nl.ckarakoc.jellycash.service.impl;

import java.util.List;
import nl.ckarakoc.jellycash.dto.api.v1.category.CreateCategoryRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.category.PartialUpdateCategoryRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.category.UpdateCategoryRequestDto;
import nl.ckarakoc.jellycash.exception.CreationException;
import nl.ckarakoc.jellycash.exception.UpdateEntityNotFoundException;
import nl.ckarakoc.jellycash.model.Category;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.repository.CategoryRepository;
import nl.ckarakoc.jellycash.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final ModelMapper modelMapper;
  private final ModelMapper skippNullMapper;

  public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, @Qualifier("skipNullMapper") ModelMapper skippNullMapper) {
    this.categoryRepository = categoryRepository;
    this.modelMapper = modelMapper;
    this.skippNullMapper = skippNullMapper;
  }

  @Override
  public List<Category> getAllCategories(User user) {
    return categoryRepository.findAllByUser(user);
  }

  @Override
  public Category getCategory(Long id, User user) {
    return categoryRepository.findByCategoryIdAndUser(id, user)
        .orElseThrow(() -> new UpdateEntityNotFoundException("Category with id " + id + " not found"));
  }

  @Transactional
  @Override
  public Category createCategory(CreateCategoryRequestDto dto, User user) {
    categoryNameCheck(dto.getName(), user);
    Category category = modelMapper.map(dto, Category.class);
    category.setUser(user);
    return categoryRepository.save(category);
  }

  @Transactional
  @Modifying
  @Override
  public Category updateCategory(Long id, UpdateCategoryRequestDto dto, User user) {
    Category category = getCategory(id, user);
    categoryNameCheck(dto.getName(), user);
    modelMapper.map(dto, category);
    return categoryRepository.save(category);
  }

  @Transactional
  @Modifying
  @Override
  public Category partialUpdateCategory(Long id, PartialUpdateCategoryRequestDto dto, User user) {
    Category category = getCategory(id, user);
    categoryNameCheck(dto.getName(), user);
    skippNullMapper.map(dto, category);
    return categoryRepository.save(category);
  }

  @Transactional
  @Override
  public void deleteCategory(Long id, User user) {
    categoryRepository.deleteByCategoryIdAndUser(id, user);
  }

  private void categoryNameCheck(String name, User user) {
    boolean alreadyExists = categoryRepository.existsByNameAndUser(name, user);
    if (alreadyExists) {
      throw new CreationException(Category.class, "Category name (" + name + ") already exists.");
    }
  }
}
