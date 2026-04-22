package org.example.app.department;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Department")
public record Department(
        @Id Long departmentId,
        String departmentName,
        Long officeId
) {}
