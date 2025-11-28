package com.example.tasker.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Common ID + timestamps for all entities.
 * Includes soft delete support.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @Column(nullable = false)
    private boolean deleted = false;

    private Instant deletedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }
}
