package org.example.app.incomingRequest;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("IncomingRequest")
public record IncomingRequest(
        @Id Long requestId,
        LocalDate submissionDate,
        String senderName,
        String senderType,
        String contactInformation,
        String submissionChannel,
        String subject,
        String requestSummary,
        String priorityLevel,
        String currentStatus,
        LocalDate registrationDate,
        String preliminaryNotes,
        Long officeId
) {
    public static IncomingRequest of(
            LocalDate submissionDate,
            String senderName,
            String senderType,
            String contactInformation,
            String submissionChannel,
            String subject,
            String requestSummary,
            String priorityLevel,
            Long officeId
    ) {
        return new IncomingRequest(
                null,
                submissionDate,
                senderName,
                senderType,
                contactInformation,
                submissionChannel,
                subject,
                requestSummary,
                priorityLevel,
                "Registered",
                LocalDate.now(),
                null,
                officeId
        );
    }
}
