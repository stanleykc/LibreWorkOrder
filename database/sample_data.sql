-- Sample data for development and testing
USE libre_work_order;

INSERT INTO work_orders (libre311_service_request_id, title, description, status, assigned_to, priority) VALUES
('SR-2024-001', 'Fix streetlight on Main Street', 'Streetlight at 123 Main Street is not working. Reported by resident.', 'PENDING', NULL, 'MEDIUM'),
('SR-2024-002', 'Pothole repair on Oak Avenue', 'Large pothole at Oak Avenue and 5th Street needs immediate attention.', 'ASSIGNED', 'john.doe@city.gov', 'HIGH'),
('SR-2024-003', 'Graffiti removal at City Hall', 'Graffiti on the east wall of City Hall building needs to be removed.', 'IN_PROGRESS', 'maintenance.team@city.gov', 'LOW'),
('SR-2024-004', 'Broken water main on Pine Street', 'Water main break causing flooding on Pine Street between 2nd and 3rd Ave.', 'COMPLETED', 'water.dept@city.gov', 'URGENT'),
('SR-2024-005', 'Tree branch blocking sidewalk', 'Fallen tree branch blocking sidewalk on Elm Street after storm.', 'PENDING', NULL, 'MEDIUM');