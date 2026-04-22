SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS RequestAssignment;
DROP TABLE IF EXISTS RequestRouting;
DROP TABLE IF EXISTS IncomingRequest;
DROP TABLE IF EXISTS StaffMember;
DROP TABLE IF EXISTS Document;
DROP TABLE IF EXISTS OperationalMetrics;
DROP TABLE IF EXISTS Department;
DROP TABLE IF EXISTS Office;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE Office (
    office_id    INT AUTO_INCREMENT PRIMARY KEY,
    office_name  VARCHAR(150) NOT NULL,
    office_email VARCHAR(150),
    office_status   VARCHAR(30),
    office_website  VARCHAR(255),
    working_hours   VARCHAR(100),
    country  VARCHAR(80),
    city     VARCHAR(100),
    address  VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE Department (
    department_id   INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(120) NOT NULL,
    office_id       INT NOT NULL,
    CONSTRAINT fk_department_office
        FOREIGN KEY (office_id) REFERENCES Office(office_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE StaffMember (
    staff_id      INT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    position      VARCHAR(150),
    email         VARCHAR(255),
    department_id INT NOT NULL,
    CONSTRAINT fk_staff_department
        FOREIGN KEY (department_id) REFERENCES Department(department_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IncomingRequest (
    request_id          INT AUTO_INCREMENT PRIMARY KEY,
    submission_date     DATE NOT NULL,
    sender_name         VARCHAR(255) NOT NULL,
    sender_type         VARCHAR(100),
    contact_information VARCHAR(255),
    submission_channel  VARCHAR(100),
    subject             VARCHAR(255) NOT NULL,
    request_summary     TEXT,
    priority_level      VARCHAR(50),
    current_status      VARCHAR(50),
    registration_date   DATE,
    preliminary_notes   TEXT,
    office_id           INT NOT NULL,
    CONSTRAINT fk_request_office
        FOREIGN KEY (office_id) REFERENCES Office(office_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE RequestRouting (
    routing_id      INT AUTO_INCREMENT PRIMARY KEY,
    routing_date    DATE,
    routing_status  VARCHAR(50),
    request_id      INT NOT NULL,
    department_id   INT NOT NULL,
    CONSTRAINT fk_routing_request
        FOREIGN KEY (request_id) REFERENCES IncomingRequest(request_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_routing_department
        FOREIGN KEY (department_id) REFERENCES Department(department_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE RequestAssignment (
    assignment_id       INT AUTO_INCREMENT PRIMARY KEY,
    assignment_date     DATE,
    due_date            DATE,
    processing_priority VARCHAR(50),
    internal_comment    TEXT,
    request_id          INT NOT NULL,
    department_id       INT NOT NULL,
    assigned_by_staff_id INT,
    CONSTRAINT fk_assignment_request
        FOREIGN KEY (request_id) REFERENCES IncomingRequest(request_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_assignment_department
        FOREIGN KEY (department_id) REFERENCES Department(department_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_assignment_staff
        FOREIGN KEY (assigned_by_staff_id) REFERENCES StaffMember(staff_id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Task (
    task_id          INT AUTO_INCREMENT PRIMARY KEY,
    task_description TEXT NOT NULL,
    due_date         DATE,
    task_status      VARCHAR(50),
    completion_date  DATE,
    request_id       INT NOT NULL,
    department_id    INT NOT NULL,
    staff_id         INT,
    CONSTRAINT fk_task_request
        FOREIGN KEY (request_id) REFERENCES IncomingRequest(request_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_task_department
        FOREIGN KEY (department_id) REFERENCES Department(department_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_task_staff
        FOREIGN KEY (staff_id) REFERENCES StaffMember(staff_id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Document (
    document_id               INT AUTO_INCREMENT PRIMARY KEY,
    document_type             VARCHAR(100),
    document_title            VARCHAR(255),
    document_status           VARCHAR(30),
    confidentiality_level     VARCHAR(50),
    document_retention_policy VARCHAR(255),
    creation_date             DATE,
    registration_date         DATE,
    request_id                INT,
    prepared_by_staff_id      INT,
    CONSTRAINT fk_document_request
        FOREIGN KEY (request_id) REFERENCES IncomingRequest(request_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_document_staff
        FOREIGN KEY (prepared_by_staff_id) REFERENCES StaffMember(staff_id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE OperationalMetrics (
    metric_id                INT AUTO_INCREMENT PRIMARY KEY,
    document_flow_rate       DECIMAL(10,2),
    completed_tasks_count    INT,
    incoming_requests_volume INT,
    overdue_tasks_count      INT,
    office_id                INT NOT NULL,
    CONSTRAINT fk_metrics_office
        FOREIGN KEY (office_id) REFERENCES Office(office_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;
