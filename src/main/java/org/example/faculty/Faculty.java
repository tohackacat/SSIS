package org.example.faculty;

import org.example.core.EntityId;

public record Faculty(EntityId id, EntityId personId, String department, double rate, double hours) {

}
