package org.example.course;

import org.example.core.EntityId;

public record Course(EntityId id, String code, String name, int credits) {

}
