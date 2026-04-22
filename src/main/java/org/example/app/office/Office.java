package org.example.app.office;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Office")
public record Office(
        @Id Long officeId,
        String officeName,
        String officeEmail,
        String officeStatus,
        String officeWebsite,
        String workingHours,
        String country,
        String city,
        String address
) {}
