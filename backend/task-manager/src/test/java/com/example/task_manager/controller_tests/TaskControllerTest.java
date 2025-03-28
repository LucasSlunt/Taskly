package com.example.task_manager.controller_tests;

import com.example.task_manager.controller.TaskController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
public class TaskControllerTest {

}
