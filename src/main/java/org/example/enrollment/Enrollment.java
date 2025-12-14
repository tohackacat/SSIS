package org.example.enrollment;

import org.example.core.EntityId;

public record Enrollment(EntityId id, EntityId studentId, EntityId courseId, String grade) {
    
}
