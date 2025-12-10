package org.example.teaching;

import org.example.EntityId;

public record Teaching(EntityId id, EntityId courseId, EntityId facultyId, String role) {

}
