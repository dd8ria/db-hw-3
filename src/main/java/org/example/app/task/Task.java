package org.example.app.task;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("Task")
public record Task(
        @Id Long taskId,
        String taskDescription,
        LocalDate dueDate,
        String taskStatus,
        LocalDate completionDate,
        Long requestId,
        Long departmentId,
        Long staffId
) {
    public Task withStatus(String newStatus, LocalDate newCompletionDate, Long newStaffId) {
        return new Task(
                taskId, taskDescription, dueDate,
                newStatus, newCompletionDate,
                requestId, departmentId, newStaffId
        );
    }
}
