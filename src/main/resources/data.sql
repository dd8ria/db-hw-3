INSERT INTO Office (office_name, office_email, office_status, office_website, working_hours, country, city, address)
VALUES
('Office of the President of Ukraine', 'office@opu.gov.ua', 'Active', 'https://www.president.gov.ua', '09:00-18:00', 'Ukraine', 'Kyiv', '11 Bankova Street'),
('Regional Liaison Office',            'liaison@opu.gov.ua', 'Active', 'https://www.president.gov.ua', '09:00-18:00', 'Ukraine', 'Lviv', '15 Svobody Avenue'),
('Administrative Support Office',      'support@opu.gov.ua', 'Under Review', 'https://www.president.gov.ua', '09:00-18:00', 'Ukraine', 'Dnipro', '8 Central Square');

INSERT INTO Department (department_name, office_id)
VALUES
('Citizens Appeals Department',      1),
('Document Coordination Department', 1),
('Regional Communications Department', 2),
('Administrative Monitoring Department', 3);

INSERT INTO StaffMember (first_name, last_name, position, email, department_id)
VALUES
('Olena',  'Koval',     'Request Intake Officer',   'olena.koval@opu.gov.ua',    1),
('Andrii', 'Melnyk',    'Department Coordinator',   'andrii.melnyk@opu.gov.ua',  2),
('Iryna',  'Boyko',     'Department Coordinator',   'iryna.boyko@opu.gov.ua',    3),
('Taras',  'Hrytsenko', 'Office Manager',           'taras.hrytsenko@opu.gov.ua',4),
('Marta',  'Shevchuk',  'Senior Specialist',        'marta.shevchuk@opu.gov.ua', 2);

INSERT INTO IncomingRequest (submission_date, sender_name, sender_type, contact_information, submission_channel, subject, request_summary, priority_level, current_status, registration_date, preliminary_notes, office_id)
VALUES
('2026-04-01','Ivan Petrenko','Citizen','ivan.petrenko@mail.com','Email','Request for public information','Citizen requests access to administrative information.','High','Registered','2026-04-01','Initial review completed.',1),
('2026-04-02','NGO Transparency Watch','Organization','contact@tw.org','Web Form','Appeal regarding document access','Request concerning access to internal coordination documents.','Medium','Assigned','2026-04-02','Forwarded for department review.',1),
('2026-04-03','Regional Community Council','Institution','office@rcc.ua','Email','Proposal for regional cooperation','Proposal regarding coordination with regional institutions.','Low','In Progress','2026-04-03','Relevant for regional communications.',2),
('2026-04-04','Maria Sydorenko','Citizen','maria.sydorenko@mail.com','Phone','Complaint about delayed response','Citizen reports delayed handling of a previous request.','High','Assigned','2026-04-08','Requires deadline verification.',3);

INSERT INTO OperationalMetrics (document_flow_rate, completed_tasks_count, incoming_requests_volume, overdue_tasks_count, office_id)
VALUES
(12.50, 18, 45, 3, 1),
(7.80,  10, 20, 1, 2),
(5.20,  8,  15, 2, 3);

INSERT INTO RequestRouting (routing_date, routing_status, request_id, department_id)
VALUES
('2026-04-01', 'Routed', 1, 1),
('2026-04-02', 'Routed', 2, 2),
('2026-04-03', 'Routed', 3, 3),
('2026-04-04', 'Routed', 4, 4);

INSERT INTO RequestAssignment (assignment_date, due_date, processing_priority, internal_comment, request_id, department_id, assigned_by_staff_id)
VALUES
('2026-04-01', '2026-04-07', 'High',   'Urgent review requested.',      1, 1, 4),
('2026-04-02', '2026-04-10', 'Medium', 'Document check required.',      2, 2, 4),
('2026-04-03', '2026-04-12', 'Low',    'Regional coordination task.',   3, 3, 4),
('2026-04-04', '2026-04-08', 'High',   'Monitor overdue risk.',         4, 4, 4);

INSERT INTO Task (task_description, due_date, task_status, completion_date, request_id, department_id, staff_id)
VALUES
('Register and verify request details',    '2026-04-02', 'Completed',   '2026-04-02', 1, 1, 1),
('Prepare internal document package',      '2026-04-06', 'In Progress', NULL,          2, 2, 2),
('Coordinate with regional office',        '2026-04-09', 'Open',        NULL,          3, 3, 3),
('Check overdue response history',         '2026-04-06', 'Overdue',     NULL,          4, 4, 4);

INSERT INTO Document (document_type, document_title, document_status, confidentiality_level, document_retention_policy, creation_date, registration_date, request_id, prepared_by_staff_id)
VALUES
('Memo',              'Initial Review Memo',         'Approved',  'Internal',     '1 year',  '2026-04-01', '2026-04-01', 1, 1),
('Internal Report',   'Document Access Analysis',    'In Review', 'Confidential', '3 years', '2026-04-02', '2026-04-03', 2, 2),
('Coordination Letter','Regional Cooperation Draft', 'Draft',     'Internal',     '2 years', '2026-04-03', '2026-04-03', 3, 3),
('Monitoring Note',   'Delayed Response Assessment', 'Approved',  'Confidential', '2 years', '2026-04-04', '2026-04-04', 4, 4);
