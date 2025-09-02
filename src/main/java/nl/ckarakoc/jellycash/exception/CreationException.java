package nl.ckarakoc.jellycash.exception;

public class CreationException extends RuntimeException {

  private final Class<?> entityClass;

  public CreationException(Class<?> entityClass, String message) {
    super(message);
    this.entityClass = entityClass;
  }

  public Class<?> getEntityClass() {
    return entityClass;
  }

  @Override
  public String getMessage() {
    return "Failed to create " + entityClass.getSimpleName() + ": " + super.getMessage();
  }
}

