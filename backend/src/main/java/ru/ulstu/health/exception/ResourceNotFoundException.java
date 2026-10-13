package ru.ulstu.health.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, String id) {
        super(resource + " с id=" + id + " не найден");
    }
}
