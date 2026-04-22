package org.example.app.staff;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("StaffMember")
public record StaffMember(
        @Id Long staffId,
        String firstName,
        String lastName,
        String position,
        String email,
        Long departmentId
) {
    public String fullName() {
        return firstName + " " + lastName;
    }
}
