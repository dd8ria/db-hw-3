package org.example.app.report;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReportRepository {

    private final JdbcClient jdbcClient;

    public ReportRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<RequestStatusRow> requestStatusSummary() {
        return jdbcClient.sql("""
                SELECT
                    ir.request_id,
                    ir.submission_date,
                    ir.sender_name,
                    ir.subject,
                    d.department_name        AS assigned_department,
                    ir.current_status,
                    COUNT(DISTINCT doc.document_id) AS related_document_count,
                    COUNT(DISTINCT t.task_id)       AS related_task_count,
                    GREATEST(
                        COALESCE(MAX(doc.registration_date), '1000-01-01'),
                        COALESCE(MAX(t.completion_date),     '1000-01-01'),
                        COALESCE(ir.registration_date,       '1000-01-01')
                    ) AS last_update_date
                FROM IncomingRequest ir
                LEFT JOIN RequestAssignment ra ON ir.request_id   = ra.request_id
                LEFT JOIN Department d         ON ra.department_id = d.department_id
                LEFT JOIN Document doc         ON ir.request_id   = doc.request_id
                LEFT JOIN Task t               ON ir.request_id   = t.request_id
                GROUP BY
                    ir.request_id, ir.submission_date, ir.sender_name,
                    ir.subject, d.department_name, ir.current_status, ir.registration_date
                ORDER BY ir.request_id
                """)
                .query(RequestStatusRow.class)
                .list();
    }

    public record RequestStatusRow(
            Long requestId,
            LocalDate submissionDate,
            String senderName,
            String subject,
            String assignedDepartment,
            String currentStatus,
            Long relatedDocumentCount,
            Long relatedTaskCount,
            LocalDate lastUpdateDate
    ) {}

    public List<OverdueTaskRow> overdueTasksReport() {
        return jdbcClient.sql("""
                SELECT
                    t.task_id,
                    t.request_id,
                    t.task_description,
                    d.department_name                              AS responsible_department,
                    CONCAT(sm.first_name, ' ', sm.last_name)       AS responsible_staff_member,
                    t.due_date,
                    t.task_status                                  AS current_status,
                    DATEDIFF(CURDATE(), t.due_date)                AS days_overdue
                FROM Task t
                LEFT JOIN Department  d  ON t.department_id = d.department_id
                LEFT JOIN StaffMember sm ON t.staff_id      = sm.staff_id
                WHERE t.due_date < CURDATE()
                  AND (t.task_status IS NULL OR t.task_status <> 'Completed')
                ORDER BY t.due_date
                """)
                .query(OverdueTaskRow.class)
                .list();
    }

    public record OverdueTaskRow(
            Long taskId,
            Long requestId,
            String taskDescription,
            String responsibleDepartment,
            String responsibleStaffMember,
            LocalDate dueDate,
            String currentStatus,
            Integer daysOverdue
    ) {}

    public List<DepartmentWorkloadRow> departmentWorkloadReport() {
        return jdbcClient.sql("""
                SELECT
                    d.department_id,
                    d.department_name,
                    COUNT(DISTINCT CASE
                        WHEN ir.current_status IS NULL OR ir.current_status <> 'Completed'
                        THEN ra.request_id END)                     AS number_of_active_requests,
                    COUNT(DISTINCT CASE
                        WHEN t.task_status IS NULL OR t.task_status <> 'Completed'
                        THEN t.task_id END)                         AS number_of_open_tasks,
                    COUNT(DISTINCT CASE
                        WHEN t.task_status = 'Completed'
                        THEN t.task_id END)                         AS number_of_completed_tasks,
                    COUNT(DISTINCT CASE
                        WHEN t.due_date < CURDATE()
                         AND (t.task_status IS NULL OR t.task_status <> 'Completed')
                        THEN t.task_id END)                         AS overdue_tasks_count,
                    AVG(CASE
                        WHEN ir.registration_date IS NOT NULL AND t.completion_date IS NOT NULL
                        THEN DATEDIFF(t.completion_date, ir.registration_date) END) AS average_processing_time
                FROM Department d
                LEFT JOIN RequestAssignment ra ON d.department_id  = ra.department_id
                LEFT JOIN IncomingRequest ir   ON ra.request_id    = ir.request_id
                LEFT JOIN Task t               ON d.department_id  = t.department_id
                GROUP BY d.department_id, d.department_name
                ORDER BY d.department_id
                """)
                .query(DepartmentWorkloadRow.class)
                .list();
    }

    public record DepartmentWorkloadRow(
            Long departmentId,
            String departmentName,
            Long numberOfActiveRequests,
            Long numberOfOpenTasks,
            Long numberOfCompletedTasks,
            Long overdueTasksCount,
            BigDecimal averageProcessingTime
    ) {}

    public List<DocumentProcessingRow> documentProcessingReport() {
        return jdbcClient.sql("""
                SELECT
                    doc.document_id,
                    doc.request_id,
                    doc.document_type,
                    doc.document_title,
                    CONCAT(sm.first_name, ' ', sm.last_name) AS prepared_by,
                    doc.registration_date,
                    doc.confidentiality_level,
                    doc.document_status
                FROM Document doc
                LEFT JOIN StaffMember sm ON doc.prepared_by_staff_id = sm.staff_id
                ORDER BY doc.document_id
                """)
                .query(DocumentProcessingRow.class)
                .list();
    }

    public record DocumentProcessingRow(
            Long documentId,
            Long requestId,
            String documentType,
            String documentTitle,
            String preparedBy,
            LocalDate registrationDate,
            String confidentialityLevel,
            String documentStatus
    ) {}
}
