package org.example.teaching;

import org.example.core.EntityId;

/*
 * An Instructing recod that holds a courseId and a facultyId.
 */
public record Teaching(EntityId id, EntityId courseId, EntityId facultyId, String role) {

}
