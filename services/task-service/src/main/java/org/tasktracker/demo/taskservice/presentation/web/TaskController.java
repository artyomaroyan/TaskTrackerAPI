package org.tasktracker.demo.taskservice.presentation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tasktracker.demo.taskservice.application.ports.in.TaskService;

/**
 * Author: Artyom Aroyan
 * Date: 01.01.26
 * Time: 22:18:58
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;
}