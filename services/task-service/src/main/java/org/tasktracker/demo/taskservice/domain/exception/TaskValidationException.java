package org.tasktracker.demo.taskservice.domain.exception;



/**
 * Author: Artyom Aroyan
 * Date: 27.01.26
 * Time: 23:19:56
 */public class TaskLogcValidationException extends RuntimeException {
  public TaskLogcValidationException(String message) {
    super(message);
  }
}