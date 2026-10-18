package com.example.statemachine.repository;

import com.example.statemachine.dto.WorkflowDetails;
import com.example.statemachine.statemachine.state.WorkflowState;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WorkflowRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public WorkflowRepository(NamedParameterJdbcTemplate jdbc) {
        this.namedParameterJdbcTemplate = jdbc;
    }

    public void create(String referenceId, WorkflowState initialState) {

        String sql = """
                    INSERT INTO workflow_brain.workflow_instances
                    (reference_id, current_state)
                    VALUES (:referenceId, :state)
                    """;

        Map<String, Object> params = Map.of(
                "referenceId", referenceId,
                "state", initialState.name()
        );

        namedParameterJdbcTemplate.update(sql, params);
    }

    public Optional<WorkflowDetails> findByReferenceId(String referenceId) {

        String sql = """
                    SELECT reference_id, current_state
                    FROM workflow_brain.workflow_instances
                    WHERE reference_id = :referenceId
                    """;

        Map<String, Object> params = Map.of("referenceId", referenceId);

        List<WorkflowDetails> workflows = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
                    String id = rs.getString("reference_id");
                    String state = rs.getString("current_state");

                    WorkflowState workflowState = WorkflowState.valueOf(state);
                    WorkflowDetails details = new WorkflowDetails();
                    details.setReferenceId(id);
                    details.setCurrentState(workflowState);
                    return details;
                }
        );
        return  workflows.stream().findFirst();
    }

    public boolean updateState(String referenceId, WorkflowState expectedState, WorkflowState nextState) {

        String sql ="""
                    UPDATE workflow_brain.workflow_instances
                    SET current_state = :nextState, updated_at = CURRENT_TIMESTAMP
                    WHERE reference_id = :referenceId AND current_state = :expectedState
                    """;

        Map<String, Object> params = Map.of(
                "referenceId", referenceId,
                "expectedState", expectedState.name(),
                "nextState", nextState.name()
        );

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);
        return rowsAffected == 1;
    }
}
