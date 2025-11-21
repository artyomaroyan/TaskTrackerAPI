package org.tasktracker.demo.exception;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 22:30:24
 */
public class DataMappingException extends RuntimeException {
    public DataMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}