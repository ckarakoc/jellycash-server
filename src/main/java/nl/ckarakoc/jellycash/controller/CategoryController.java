package nl.ckarakoc.jellycash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.jellycash.config.AppConstants.ApiPaths;
import nl.ckarakoc.jellycash.dto.api.v1.category.CreateCategoryRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.category.PartialUpdateCategoryRequestDto;
import nl.ckarakoc.jellycash.dto.api.v1.category.CategoryDto;
import nl.ckarakoc.jellycash.dto.api.v1.category.UpdateCategoryRequestDto;
import nl.ckarakoc.jellycash.model.Category;
import nl.ckarakoc.jellycash.model.User;
import nl.ckarakoc.jellycash.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.Categories)
@Tag(name = "Categories", description = "Operations related to categories")
public class CategoryController {

  private final CategoryService categoryService;
  private final ModelMapper modelMapper;

  @Operation(
      summary = "List all categories",
      description = "Retrieves all available categories of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Invalid request")
      }
  )
  @GetMapping
  public ResponseEntity<List<CategoryDto>> getAllCategories(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(categoryService.getAllCategories(user).stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList());
  }

  @Operation(
      summary = "Get category by ID",
      description = "Retrieves a specific category by its ID of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Category not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> getCategory(
      @PathVariable @Schema(description = "ID of the category", example = "1") Long id,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(modelMapper.map(categoryService.getCategory(id, user), CategoryDto.class));
  }

  @Operation(
      summary = "Create a new category",
      description = "Creates a new category with the provided data for the logged in user",
      responses = {
          @ApiResponse(responseCode = "201", description = "Created"),
          @ApiResponse(responseCode = "400", description = "Bad request: Missing or invalid data"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PostMapping
  public ResponseEntity<Void> createCategory(
      @RequestBody @Valid CreateCategoryRequestDto dto,
      @AuthenticationPrincipal User user) {
    Category created = categoryService.createCategory(dto, user);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getCategoryId())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(
      summary = "Fully update a category",
      description = "Updates an existing category by replacing all its data of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data or ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Category not found"),
          @ApiResponse(responseCode = "409", description = "Conflict: Update conflict"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PutMapping("/{id}")
  public ResponseEntity<CategoryDto> updateCategory(
      @PathVariable @Schema(description = "ID of the category", example = "1") Long id,
      @RequestBody @Valid UpdateCategoryRequestDto dto,
      @AuthenticationPrincipal User user) {

    return ResponseEntity.ok(modelMapper.map(categoryService.updateCategory(id, dto, user), CategoryDto.class));
  }

  @Operation(
      summary = "Partially update a category",
      description = "Updates one or more fields of an existing category of the logged in user",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid data or ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Category not found"),
          @ApiResponse(responseCode = "409", description = "Conflict: Update conflict"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PatchMapping("/{id}")
  public ResponseEntity<CategoryDto> partialUpdateCategory(
      @PathVariable @Schema(description = "ID of the category", example = "1") Long id,
      @RequestBody @Valid PartialUpdateCategoryRequestDto dto,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(modelMapper.map(categoryService.partialUpdateCategory(id, dto, user), CategoryDto.class));
  }

  @Operation(
      summary = "Delete a category",
      description = "Deletes a category by its ID of the logged in user",
      responses = {
          @ApiResponse(responseCode = "204", description = "No Content: Category deleted successfully"),
          @ApiResponse(responseCode = "400", description = "Bad request: Invalid ID"),
          @ApiResponse(responseCode = "401", description = "Unauthorized"),
          @ApiResponse(responseCode = "403", description = "Forbidden"),
          @ApiResponse(responseCode = "404", description = "Category not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(
      @PathVariable @Schema(description = "ID of the category", example = "1") Long id,
      @AuthenticationPrincipal User user) {
    categoryService.deleteCategory(id, user);
    return ResponseEntity.noContent().build();
  }

}
