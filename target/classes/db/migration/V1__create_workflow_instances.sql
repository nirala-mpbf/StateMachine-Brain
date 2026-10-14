CREATE SCHEMA IF NOT EXISTS workflow_brain;

CREATE TABLE workflow_brain.workflow_instances (
   reference_id VARCHAR(100) PRIMARY KEY,
   current_state VARCHAR(50) NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);