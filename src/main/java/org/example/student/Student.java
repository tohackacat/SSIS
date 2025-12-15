package org.example.student;

import org.example.core.EntityId;

public record Student(EntityId id, EntityId personId, int enrollmentYear) {
}
