package com.example.tasker.domain;



import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Composite key (taskId, labelId) for TaskLabel.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskLabelId implements Serializable {
    private Long taskId;
    private Long labelId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskLabelId that)) return false;
        return Objects.equals(taskId, that.taskId) &&
                Objects.equals(labelId, that.labelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, labelId);
    }
}

